package fyi.deeno.searchservice.model;

import lombok.Data;

import java.lang.reflect.Array;
import java.util.List;

@Data
public class PositionalPostingHit {
    final String docTitles;
    final String docIds;

    public PositionalPostingHit(String docTitles, String docIds) {
        this.docTitles = docTitles;
        this.docIds = docIds;
    }
}
