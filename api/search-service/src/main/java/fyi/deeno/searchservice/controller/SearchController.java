package fyi.deeno.searchservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @GetMapping
    public List<String> getPostings() {
        return new ArrayList<>() {{
            add("item1");
            add("item2");
            add("item3");
        }};
    }
}
