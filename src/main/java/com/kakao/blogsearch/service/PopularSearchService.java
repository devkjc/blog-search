package com.kakao.blogsearch.service;

import com.kakao.blogsearch.dto.PopularSearchResponse;

import java.util.List;

public interface PopularSearchService {

    void saveAndAddCount(String query);
    List<PopularSearchResponse> getTop10PopularSearchResponse();

}
