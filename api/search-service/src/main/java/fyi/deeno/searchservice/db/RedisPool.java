package fyi.deeno.searchservice.db;

import fyi.deeno.searchservice.model.PositionalPostingHit;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPooled;

import java.util.*;

@Component
public class RedisPool {
    public static JedisPooled jedis = new JedisPooled("localhost", 6379);

    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public List<String> get(String key) {
        String value = jedis.get(key);
        if (value == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(value.split(","));
    }

    public String getById(long wordId) {
        String compoundKey = String.format("%s:%d", "vocab", wordId);
        return jedis.hget(compoundKey, "word");
    }

    public String getByWord(String word) {
        String compoundKey = String.format("%s:%s", "word2id", word);
        return jedis.hget(compoundKey, "id");
    }

    public PositionalPostingHit getFromPosIdx(String wordId) {
        String compoundKey = String.format("%s:%s", "posIdx", wordId);
        String docTitles = jedis.hget(compoundKey, "docTitles");
        String docIds = jedis.hget(compoundKey, "docIds");
        return new PositionalPostingHit(docTitles, docIds);
    }
}
