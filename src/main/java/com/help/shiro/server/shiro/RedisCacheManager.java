package com.help.shiro.server.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author created by BangZhuLi
 * @date 2018/1/30  11:27
 */
@Slf4j
@Service
public class RedisCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
            log.info("获取名称为: " + name + " 的RedisCache实例");
            Cache c = caches.get(name);
            if (c == null) {
                c = new RedisCache<K, V>(redisTemplate);
                caches.put(name, c);
            }
            return c;
    }
}
