package com.kakao.blogsearch.popular.dto;

import com.kakao.blogsearch.popular.domain.PopularSearch;
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

    public static PopularSearchResponse of(String query, long count) {
        return PopularSearchResponse.builder()
                .query(query)
                .count(count)
                .build();
    }
}
