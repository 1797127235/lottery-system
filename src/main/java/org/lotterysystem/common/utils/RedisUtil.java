package org.lotterysystem.common.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    /*
        RedisTemplate: 先将存储的数据转换成字节数组  不可读
        StringRedisTemplate: 直接存放的就是String 可读
     */

    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public boolean set(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key,value);
            return true;
        }  catch (Exception e) {
            log.error("redisutil error",e);
            return false;
        }
    }

    public String get(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        }catch (Exception e) {
            log.error("redisutil error",e);
            return null;
        }
    }

    public boolean set(String key, String value, Long expireTime) {
        try {
            stringRedisTemplate.opsForValue().set(key,value,expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("redisutil error",e);
            return false;
        }
    }

    public boolean delete(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    stringRedisTemplate.delete(key[0]);
                    return true;
                } else {
                    stringRedisTemplate.delete(Arrays.asList(key));
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("redisutil error",e);
            return false;
        }
        return true;
    }

    public boolean exists(String key) {
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("redisutil error",e);
            return false;
        }
    }
 }

