package com.kakao.blogsearch.client.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebClientInjector {

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
