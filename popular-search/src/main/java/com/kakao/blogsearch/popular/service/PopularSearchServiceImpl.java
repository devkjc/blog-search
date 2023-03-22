package com.kakao.blogsearch.popular.service;

import com.kakao.blogsearch.popular.domain.PopularSearch;
import com.kakao.blogsearch.popular.dto.PopularSearchResponse;
import com.kakao.blogsearch.popular.repository.PopularSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PopularSearchServiceImpl implements PopularSearchService {

    private final PopularSearchRepository popularSearchRepository;

    @Override
    public void saveAndAddCount(String query) {
        PopularSearch popularSearch = popularSearchRepository.findByQuery(query).orElseGet(() -> PopularSearch.builder().query(query).build());
        popularSearch.addCount();
        popularSearchRepository.save(popularSearch);
    }

    @Override
    public List<PopularSearchResponse> getTop10PopularSearchResponse() {
        return getTop10Search().stream().map(PopularSearchResponse::of).toList();
    }

    private List<PopularSearch> getTop10Search() {
        return popularSearchRepository.findTop10ByOrderByCountDescIdDesc();
    }

}
