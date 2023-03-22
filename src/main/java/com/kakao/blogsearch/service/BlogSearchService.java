package com.kakao.blogsearch.service;

import com.kakao.blogsearch.dto.BlogSearchRequest;
import com.kakao.blogsearch.dto.BlogSearchResponse;
import com.kakao.blogsearch.dto.KakaoBlogSearchResponse;
import com.kakao.blogsearch.dto.NaverBlogSearchResponse;
import com.kakao.blogsearch.search.SearchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogSearchService {

//    private final PopularSearchServiceImpl popularSearchServiceImpl;
    private final PopularSearchRedisServiceImpl popularSearchService;

    @Transactional
    public ResponseEntity<Page<BlogSearchResponse>> getBlogSearchResponses(SearchEngine engine, BlogSearchRequest searchRequest) {
        popularSearchService.saveAndAddCount(searchRequest.query());

        ResponseEntity<?> responseEntity = null;

        try {
            responseEntity = blogApiCall(engine, searchRequest);
        } catch (WebClientResponseException.InternalServerError |
                 WebClientResponseException.BadGateway |
                 WebClientResponseException.ServiceUnavailable e) {
            responseEntity = otherBlogApiCall(engine, searchRequest, responseEntity);
        }

        assert responseEntity != null;
        return apiResponseBodyMapPage(responseEntity, searchRequest);
    }

    private ResponseEntity<?> otherBlogApiCall(SearchEngine engine, BlogSearchRequest searchRequest, ResponseEntity<?> responseEntity) {
        for (SearchEngine otherSearchEngine : SearchEngine.otherSearchEngines(engine)) {
            responseEntity = blogApiCall(otherSearchEngine, searchRequest);
            if(responseEntity.getStatusCode().is2xxSuccessful()) break;
        }
        return responseEntity;
    }

    private ResponseEntity<?> blogApiCall(SearchEngine engine, BlogSearchRequest searchRequest) {
        WebClient webClient = engine.getWebClient();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParams(engine.getUriBuilder().apply(searchRequest)).build())
                .retrieve()
                .toEntity(engine.getResponseClass())
                .block();
    }

    private ResponseEntity<Page<BlogSearchResponse>> apiResponseBodyMapPage(ResponseEntity<?> response, BlogSearchRequest searchRequest) {

        Pageable pageable = searchRequest.convertPageable();
        Page<BlogSearchResponse> blogSearchResponses = null;

        if (response.getStatusCode().is2xxSuccessful()) {
            Object body = response.getBody();

            if (body instanceof KakaoBlogSearchResponse searchResponse) {
                List<BlogSearchResponse> documents = searchResponse.documents();
                blogSearchResponses = new PageImpl<>(documents, pageable, searchResponse.meta().total_count());
            }

            if (body instanceof NaverBlogSearchResponse searchResponse) {
                List<BlogSearchResponse> documents =
                        searchResponse.items().stream().map(BlogSearchResponse::of).collect(Collectors.toList());
                blogSearchResponses = new PageImpl<>(documents, pageable, searchResponse.total());
            }
        }

        return new ResponseEntity<>(
                blogSearchResponses,
                response.getHeaders(),
                response.getStatusCode());
    }
}
