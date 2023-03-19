package com.kakao.blogsearch.controller;

import com.kakao.blogsearch.dto.BlogSearchResponse;
import com.kakao.blogsearch.dto.KakaoBlogSearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search/blog")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
@Api(tags = {"블로그 검색"})
public class BlogSearchController {

    @GetMapping
    @ApiOperation(value = "")
    public ResponseEntity<Page<BlogSearchResponse>> searchBlog() {
        return ResponseEntity.ok().build();
    }


}
