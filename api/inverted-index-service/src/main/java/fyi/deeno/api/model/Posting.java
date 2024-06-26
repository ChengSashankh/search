package fyi.deeno.api.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Posting {
    public String title;
    public String summary;
    public String[] matchingSubstring;
    public String href;

    public Posting(String id) {
        title = id;
        summary = id;
        matchingSubstring = new String[0];
        href = String.format("https://en.wikipedia.org/?curid=%s", id);
    }
}
