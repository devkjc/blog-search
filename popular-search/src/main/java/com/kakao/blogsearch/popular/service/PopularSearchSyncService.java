package com.kakao.blogsearch.popular.service;

import com.kakao.blogsearch.popular.domain.PopularSearch;
import com.kakao.blogsearch.popular.repository.PopularSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@EnableScheduling
@RequiredArgsConstructor
public class PopularSearchSyncService {

    private final PopularSearchRepository popularSearchRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private final static String POPULAR_SEARCH_KEY = "popular_search";

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void syncPopularSearch() {

        Set<ZSetOperations.TypedTuple<String>> popularSearchSet =
                redisTemplate.opsForZSet().reverseRangeWithScores(POPULAR_SEARCH_KEY, 0, -1);

        if (popularSearchSet != null && !popularSearchSet.isEmpty()) {
            List<PopularSearch> popularSearchList = new ArrayList<>();
            popularSearchSet.forEach(tuple -> {
                String query = tuple.getValue();
                long score = tuple.getScore() != null ? tuple.getScore().longValue() : 0;
                PopularSearch popularSearch =
                        popularSearchRepository.findByQuery(query).orElseGet(() -> new PopularSearch(query));
                if (popularSearch.getCount() != score) {
                    popularSearch.setCount(score);
                    popularSearchList.add(popularSearch);
                }
            });
            popularSearchRepository.saveAll(popularSearchList);
        }
    }

}
