package com.kakao.blogsearch.repository;

import com.kakao.blogsearch.domain.PopularSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PopularSearchRepository extends JpaRepository<PopularSearch, Long> {

    Optional<PopularSearch> findByQuery(String query);

    List<PopularSearch> findTop10ByOrderByCountDescIdDesc();
}
