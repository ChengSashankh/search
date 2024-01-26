package fyi.deeno.api.db;

import fyi.deeno.api.model.PositionalPostingHit;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPooled;

import java.util.*;

@Component
public class RedisPool {
    public static JedisPooled jedis = new JedisPooled("deeno-app.redis.cache.windows.net", 6380, "", "WoR7W9JeJJCzsfwgya5LqM1Se3l8CiOR3AzCaNDLBiA=");

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
