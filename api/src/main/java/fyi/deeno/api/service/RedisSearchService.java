package fyi.deeno.api.service;

import fyi.deeno.api.db.RedisPool;
import fyi.deeno.api.model.PositionalPostingHit;
import fyi.deeno.api.model.Posting;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedisSearchService {

    private final Environment env;

    private final RedisPool redisPool;
    public RedisSearchService(Environment env, RedisPool redisPool) throws IOException {
        this.env = env;
        this.redisPool = redisPool;
        //this.readIndex();
    }

    public List<Posting> get(String key) {
        return redisPool.get(key.toLowerCase()).stream()
                .map(id -> {
                    Posting posting = new Posting(id);
                    //posting.setTitle(pages.getOrDefault(id, "unknown"));
                    return posting;
                })
                .collect(Collectors.toList());
    }

    public List<PositionalPostingHit> get(String[] keys) {
        return Arrays.stream(keys)
                .map(String::toLowerCase)
                .map(redisPool::getByWord)
                .map(redisPool::getFromPosIdx)
//                .flatMap(Collection::stream)
//                .distinct()
//                .map(id -> {
//                    Posting posting = new Posting(id);
//                    //posting.setTitle(pages.getOrDefault(id, "unknown"));
//                    return posting;
//                })
                .collect(Collectors.toList());
    }

}
