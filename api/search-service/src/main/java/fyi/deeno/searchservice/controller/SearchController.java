package fyi.deeno.searchservice.controller;

import fyi.deeno.searchservice.model.Posting;
import fyi.deeno.searchservice.model.SearchResults;
import fyi.deeno.searchservice.service.RedisSearchService;
import fyi.deeno.searchservice.service.SimpleMapSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "*")
public class SearchController {

    private RedisSearchService simpleMapSearchService;

    public SearchController(RedisSearchService simpleMapSearchService) {
        this.simpleMapSearchService = simpleMapSearchService;
    }

    @GetMapping
    public SearchResults getPostings(@RequestParam("q") String query) {
        SearchResults searchResults = new SearchResults();
        searchResults.results = simpleMapSearchService.get(query);
        return searchResults;
    }
}
