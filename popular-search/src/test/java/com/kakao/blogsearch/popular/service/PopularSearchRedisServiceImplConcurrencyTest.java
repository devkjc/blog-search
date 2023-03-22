package com.kakao.blogsearch.popular.service;

import com.kakao.blogsearch.popular.dto.PopularSearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PopularSearchRedisServiceImplConcurrencyTest extends RedisTest {

    @Autowired
    private PopularSearchRedisServiceImpl popularSearchService;

    private final int numberOfThreads = 100;
    private final int incrementCountPerThread = 10;

    @Test
    public void testSaveAndAddCountConcurrency() throws InterruptedException {
        String query = "test";
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger count = new AtomicInteger();

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < incrementCountPerThread; j++) {
                    popularSearchService.saveAndAddCount(query);
                    count.getAndIncrement();
                }
                latch.countDown();
            });
        }

        boolean await = latch.await(3, TimeUnit.SECONDS);
        executorService.shutdown();

        List<PopularSearchResponse> top10PopularSearchResponse = popularSearchService.getTop10PopularSearchResponse();
        long totalCount = top10PopularSearchResponse.stream()
                .filter(response -> response.query().equals(query))
                .mapToLong(PopularSearchResponse::count)
                .findFirst()
                .orElse(0L);

        assertEquals(numberOfThreads * incrementCountPerThread, totalCount);
    }
}