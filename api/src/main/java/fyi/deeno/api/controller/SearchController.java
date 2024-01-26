package fyi.deeno.api.controller;

import fyi.deeno.api.model.SearchResults;
import fyi.deeno.api.service.RedisSearchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "*")
public class SearchController {

    private final RedisSearchService redisSearchService;

    public SearchController(RedisSearchService redisSearchService) {
        this.redisSearchService = redisSearchService;
    }

    @GetMapping
    public SearchResults getPostings(@RequestParam("q") String query) {
        // TODO: What if the comma is in the query string
        return new SearchResults(redisSearchService.get(query.split(",")));
    }
}
