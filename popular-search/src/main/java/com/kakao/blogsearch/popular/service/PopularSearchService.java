package com.kakao.blogsearch.popular.service;

import com.kakao.blogsearch.popular.dto.PopularSearchResponse;

import java.util.List;

public interface PopularSearchService {

    void saveAndAddCount(String query);
    List<PopularSearchResponse> getTop10PopularSearchResponse();

}
