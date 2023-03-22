package com.kakao.blogsearch.popular.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${spring.redis.popular-key}")
    private String POPULAR_SEARCH_KEY;

    @AfterEach
    void tearDown() {
        redisTemplate.delete(POPULAR_SEARCH_KEY);
    }

}
