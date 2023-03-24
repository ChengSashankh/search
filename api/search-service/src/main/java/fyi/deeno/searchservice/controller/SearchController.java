package fyi.deeno.searchservice.controller;

import fyi.deeno.searchservice.model.Posting;
import fyi.deeno.searchservice.model.SearchResults;
import fyi.deeno.searchservice.service.SimpleMapSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/search")
public class SearchController {

    private SimpleMapSearchService simpleMapSearchService;

    public SearchController(SimpleMapSearchService simpleMapSearchService) {
        this.simpleMapSearchService = simpleMapSearchService;
    }

    @GetMapping
    public SearchResults getPostings(@RequestParam("q") String query) {
        SearchResults searchResults = new SearchResults();
        searchResults.results = simpleMapSearchService.get(query);
        return searchResults;
    }
}
