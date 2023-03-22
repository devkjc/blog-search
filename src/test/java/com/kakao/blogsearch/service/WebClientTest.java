package com.kakao.blogsearch.service;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class WebClientTest {

    public static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void webClient_exception() {

        mockWebServer.enqueue(new MockResponse().setResponseCode(503));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        ResponseEntity<?> responseEntity = null;

        HttpUrl url = mockWebServer.url("/hello");
        try {
            responseEntity = apiCall(url);
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (WebClientResponseException.InternalServerError |
                 WebClientResponseException.BadGateway |
                 WebClientResponseException.ServiceUnavailable e) {
            responseEntity = apiCall(mockWebServer.url("/hello2"));
        }

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Nullable
    private static ResponseEntity<String> apiCall(HttpUrl url) {
        return WebClient.builder().build().get().uri(url.uri()).retrieve().toEntity(String.class).block();
    }
}
