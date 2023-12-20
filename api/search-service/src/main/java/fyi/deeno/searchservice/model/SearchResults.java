package fyi.deeno.searchservice.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchResults {
    final public List<PositionalPostingHit> results;
}
