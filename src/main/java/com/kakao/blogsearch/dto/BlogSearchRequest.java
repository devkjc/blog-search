package com.kakao.blogsearch.dto;

import com.kakao.blogsearch.search.SearchSource;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Builder
public record BlogSearchRequest(
        @NotBlank String query,
        BlogSearchSort sort,
        @Positive @Max(value = 50) int page,
        @Positive @Max(value = 50) int size
) {

    @Getter
    @RequiredArgsConstructor
    public enum BlogSearchSort {
        ACCURACY("sim"),
        RECENCY("date"),
        ;

        private final String naverSort;
    }

    public String sort(SearchSource source) {
        return source.equals(SearchSource.KAKAO) ? sort.name() : sort.getNaverSort();
    }

    public BlogSearchRequest {
        Objects.requireNonNull(query);
        if(sort == null) sort = BlogSearchSort.ACCURACY;
        if(page == 0 || page > 50) page = 1;
        if(size == 0 || size > 50) size = 10;
    }

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size, Sort.by(sort.toString()));
    }
}
