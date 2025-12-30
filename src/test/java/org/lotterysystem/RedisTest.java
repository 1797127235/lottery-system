package org.lotterysystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void pingRedis() {
        String key = "redis:connect:test";
        String value = "ok";
        stringRedisTemplate.opsForValue().set(key, value);
        String actual = stringRedisTemplate.opsForValue().get(key);
        Assertions.assertEquals(value, actual, "Redis 读写校验失败");
    }
    @Test
    void redisTest() {
        String key1 = "redis:list:key1";
        String key2 = "redis:list:key2";

        // 清理旧值，避免 WRONGTYPE（之前可能是 string/value 类型）
        stringRedisTemplate.delete(key1);
        stringRedisTemplate.delete(key2);

        stringRedisTemplate.opsForList().leftPush(key1, "value1");
        stringRedisTemplate.opsForList().leftPush(key2, "value2");

        String v1 = stringRedisTemplate.opsForList().index(key1, 0);
        String v2 = stringRedisTemplate.opsForList().index(key2, 0);

        Assertions.assertEquals("value1", v1, "列表读取失败");
        Assertions.assertEquals("value2", v2, "列表读取失败");

    }
}
