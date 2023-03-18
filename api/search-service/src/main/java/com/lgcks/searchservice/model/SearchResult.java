package com.lgcks.searchservice.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = {"summary", "href", "score"})
@Data
public class SearchResult {

    private long id;
    private String summary;
    private String href;
    private long score;

}
