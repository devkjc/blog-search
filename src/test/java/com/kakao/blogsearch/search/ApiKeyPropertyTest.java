package com.kakao.blogsearch.search;

import java.util.List;
import java.util.Map;

import com.kakao.blogsearch.search.ApiKeyProperty;
import com.kakao.blogsearch.search.SearchSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApiKeyPropertyTest {

    @Autowired
    private ApiKeyProperty apiKeyProperty;

    @Test
    void configurationPropertyTest() {
        Map<SearchSource, Map<String, List<String>>> keys = apiKeyProperty.getKeys();
        assertThat(keys.isEmpty()).isFalse();
    }
}