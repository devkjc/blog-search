package com.kakao.blogsearch.popular.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PopularSearchSyncServiceTest extends RedisTest {

    @Autowired
    private PopularSearchSyncService popularSearchSyncService;
    @Autowired
    private PopularSearchRedisServiceImpl popularSearchRedisService;

    @Test
    @DisplayName("PopularSearchSyncService 동기화 테스트")
    public void syncPopularSearchTest() {
        // given
        // Redis에 검색어 3개 추가
        popularSearchRedisService.saveAndAddCount("java");
        popularSearchRedisService.saveAndAddCount("spring");
        popularSearchRedisService.saveAndAddCount("kotlin");

        // when
        popularSearchSyncService.syncPopularSearch();

        // then
        // 검색어 3개가 H2 DB에 추가되었는지 검증
        assertThat(popularSearchRedisService.getPopularSearchCountFromH2("java")).isEqualTo(1L);
        assertThat(popularSearchRedisService.getPopularSearchCountFromH2("spring")).isEqualTo(1L);
        assertThat(popularSearchRedisService.getPopularSearchCountFromH2("kotlin")).isEqualTo(1L);

        // Redis에 검색어 2개 추가
        popularSearchRedisService.saveAndAddCount("java");
        popularSearchRedisService.saveAndAddCount("spring");

        // when
        popularSearchSyncService.syncPopularSearch();

        // then
        // 검색어 2개가 H2 DB에 업데이트 되었는지 검증
        assertThat(popularSearchRedisService.getPopularSearchCountFromH2("java")).isEqualTo(2L);
        assertThat(popularSearchRedisService.getPopularSearchCountFromH2("spring")).isEqualTo(2L);
        assertThat(popularSearchRedisService.getPopularSearchCountFromH2("kotlin")).isEqualTo(1L);
    }
}