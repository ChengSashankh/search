package fyi.deeno.api.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import fyi.deeno.api.model.Posting;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class InMemoryIndexService {

    @Value("classpath:phrases.txt")
    private Resource resource;

    @Value("${index.columns}")
    private String columnsToIndex;

    @Value("${index.id}")
    private String indexIdColumn;

    private final Multimap<String, Posting> index = ArrayListMultimap.create();

    private static final Logger log = Logger.getLogger(InMemoryIndexService.class.getName());

    @PostConstruct
    public void buildIndex() throws IOException {
        int idColumnIdx = Integer.parseInt(indexIdColumn);
        Integer[] targetColumns = (Integer[]) Arrays.stream(columnsToIndex.split(","))
                        .map(Integer::parseInt).toArray();

        log.info("Beginning inverted index building using resource file");
        String lines = resource.getContentAsString(Charset.defaultCharset());

        for (String line: lines.split("\n")) {
            line.split(",")
        }

        log.info("Trie build completed. Ready to serve requests");
    }



}
