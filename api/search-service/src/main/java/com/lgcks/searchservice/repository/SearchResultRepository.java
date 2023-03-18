package com.lgcks.searchservice.repository;

import com.lgcks.searchservice.model.SearchResult;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface SearchResultRepository extends R2dbcRepository<SearchResult, Long> {

    Flux<SearchResult> findAllBySummaryLike(String pattern);
}
