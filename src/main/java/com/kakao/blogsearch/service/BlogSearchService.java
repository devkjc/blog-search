package com.kakao.blogsearch.service;

import com.kakao.blogsearch.domain.PopularSearch;
import com.kakao.blogsearch.dto.BlogSearchRequest;
import com.kakao.blogsearch.dto.BlogSearchResponse;
import com.kakao.blogsearch.dto.KakaoBlogSearchResponse;
import com.kakao.blogsearch.dto.NaverBlogSearchResponse;
import com.kakao.blogsearch.repository.PopularSearchRepository;
import com.kakao.blogsearch.search.SearchSource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogSearchService {

    private final PopularSearchRepository popularSearchRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public Page<BlogSearchResponse> getBlogSearchResponses(SearchSource source, BlogSearchRequest searchRequest) {
        String query = searchRequest.query();
        redisTemplate.opsForZSet().incrementScore("popular", query, 1);
        return apiResponseToPage(callBlogAPI(searchRequest, source), searchRequest);
    }

    private Page<BlogSearchResponse> apiResponseToPage(Object response, BlogSearchRequest searchRequest){

        Pageable pageable = searchRequest.convertPageable();

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
