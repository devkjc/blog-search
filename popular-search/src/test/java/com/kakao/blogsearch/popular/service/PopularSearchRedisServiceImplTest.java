package com.kakao.blogsearch.popular.service;

import com.kakao.blogsearch.popular.dto.PopularSearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PopularSearchRedisServiceImplTest extends RedisTest {

    @Autowired
    private PopularSearchRedisServiceImpl popularSearchRedisService;

    String query = "검색어";

    @Test
    void Redis_검색어_저장() {
        popularSearchRedisService.saveAndAddCount(query);
        popularSearchRedisService.saveAndAddCount(query);

        long redisScore = popularSearchRedisService.getPopularSearchCountFromRedis(query);
        assertThat(redisScore).isEqualTo(2);
    }

    @Test
    void Redis_인기_검색어_10개_조회() {

        //given
        // 검색어0 - 20
        // 검색어1 - 19
        // 검색어2 - 18
        // ...
        // 검색어18 - 2
        // 검색어19 - 1
        int max_size = 20;
        for (int i = 0; i < max_size; i++) {
            for (int j = i; j < max_size; j++) {
                popularSearchRedisService.saveAndAddCount(query + i);
            }
        }

        List<PopularSearchResponse> topSearches = popularSearchRedisService.getTop10PopularSearchResponse();

        assertThat(topSearches.size()).isEqualTo(10);
        assertThat(topSearches.get(0).query()).isEqualTo(query + 0);
        assertThat(topSearches.get(0).count()).isEqualTo(max_size);
    }
}