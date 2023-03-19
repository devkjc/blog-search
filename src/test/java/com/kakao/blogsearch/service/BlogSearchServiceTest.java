package com.kakao.blogsearch.service;

import com.kakao.blogsearch.dto.BlogSearchRequest;
import com.kakao.blogsearch.dto.BlogSearchResponse;
import com.kakao.blogsearch.dto.KakaoBlogSearchResponse;
import com.kakao.blogsearch.dto.NaverBlogSearchResponse;
import com.kakao.blogsearch.search.SearchSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class BlogSearchServiceTest {

    BlogSearchRequest searchRequest =
        BlogSearchRequest.builder()
                .query("JAVA")
                .page(2)
                .build();

    @Test
    void 카카오_블로그_검색() {
        SearchSource kakao = SearchSource.KAKAO;
        Object response = callBlogAPI(searchRequest, kakao);
        Page<BlogSearchResponse> pageBlogResponse = getPageBlogResponse(response, searchRequest);
        assertThat(pageBlogResponse.isEmpty()).isFalse();
    }

    @Test
    void 네이버_블로그_검색() {
        SearchSource naver = SearchSource.NAVER;
        Object response = callBlogAPI(searchRequest, naver);
        Page<BlogSearchResponse> pageBlogResponse = getPageBlogResponse(response, searchRequest);
        assertThat(pageBlogResponse.isEmpty()).isFalse();
    }

    public Page<BlogSearchResponse> getPageBlogResponse(Object response, BlogSearchRequest searchRequest){

        Pageable pageable = searchRequest.getPageable();

        if (response instanceof KakaoBlogSearchResponse searchResponse) {
            List<BlogSearchResponse> documents = searchResponse.documents();
            return new PageImpl<>(documents, pageable, searchResponse.meta().total_count());
        }

        if (response instanceof NaverBlogSearchResponse searchResponse) {
            List<BlogSearchResponse> documents =
                    searchResponse.items().stream().map(BlogSearchResponse::of).collect(Collectors.toList());
            return new PageImpl<>(documents, pageable, searchResponse.total());
        }

        throw new IllegalArgumentException();
    }

    private Object callBlogAPI(BlogSearchRequest searchRequest, SearchSource source) {
        WebClient webClient = source.getWebClient();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParams(source.getUriBuilder().apply(searchRequest)).build())
                .retrieve()
                .bodyToMono(source.getResponseClass())
                .block();
    }
}

