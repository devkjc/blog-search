package com.kakao.blogsearch.service;

import com.kakao.blogsearch.dto.BlogSearchRequest;
import com.kakao.blogsearch.dto.BlogSearchResponse;
import com.kakao.blogsearch.search.SearchSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

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
        SearchSource kakao = SearchSource.KAKAO;
        Page<BlogSearchResponse> pageBlogResponse = blogSearchService.getBlogSearchResponses(kakao, searchRequest);
        assertThat(pageBlogResponse.isEmpty()).isFalse();
    }

    @Test
    void 네이버_블로그_검색() {
        SearchSource naver = SearchSource.NAVER;
        Page<BlogSearchResponse> pageBlogResponse = blogSearchService.getBlogSearchResponses(naver, searchRequest);
        assertThat(pageBlogResponse.isEmpty()).isFalse();
    }
}

