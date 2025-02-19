package com.lyh.util;

import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisConnectionException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DistributedLock {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public boolean acquireLock(String key, String uuid, int timeout) {
        try {
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, uuid, timeout, TimeUnit.SECONDS);
            return acquired != null && acquired;
        } catch (RedisConnectionException | RedisCommandTimeoutException | RedisConnectionFailureException | QueryTimeoutException |
                 RedisSystemException e) {
            log.error("获取锁方法acquireLock()异常：redis连接异常：{}", e.getMessage());
        }
       return false;
    }

    public void releaseLock(String key) {
        try {
            if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                return;
            }
            String currentValue = redisTemplate.opsForValue().get(key);
            if (currentValue == null || !currentValue.equals(redisTemplate.opsForValue().get(key))) {
                return;
            }
            redisTemplate.delete(key);
        } catch (RedisConnectionException | RedisCommandTimeoutException | RedisConnectionFailureException  | QueryTimeoutException |
                 RedisSystemException e) {
            log.error("释放锁方法releaseLock()异常：redis连接异常：{}", e.getMessage());
        }
    }
}