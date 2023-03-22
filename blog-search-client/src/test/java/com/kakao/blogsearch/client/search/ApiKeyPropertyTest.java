package com.kakao.blogsearch.client.search;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApiKeyPropertyTest {

    @Autowired
    private ApiKeyProperty apiKeyProperty;

    @Test
    void configurationPropertyTest() {
        Map<SearchEngine, Map<String, List<String>>> keys = apiKeyProperty.getHeaders();
        assertThat(keys.isEmpty()).isFalse();
    }
}