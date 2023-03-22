package com.kakao.blogsearch.client.dto;

import java.util.List;

public record KakaoBlogSearchResponse(
        List<BlogSearchResponse> documents,
        MetaResponse meta
) {

    public record MetaResponse(int total_count, int pageable_count, boolean is_end){}

}
