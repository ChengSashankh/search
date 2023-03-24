package fyi.deeno.searchservice.service;

import fyi.deeno.searchservice.model.Posting;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SimpleMapSearchService {

    private final Environment env;

    private final Map<String, String[]> index;
    private final Map<String, String> pages;

    public SimpleMapSearchService(Environment env) throws IOException {
        this.env = env;
        this.index = readIndex();
        this.pages = readPages();
    }

    private Map<String, String[]> readIndex() throws IOException {
        String pathToIndexFile = env.getProperty("index.file", "/Users/cksash/IdeaProjects/looking-glass/search/backend/extractor/src/main/resources/outputIndex/part-00000-6465bb1e-9260-4589-908e-35f7b0924fb2-c000.csv");
        List<String> lines = Files.readAllLines(new File(pathToIndexFile).toPath());
        return lines.stream()
                .collect(Collectors.toMap(
                        line -> line.split("\t")[0],
                        line -> line.split("\t")[1].replace("[", "").replace("]", "").split(",")
                ));
    }

    private Map<String, String> readPages() throws IOException {
        String pathToIndexFile = env.getProperty("pages.file", "/Users/cksash/IdeaProjects/looking-glass/search/backend/extractor/src/main/resources/pages/part-00000-69858536-01f3-471f-8fff-85b4e59999f2-c000.csv");
        List<String> lines = Files.readAllLines(new File(pathToIndexFile).toPath());
        return lines.stream()
                .collect(Collectors.toMap(
                        line -> line.split("\t")[0],
                        line -> line.split("\t")[1]
                ));
    }


    public List<Posting> get(String key) {
        return Arrays.stream(index.getOrDefault(key.toLowerCase(), new String[0]))
                .map(id -> {
                    Posting posting = new Posting(id);
                    posting.title = pages.getOrDefault(id, "Unknown");

                    return posting;
                })
                .collect(Collectors.toList());
    }

}
