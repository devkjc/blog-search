package com.kakao.blogsearch.client.service;

import com.kakao.blogsearch.client.dto.BlogSearchRequest;
import com.kakao.blogsearch.client.dto.BlogSearchResponse;
import com.kakao.blogsearch.client.search.SearchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BlogSearchService {

    public Mono<ResponseEntity<Page<BlogSearchResponse>>> getBlogSearchResponses(SearchEngine engine, BlogSearchRequest searchRequest) {
        return blogApiCall(engine, searchRequest)
                .onErrorResume(e -> fallbackToOtherSearchEngine(engine, searchRequest));
    }

    private Mono<ResponseEntity<Page<BlogSearchResponse>>> fallbackToOtherSearchEngine(SearchEngine engine, BlogSearchRequest searchRequest) {
        return Flux.fromIterable(SearchEngine.otherSearchEngines(engine))
                .concatMap(otherSearchEngine -> blogApiCall(otherSearchEngine, searchRequest))
                .filter(responseEntity -> responseEntity.getStatusCode().is2xxSuccessful())
                .take(1)
                .next();
    }

    private Mono<ResponseEntity<Page<BlogSearchResponse>>> blogApiCall(SearchEngine engine, BlogSearchRequest searchRequest) {
        return engine.getWebClient().get()
                .uri(uriBuilder -> uriBuilder.queryParams(engine.getUriBuilder(searchRequest)).build())
                .retrieve()
                .toEntity(engine.getResponseClass())
                .map(responseEntity -> {
                    Page<BlogSearchResponse> responsePage = engine.mapToBlogSearchResponses(responseEntity, searchRequest.getPageable());
                    return new ResponseEntity<>(responsePage, responseEntity.getStatusCode());
                });
    }
}
