package com.kakao.blogsearch.client.dto;

import com.kakao.blogsearch.client.search.SearchEngine;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Builder
public record BlogSearchRequest(
        String query,
        BlogSearchSort sort,
        int page,
        int size
) {

    @Getter
    @RequiredArgsConstructor
    public enum BlogSearchSort {
        ACCURACY("sim"),
        RECENCY("date"),
        ;

        private final String naverSort;
    }

    public String sort(SearchEngine source) {
        return source.equals(SearchEngine.KAKAO) ? sort.name() : sort.getNaverSort();
    }

    public BlogSearchRequest {
        Objects.requireNonNull(query);
        if(sort == null) sort = BlogSearchSort.ACCURACY;
        if(page <= 0 || page > 50) page = 1;
        if(size <= 0 || size > 50) size = 10;
    }

    public Pageable convertPageable() {
        return PageRequest.of(page - 1, size, Sort.by(sort.toString()));
    }
}
