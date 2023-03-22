package com.kakao.blogsearch.popular.service;

import com.kakao.blogsearch.popular.domain.PopularSearch;
import com.kakao.blogsearch.popular.dto.PopularSearchResponse;
import com.kakao.blogsearch.popular.repository.PopularSearchRepository;
import lombok.RequiredArgsConstructor;
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

    private final static String POPULAR_SEARCH_KEY = "popular_search";

    @Override
    public void saveAndAddCount(String query) {
        PopularSearch popularSearch = popularSearchRepository.findByQuery(query).orElseGet(() -> PopularSearch.builder().query(query).build());
        popularSearch.addCount();
        popularSearchRepository.save(popularSearch);

        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(POPULAR_SEARCH_KEY, popularSearch.getQuery(), 1);
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

}
