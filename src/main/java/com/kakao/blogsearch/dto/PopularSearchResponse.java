package com.kakao.blogsearch.dto;

import com.kakao.blogsearch.domain.PopularSearch;
import lombok.Builder;

@Builder
public record PopularSearchResponse(
        String query,
        long count
) {

    public static PopularSearchResponse of(PopularSearch popularSearch) {
        return PopularSearchResponse.builder()
                .query(popularSearch.getQuery())
                .count(popularSearch.getCount())
                .build();
    }
}