package com.kakao.blogsearch.popular.repository;

import com.kakao.blogsearch.popular.domain.PopularSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PopularSearchRepository extends JpaRepository<PopularSearch, Long> {

    Optional<PopularSearch> findByQuery(String query);

    List<PopularSearch> findTop10ByOrderByCountDescIdDesc();
}
