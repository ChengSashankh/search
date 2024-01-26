package fyi.deeno.api.model;

import lombok.Data;

@Data
public class PositionalPostingHit {
    final String docTitles;
    final String docIds;

    public PositionalPostingHit(String docTitles, String docIds) {
        this.docTitles = docTitles;
        this.docIds = docIds;
    }
}
