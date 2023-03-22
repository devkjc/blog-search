package com.kakao.blogsearch.controller;

import com.kakao.blogsearch.dto.PopularSearchResponse;
import com.kakao.blogsearch.service.PopularSearchRedisServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
@Api(tags = {"인기 검색어"})
public class PopularSearchController {

    private final PopularSearchRedisServiceImpl popularSearchService;

    @GetMapping("/popular")
    @ApiOperation(value = "인기 검색어 목록 조회")
    public ResponseEntity<List<PopularSearchResponse>> getTop10PopularSearchResponse() {
        return ResponseEntity.ok(popularSearchService.getTop10PopularSearchResponse());
    }
}
