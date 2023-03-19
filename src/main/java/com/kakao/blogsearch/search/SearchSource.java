package com.kakao.blogsearch.search;

import com.kakao.blogsearch.dto.BlogSearchRequest;
import com.kakao.blogsearch.dto.KakaoBlogSearchResponse;
import com.kakao.blogsearch.dto.NaverBlogSearchResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public enum SearchSource {

    KAKAO("dapi.kakao.com/v2/search/blog", getUriBuilderKakao()),
    NAVER("https://openapi.naver.com/v1/search/blog.json", getUriBuilderNaver()),
    ;

    private final String url;
    private WebClient webClient;
    private final Function<BlogSearchRequest, MultiValueMap<String, String>> uriBuilder;

    SearchSource(String url, Function<BlogSearchRequest, MultiValueMap<String, String>> uriBuilder) {
        this.url = url;
        this.uriBuilder = uriBuilder;
    }

    public Class<?> getResponseClass() {
        return this.equals(KAKAO) ? KakaoBlogSearchResponse.class : NaverBlogSearchResponse.class;
    }

    private static Function<BlogSearchRequest, MultiValueMap<String, String>> getUriBuilderKakao() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        return searchRequest -> {
            map.put("query", Collections.singletonList(searchRequest.query()));
            map.put("page", Collections.singletonList(String.valueOf(searchRequest.page())));
            map.put("size", Collections.singletonList(String.valueOf(searchRequest.size())));
            map.put("sort", Collections.singletonList(String.valueOf(searchRequest.sort(KAKAO))));
            return map;
        };
    }

    private static Function<BlogSearchRequest, MultiValueMap<String, String>> getUriBuilderNaver() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        return searchRequest -> {
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

            Map<SearchSource, Map<String, List<String>>> keys = apiKeyProperty.getKeys();

            for (SearchSource value : SearchSource.values()) {

                Map<String, List<String>> keyMap = keys.get(value);

                WebClient webClient =
                        WebClient.builder()
                                .baseUrl(value.getUrl())
                                .defaultHeaders(httpHeaders -> {
                                    httpHeaders.putAll(keyMap);
                                }).build();
                value.setWebClient(webClient);
            }
        }
    }
}
