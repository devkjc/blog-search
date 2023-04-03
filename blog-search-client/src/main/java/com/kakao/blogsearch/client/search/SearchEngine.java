package com.kakao.blogsearch.client.search;

import com.kakao.blogsearch.client.dto.BlogSearchRequest;
import com.kakao.blogsearch.client.dto.BlogSearchResponse;
import com.kakao.blogsearch.client.dto.KakaoBlogSearchResponse;
import com.kakao.blogsearch.client.dto.NaverBlogSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SearchEngine {

    KAKAO("https://dapi.kakao.com/v2/search/blog", KakaoBlogSearchResponse.class, getUriBuilderKakao(), mapToBlogSearchResponsesKakao()),
    NAVER("https://openapi.naver.com/v1/search/blog", NaverBlogSearchResponse.class, getUriBuilderNaver(), mapToBlogSearchResponsesNaver()),
    ;

    private final String baseUrl;
    private WebClient webClient;
    private final Class<?> responseClass;
    private final Function<BlogSearchRequest, MultiValueMap<String, String>> uriBuilder;
    private final BiFunction<ResponseEntity<?>, Pageable, Page<BlogSearchResponse>> responseBodyMapper;

    SearchEngine(String baseUrl,
                 Class<?> responseClass, Function<BlogSearchRequest, MultiValueMap<String, String>> uriBuilder,
                 BiFunction<ResponseEntity<?>, Pageable, Page<BlogSearchResponse>> responseBodyMapper
    ) {
        this.baseUrl = baseUrl;
        this.responseClass = responseClass;
        this.uriBuilder = uriBuilder;
        this.responseBodyMapper = responseBodyMapper;
    }

    public static List<SearchEngine> otherSearchEngines(SearchEngine searchEngine) {
        return Arrays.stream(SearchEngine.values()).filter(otherEngine -> !otherEngine.equals(searchEngine)).toList();
    }

    public MultiValueMap<String, String> getUriBuilder(BlogSearchRequest searchRequest) {
        return uriBuilder.apply(searchRequest);
    }

    private static Function<BlogSearchRequest, MultiValueMap<String, String>> getUriBuilderKakao() {
        return searchRequest -> {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.put("query", Collections.singletonList(searchRequest.query()));
            map.put("page", Collections.singletonList(String.valueOf(searchRequest.page())));
            map.put("size", Collections.singletonList(String.valueOf(searchRequest.size())));
            map.put("sort", Collections.singletonList(String.valueOf(searchRequest.sort(KAKAO))));
            return map;
        };
    }

    private static Function<BlogSearchRequest, MultiValueMap<String, String>> getUriBuilderNaver() {
        return searchRequest -> {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.put("query", Collections.singletonList(searchRequest.query()));
            map.put("start", Collections.singletonList(String.valueOf(searchRequest.page())));
            map.put("display", Collections.singletonList(String.valueOf(searchRequest.size())));
            map.put("sort", Collections.singletonList(String.valueOf(searchRequest.sort(NAVER))));
            return map;
        };
    }

    public Page<BlogSearchResponse> mapToBlogSearchResponses(ResponseEntity<?> response, Pageable pageable) {
        return responseBodyMapper.apply(response, pageable);
    }

    //TODO 좀 더 객체지향적으로 인간의 실수로 일어나는 실수가 없게 만들 수는 없을까?
    private static BiFunction<ResponseEntity<?>, Pageable, Page<BlogSearchResponse>> mapToBlogSearchResponsesKakao() {
        return (responseEntity, pageable) -> {
            KakaoBlogSearchResponse searchResponse = (KakaoBlogSearchResponse) responseEntity.getBody();
            List<BlogSearchResponse> documents = searchResponse.documents();
            return new PageImpl<>(documents, pageable, searchResponse.meta().total_count());
        };
    }

    private static BiFunction<ResponseEntity<?>, Pageable, Page<BlogSearchResponse>> mapToBlogSearchResponsesNaver() {
        return (responseEntity, pageable) -> {
            NaverBlogSearchResponse searchResponse = (NaverBlogSearchResponse) responseEntity.getBody();
            List<BlogSearchResponse> documents = searchResponse.items().stream().map(BlogSearchResponse::of).collect(Collectors.toList());
            return new PageImpl<>(documents, pageable, searchResponse.total());
        };
    }

    void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public Class<?> getResponseClass() {
        return responseClass;
    }
}