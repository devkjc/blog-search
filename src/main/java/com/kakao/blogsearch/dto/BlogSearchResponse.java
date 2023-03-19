package com.kakao.blogsearch.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record BlogSearchResponse(String title, String contents, String url, String blogname, String thumbnail, Date datetime) {

    public static BlogSearchResponse of(NaverBlogSearchResponse.NaverDocumentResponse documentResponse) {
        return BlogSearchResponse.builder()
                .title(documentResponse.title())
                .contents(documentResponse.description())
                .url(documentResponse.link())
                .blogname(documentResponse.bloggername())
                .datetime(documentResponse.postdate())
                .build();
    }
}
