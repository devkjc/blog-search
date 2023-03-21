package com.kakao.blogsearch.service;

import com.kakao.blogsearch.domain.PopularSearch;
import com.kakao.blogsearch.dto.PopularSearchResponse;
import com.kakao.blogsearch.repository.PopularSearchRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PopularSearchServiceTest {

    @Autowired
    private PopularSearchService popularSearchService;

    @Autowired
    private PopularSearchRepository popularSearchRepository;

    String query = "검색어";

    @BeforeAll
    void setUp() {
        // 검색어0 - 20
        // 검색어1 - 19
        // 검색어2 - 18
        // ...
        // 검색어18 - 2
        // 검색어19 - 1
        for (int i = 0; i < 20; i++) {
            for (int j = i; j < 20; j++) {
                popularSearchService.saveAndAddCount(query + i);
            }
        }
    }

    @Test
    void 검색어_저장() {
        //given
        String query = "검색어";
        //when
        popularSearchService.saveAndAddCount(query);
        //then
        Optional<PopularSearch> popularSearchOptional = popularSearchRepository.findByQuery(query);

        assertThat(popularSearchOptional.isPresent()).isTrue();
        assertThat(popularSearchOptional.get().getCount()).isEqualTo(1);
        assertThat(popularSearchOptional.get().getQuery()).isEqualTo(query);
    }

    @Test
    void 인기_검색어_10개_조회() {
        //given
        //when
        List<PopularSearchResponse> top10 = popularSearchService.getPopularSearchResponse();
        //then
        assertThat(top10.size()).isEqualTo(10);
        assertThat(top10.get(0).query()).isEqualTo(query + 0);
    }
}