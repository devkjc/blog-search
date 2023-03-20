package com.kakao.blogsearch.controller;

import com.kakao.blogsearch.dto.BlogSearchRequest;
import com.kakao.blogsearch.dto.BlogSearchResponse;
import com.kakao.blogsearch.search.SearchSource;
import com.kakao.blogsearch.service.BlogSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
@Api(tags = {"블로그 검색"})
public class BlogSearchController {

    private final BlogSearchService blogSearchService;

    @GetMapping("/{source}/blog")
    @ApiOperation(value = "")
    public ResponseEntity<Page<BlogSearchResponse>> searchBlog(@RequestParam @NotBlank String query,
                                                               @RequestParam BlogSearchRequest.BlogSearchSort sort,
                                                               @RequestParam @Positive @Max(value = 50) int page,
                                                               @RequestParam @Positive @Max(value = 50) int size,
                                                               @PathVariable SearchSource source) {
        BlogSearchRequest blogSearchRequest = new BlogSearchRequest(query, sort, page, size);
        return ResponseEntity.ok(blogSearchService.getBlogSearchResponses(source, blogSearchRequest));
    }


}
