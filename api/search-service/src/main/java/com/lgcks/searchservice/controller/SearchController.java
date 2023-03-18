package com.lgcks.searchservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/search")
public class SearchController {

    @GetMapping
    public Flux<String> getTrendingSearches() {
        return Flux.interval(Duration.of(2, ChronoUnit.SECONDS))
                .map(idx -> "Next item");
    }
}
