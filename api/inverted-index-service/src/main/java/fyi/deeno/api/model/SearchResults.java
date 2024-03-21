package fyi.deeno.api.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchResults {
    final public List<PositionalPostingHit> results;
}
