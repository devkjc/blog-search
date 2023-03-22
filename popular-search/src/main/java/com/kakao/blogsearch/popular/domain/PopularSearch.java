package com.kakao.blogsearch.popular.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "popular_search")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularSearch {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String query;

    @Column(nullable = false)
    private long count;

    public void addCount() {
        count++;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public PopularSearch(String query) {
        this.query = query;
    }
}
