package com.kakao.blogsearch.client.dto;

import java.util.Date;
import java.util.List;

public record NaverBlogSearchResponse(
        List<NaverDocumentResponse> items,
        int total,
        int start,
        int display
) {

    public record NaverDocumentResponse(
        String title,
        String link,
        String description,
        String bloggername,
        Date postdate) {}
}
