package com.kakao.blogsearch.client.service;

import com.kakao.blogsearch.client.dto.BlogSearchRequest;
import com.kakao.blogsearch.client.dto.BlogSearchResponse;
import com.kakao.blogsearch.client.search.SearchEngine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

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
        Mono<ResponseEntity<Page<BlogSearchResponse>>> blogSearchResponsesMono = blogSearchService.getBlogSearchResponses(kakao, searchRequest);
        ResponseEntity<Page<BlogSearchResponse>> responseEntity = blogSearchResponsesMono.block();
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 네이버_블로그_검색() {
        SearchEngine naver = SearchEngine.NAVER;
        Mono<ResponseEntity<Page<BlogSearchResponse>>> blogSearchResponsesMono = blogSearchService.getBlogSearchResponses(naver, searchRequest);
        ResponseEntity<Page<BlogSearchResponse>> responseEntity = blogSearchResponsesMono.block();
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}

