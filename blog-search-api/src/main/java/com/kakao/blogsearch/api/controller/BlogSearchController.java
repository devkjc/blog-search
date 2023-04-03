package com.kakao.blogsearch.api.controller;

import com.kakao.blogsearch.client.dto.BlogSearchRequest;
import com.kakao.blogsearch.client.dto.BlogSearchResponse;
import com.kakao.blogsearch.client.search.SearchEngine;
import com.kakao.blogsearch.client.service.BlogSearchService;
import com.kakao.blogsearch.popular.service.PopularSearchRedisServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
@Api(tags = {"블로그 검색"})
public class BlogSearchController {

    private final BlogSearchService blogSearchService;
    private final PopularSearchRedisServiceImpl popularSearchService;

    //TODO 비동기 처리로 어느정도 변경을 했으나 swagger response에 값이 안보여 block()으로 임시 처리.
    @GetMapping("/{engine}/blog")
    @ApiOperation(value = "블로그 검색 엔진별 조회")
    public ResponseEntity<Page<BlogSearchResponse>> searchBlog(
            @RequestParam @NotBlank String query,
            @RequestParam(defaultValue = "ACCURACY", required = false) BlogSearchRequest.BlogSearchSort sort,
            @RequestParam(defaultValue = "1", required = false) @Positive @Max(value = 50) int page,
            @RequestParam(defaultValue = "10", required = false) @Positive @Max(value = 50) int size,
            @PathVariable SearchEngine engine) {
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest(query, sort, page, size);
        popularSearchService.saveAndAddCount(blogSearchRequest.query());
        return blogSearchService.getBlogSearchResponses(engine, blogSearchRequest).block();
    }

}
