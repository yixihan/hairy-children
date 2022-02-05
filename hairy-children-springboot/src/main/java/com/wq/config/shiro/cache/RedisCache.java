package com.wq.config.shiro.cache;

import com.alibaba.fastjson.JSONObject;
import com.wq.util.SpringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collection;
import java.util.Set;

/**
 * 自定义 redis 缓存
 * @author : yixihan
 * @create : 2022-02-05-12:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class RedisCache<K, V> implements Cache<K, V> {

    private String cacheName;

    @Override
    public V get(K k) throws CacheException {
        String val = (String) getRedisTemplate().opsForHash().get(this.cacheName, k.toString());

        log.info (val);
        SimpleAuthenticationInfo info = JSONObject.parseObject(val, SimpleAuthenticationInfo.class);

        log.info ("" + info);

        return (V) val;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        System.out.println(k.getClass());
        System.out.println(v.getClass());

        String key = JSONObject.toJSONString(k);
        String value = JSONObject.toJSONString(v);

        log.info ("key : " + key);
        log.info ("value : " + value);

        getRedisTemplate().opsForHash().put(this.cacheName,key, value);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        return (V) getRedisTemplate().opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        getRedisTemplate().opsForHash().delete(this.cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<K> keys() {
        return (Set<K>) getRedisTemplate().opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<V> values() {
        return (Collection<V>) getRedisTemplate().opsForHash().values(this.cacheName);
    }

    /**
     * 获取 RedisTemplate
     * @return redisTemplate
     */
    private RedisTemplate<String, V> getRedisTemplate () {
        RedisTemplate<String, V> redisTemplate = SpringUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer ());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        return redisTemplate;

    }
}
