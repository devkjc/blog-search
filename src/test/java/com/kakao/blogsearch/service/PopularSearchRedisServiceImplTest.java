package com.kakao.blogsearch.service;

import com.kakao.blogsearch.domain.PopularSearch;
import com.kakao.blogsearch.dto.PopularSearchResponse;
import com.kakao.blogsearch.repository.PopularSearchRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PopularSearchRedisServiceImplTest {

    @Autowired
    private PopularSearchRedisServiceImpl popularSearchRedisServiceImpl;

    @Autowired
    private PopularSearchRepository popularSearchRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    String query = "검색어";

    @BeforeAll
    void setUp() {
        popularSearchRepository.deleteAll();
    }

    @Test
    void Redis_검색어_저장() {
        popularSearchRedisServiceImpl.saveAndAddCount(query);
        popularSearchRedisServiceImpl.saveAndAddCount(query);

        PopularSearch popularSearch = popularSearchRepository.findByQuery(query).orElse(null);
        assertThat(popularSearch).isNotNull();
        assertThat(popularSearch.getCount()).isEqualTo(2);

        Double redisScore = redisTemplate.opsForZSet().score("popular_search", query);
        assertThat(redisScore).isNotNull();
        assertThat(redisScore.intValue()).isEqualTo(2);
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
                popularSearchRedisServiceImpl.saveAndAddCount(query + i);
            }
        }

        List<PopularSearchResponse> topSearches = popularSearchRedisServiceImpl.getTop10PopularSearchResponse();

        assertThat(topSearches.size()).isEqualTo(10);
        assertThat(topSearches.get(0).query()).isEqualTo(query + 0);
        assertThat(topSearches.get(0).count()).isEqualTo(max_size);
    }
}