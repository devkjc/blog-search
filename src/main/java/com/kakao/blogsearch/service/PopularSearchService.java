package com.kakao.blogsearch.service;

import com.kakao.blogsearch.domain.PopularSearch;
import com.kakao.blogsearch.dto.PopularSearchResponse;
import com.kakao.blogsearch.repository.PopularSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PopularSearchService {

    private final PopularSearchRepository popularSearchRepository;

    public void saveAndAddCount(String query) {
        PopularSearch popularSearch = popularSearchRepository.findByQuery(query).orElseGet(() -> PopularSearch.builder().query(query).build());
        popularSearch.addCount();
        popularSearchRepository.save(popularSearch);
    }

    public List<PopularSearchResponse> getPopularSearchResponse() {
        return getTop10Search().stream().map(PopularSearchResponse::of).toList();
    }

    private List<PopularSearch> getTop10Search() {
        return popularSearchRepository.findTop10ByOrderByCountDescIdDesc();
    }
}
