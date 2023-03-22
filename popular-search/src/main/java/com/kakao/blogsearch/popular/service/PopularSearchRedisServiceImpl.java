package com.kakao.blogsearch.popular.service;

import com.kakao.blogsearch.popular.domain.PopularSearch;
import com.kakao.blogsearch.popular.dto.PopularSearchResponse;
import com.kakao.blogsearch.popular.repository.PopularSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PopularSearchRedisServiceImpl implements PopularSearchService {

    private final PopularSearchRepository popularSearchRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.redis.popular-key}")
    private String POPULAR_SEARCH_KEY;

    @Override
    public void saveAndAddCount(String query) {
        redisTemplate.opsForZSet().incrementScore(POPULAR_SEARCH_KEY, query, 1);
    }

    @Override
    public List<PopularSearchResponse> getTop10PopularSearchResponse() {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        Set<ZSetOperations.TypedTuple<Object>> popularSearchSet =
                zSetOps.reverseRangeWithScores(POPULAR_SEARCH_KEY, 0, 9);

        if (popularSearchSet != null && !popularSearchSet.isEmpty()) {
            return popularSearchSet.stream()
                    .filter(Objects::nonNull)
                    .map(this::toPopularSearchResponse)
                    .toList();
        }

        return popularSearchRepository.findTop10ByOrderByCountDescIdDesc()
                .stream()
                .map(PopularSearchResponse::of)
                .toList();
    }

    private PopularSearchResponse toPopularSearchResponse(ZSetOperations.TypedTuple<Object> tuple) {
        return PopularSearchResponse.of(
                Objects.toString(tuple.getValue()),
                tuple.getScore() != null ? tuple.getScore().longValue() : 0);
    }

    public long getPopularSearchCountFromRedis(String query) {
        Double score = redisTemplate.opsForZSet().score(POPULAR_SEARCH_KEY, query);
        return score != null ? score.longValue() : 0;
    }

    public long getPopularSearchCountFromH2(String query) {
        return popularSearchRepository.findByQuery(query).map(PopularSearch::getCount).orElse(0L);
    }
}
