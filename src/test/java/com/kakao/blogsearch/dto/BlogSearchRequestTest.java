package com.kakao.blogsearch.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class BlogSearchRequestTest {

    String query = "카카오뱅크";

    @Test
    void SearchRequest_기본값_생성() {
        //given
        BlogSearchRequest request =
                BlogSearchRequest.builder()
                        .query(query)
                        .size(0)
                        .build();
        //when
        //then
        assertThat(request.page()).isEqualTo(1);
        assertThat(request.size()).isEqualTo(10);
        assertThat(request.query()).isEqualTo(query);
        assertThat(request.sort()).isEqualTo(BlogSearchRequest.BlogSearchSort.ACCURACY);
    }

    @Test
    void SearchRequest_size_page_최대_생성() {
        //given
        BlogSearchRequest request =
                BlogSearchRequest.builder()
                        .query(query)
                        .page(51)
                        .size(51)
                        .build();
        //when
        //then
        assertThat(request.page()).isEqualTo(1);
        assertThat(request.size()).isEqualTo(10);
        assertThat(request.query()).isEqualTo(query);
        assertThat(request.sort()).isEqualTo(BlogSearchRequest.BlogSearchSort.ACCURACY);
    }

    @Test
    void SearchRequest_생성_실패() {
        assertThatThrownBy(() -> BlogSearchRequest.builder().build()).isInstanceOf(NullPointerException.class);
    }
}