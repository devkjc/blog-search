package com.kakao.blogsearch.popular.service;

import com.kakao.blogsearch.popular.domain.PopularSearch;
import com.kakao.blogsearch.popular.dto.PopularSearchResponse;
import com.kakao.blogsearch.popular.repository.PopularSearchRepository;
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
class PopularSearchServiceImplTest {

    @Autowired
    private PopularSearchServiceImpl popularSearchServiceImpl;

    @Autowired
    private PopularSearchRepository popularSearchRepository;

    String query = "검색어";
    int max_size = 20;

    @BeforeAll
    void setUp() {

        popularSearchRepository.deleteAll();

        // 검색어0 - 20
        // 검색어1 - 19
        // 검색어2 - 18
        // ...
        // 검색어18 - 2
        // 검색어19 - 1
        for (int i = 0; i < max_size; i++) {
            for (int j = i; j < max_size; j++) {
                popularSearchServiceImpl.saveAndAddCount(query + i);
            }
        }
    }

    @Test
    void 검색어_저장() {
        //given
        String query = "검색어";
        //when
        popularSearchServiceImpl.saveAndAddCount(query);
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
        List<PopularSearchResponse> top10 = popularSearchServiceImpl.getTop10PopularSearchResponse();
        //then
        assertThat(top10.size()).isEqualTo(10);
        assertThat(top10.get(0).query()).isEqualTo(query + 0);
        assertThat(top10.get(0).count()).isEqualTo(max_size);
    }
}