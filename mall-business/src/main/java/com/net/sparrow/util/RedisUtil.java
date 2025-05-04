package com.net.sparrow.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Slf4j
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 保存缓存，同时设置过期时间
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value, long expireTime) {
        try {
            stringRedisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("Redis保存数据失败：", e);
            return false;
        }
    }

    /**
     * 保存缓存
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis保存数据失败：", e);
            return false;
        }
    }

    /**
     * 获取普通缓存
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return key == null ? null : stringRedisTemplate.opsForValue().get(key);
    }


    /**
     * 保存缓存
     *
     * @param key 键
     * @return true成功 false失败
     */
    public void del(String key) {
        try {
            if (StringUtils.hasLength(key)) {
                stringRedisTemplate.delete(key);
            }
        } catch (Exception e) {
            log.error("Redis删除数据失败：", e);
        }
    }

    /**
     * 批量保存hash结构数据
     *
     * @param key key
     * @param map map
     * @return 是否成功
     */
    public boolean putHashMap(String key, Map<Object, Object> map) {
        try {
            stringRedisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("Redis保存数据失败：", e);
            return false;
        }
    }

    /**
     * 保存hash结构数据
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   值
     * @return 是否成功
     */
    public boolean putHashValue(String key, Object hashKey, Object value) {
        try {
            stringRedisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error("Redis保存数据失败：", e);
            return false;
        }
    }

    /**
     * 获取hash结构数据
     *
     * @param key key
     * @param key hashKey
     * @return 值
     */
    public Object getHashValue(String key, Object hashKey) {
        return key == null || hashKey == null ? null : stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 保存缓存，如果key存在，则不做处理
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setIfAbsent(String key, String value) {
        try {
            return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            log.error("Redis保存数据失败：", e);
            return false;
        }
    }
}
