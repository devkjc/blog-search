package com.kakao.blogsearch.search;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ConfigurationProperties("api")
public class ApiKeyProperty {

    private final Map<SearchSource,Map<String, List<String>>> keys;

    public Map<SearchSource,Map<String, List<String>>> getKeys() {
        return keys;
    }
}