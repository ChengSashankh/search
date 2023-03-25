package fyi.deeno.searchservice.db;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPooled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
}
