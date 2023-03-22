package com.kakao.blogsearch.client.search;

import com.kakao.blogsearch.client.dto.BlogSearchRequest;
import com.kakao.blogsearch.client.dto.KakaoBlogSearchResponse;
import com.kakao.blogsearch.client.dto.NaverBlogSearchResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public enum SearchEngine {

    KAKAO("https://dapi.kakao.com/v2/search/blog", getUriBuilderKakao()),
    NAVER("https://openapi.naver.com/v1/search/blog", getUriBuilderNaver()),
    ;

    private final String baseUrl;
    private WebClient webClient;
    private final Function<BlogSearchRequest, MultiValueMap<String, String>> uriBuilder;

    SearchEngine(String baseUrl, Function<BlogSearchRequest, MultiValueMap<String, String>> uriBuilder) {
        this.baseUrl = baseUrl;
        this.uriBuilder = uriBuilder;
    }

    public static List<SearchEngine> otherSearchEngines(SearchEngine searchEngine) {
        return Arrays.stream(SearchEngine.values()).filter(otherEngine -> !otherEngine.equals(searchEngine)).toList();
    }

    public Class<?> getResponseClass() {
        return this.equals(KAKAO) ? KakaoBlogSearchResponse.class : NaverBlogSearchResponse.class;
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

    private void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Component
    @RequiredArgsConstructor
    public static class WebClientInjector {

        private final ApiKeyProperty apiKeyProperty;

        @PostConstruct
        public void inject() {

            Map<SearchEngine, Map<String, List<String>>> keys = apiKeyProperty.getHeaders();

            Arrays.stream(SearchEngine.values()).forEach(searchEngine -> {

                Map<String, List<String>> keyMap = keys.get(searchEngine);

                searchEngine.setWebClient(WebClient.builder()
                        .baseUrl(searchEngine.getBaseUrl())
                        .defaultHeaders(httpHeaders -> {
                            httpHeaders.putAll(keyMap);
                        }).build());
            });
        }
    }
}
