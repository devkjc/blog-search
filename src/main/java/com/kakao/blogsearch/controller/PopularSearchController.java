package com.kakao.blogsearch.controller;

import com.kakao.blogsearch.dto.BlogSearchRequest;
import com.kakao.blogsearch.dto.BlogSearchResponse;
import com.kakao.blogsearch.dto.PopularSearchResponse;
import com.kakao.blogsearch.search.SearchEngine;
import com.kakao.blogsearch.service.BlogSearchService;
import com.kakao.blogsearch.service.PopularSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
@Api(tags = {"인기 검색어"})
public class PopularSearchController {

    private final PopularSearchService popularSearchService;

    @GetMapping("/popular")
    @ApiOperation(value = "인기 검색어 목록 조회")
    public ResponseEntity<List<PopularSearchResponse>> getPopularSearchList() {
        return ResponseEntity.ok(popularSearchService.getPopularSearchResponse());
    }
}
