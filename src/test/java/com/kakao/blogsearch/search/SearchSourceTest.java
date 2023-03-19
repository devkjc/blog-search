package com.kakao.blogsearch.search;

import com.kakao.blogsearch.dto.BlogSearchRequest;
import com.kakao.blogsearch.dto.KakaoBlogSearchResponse;
import com.kakao.blogsearch.dto.NaverBlogSearchResponse;
import com.kakao.blogsearch.search.SearchSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class SearchSourceTest {

    SearchSource kakao = SearchSource.KAKAO;
    SearchSource naver = SearchSource.NAVER;

    @Test
    void webClient_생성() {
        //given
        WebClient kakaoWebClient = kakao.getWebClient();
        WebClient naverWebClient = naver.getWebClient();
        //when
        //then
        assertThat(kakaoWebClient).isInstanceOf(WebClient.class);
        assertThat(naverWebClient).isInstanceOf(WebClient.class);
    }

    @Test
    void 요청_헤더_생성() {
        //given
        String query = "hello";
        BlogSearchRequest searchRequest =
                BlogSearchRequest.builder()
                        .query(query)
                        .sort(BlogSearchRequest.BlogSearchSort.ACCURACY)
                        .build();

        //when
        MultiValueMap<String, String> kakaoHeaderMap = kakao.getUriBuilder().apply(searchRequest);
        MultiValueMap<String, String> naverHeaderMap = naver.getUriBuilder().apply(searchRequest);
        //then
        assertThat(kakaoHeaderMap.get("query").contains(query)).isTrue();
        assertThat(kakaoHeaderMap.get("page").contains(String.valueOf(searchRequest.page()))).isTrue();
        assertThat(kakaoHeaderMap.get("size").contains(String.valueOf(searchRequest.size()))).isTrue();
        assertThat(kakaoHeaderMap.get("sort").contains(searchRequest.sort().name())).isTrue();

        assertThat(naverHeaderMap.get("query").contains(query)).isTrue();
        assertThat(naverHeaderMap.get("start").contains(String.valueOf(searchRequest.page()))).isTrue();
        assertThat(naverHeaderMap.get("display").contains(String.valueOf(searchRequest.size()))).isTrue();
        assertThat(naverHeaderMap.get("sort").contains(searchRequest.sort().getNaverSort())).isTrue();
    }

    @Test
    void Response_Class_매칭() {

        Class<?> kakaoResponseClass = kakao.getResponseClass();
        Class<?> naverResponseClass = naver.getResponseClass();

        assertThat(kakaoResponseClass).isEqualTo(KakaoBlogSearchResponse.class);
        assertThat(naverResponseClass).isEqualTo(NaverBlogSearchResponse.class);
    }
}