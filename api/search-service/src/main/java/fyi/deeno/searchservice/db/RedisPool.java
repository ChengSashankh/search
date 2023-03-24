package fyi.deeno.searchservice.db;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPooled;

@Component
public class RedisPool {
    public static JedisPooled jedis = new JedisPooled("localhost", 6379);

    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public String get(String key) {
        return jedis.get(key);
    }
}
