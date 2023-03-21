package com.kakao.blogsearch.service;

import com.kakao.blogsearch.dto.BlogSearchRequest;
import com.kakao.blogsearch.dto.BlogSearchResponse;
import com.kakao.blogsearch.search.SearchEngine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BlogSearchServiceTest {

    @Autowired
    private BlogSearchService blogSearchService;

    BlogSearchRequest searchRequest =
        BlogSearchRequest.builder()
                .query("JAVA")
                .page(2)
                .build();

    @Test
    void 카카오_블로그_검색() {
        SearchEngine kakao = SearchEngine.KAKAO;
        ResponseEntity<Page<BlogSearchResponse>> blogSearchResponses = blogSearchService.getBlogSearchResponses(kakao, searchRequest);
        assertThat(blogSearchResponses.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 네이버_블로그_검색() {
        SearchEngine naver = SearchEngine.NAVER;
        ResponseEntity<Page<BlogSearchResponse>> blogSearchResponses = blogSearchService.getBlogSearchResponses(naver, searchRequest);
        assertThat(blogSearchResponses.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}

