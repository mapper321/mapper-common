package com.mapper.core.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */
@Component
public class RedisUtil {

    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate1;

    private static RedisTemplate redisTemplate;

    private RedisUtil(){};

    @PostConstruct
    public void init() {
        this.redisTemplate = redisTemplate1;
    }


    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public static boolean set(final String key, Object value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean setStringResult(final String key, String value) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    public static boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    public static boolean expire(final String key, String value, long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 写入缓存设置时效时间
     * @param key
     * @return
     */
    public static boolean set(final String key, Long expireTime) {
        boolean result = false;
        try {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 批量删除对应的value
     * @param keys
     */
    public static void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     * @param pattern
     */
    public static void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys("*"+pattern+"*");
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }
    /**
     * 删除对应的value
     * @param key
     */
    public static void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public static boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    public static Object get(final String key) {
        Object result = null;
        result = redisTemplate.opsForValue().get(key);
        return result;
    }

    public static String getStringResult(final String key) {
        String result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        if(null != operations.get(key)){
            result = operations.get(key).toString();
        }
        return result;
    }

    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    public static void hmSet(String key, Object hashKey, Object value){
        HashOperations<String, Object, Object>  hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }

    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    public static Object hmGet(String key, Object hashKey){
        HashOperations<String, Object, Object>  hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }

    /**
     * 列表添加
     * @param k
     * @param v
     */
    public static void lPush(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }

    /**
     * 列表获取
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public static List<Object> lRange(String k, long l, long l1){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,l,l1);
    }

    /**
     * 集合添加
     * @param key
     * @param value
     */
    public static void add(String key,Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    public static void add(String key, String value, long expireTime) {
        try {
            add(key,value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 集合获取
     * @param key
     * @return
     */
    public static Set<Object> setMembers(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param scoure
     */
    public static void zAdd(String key,Object value,double scoure){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,scoure);
    }

    /**
     * 有序集合获取 通过分数区间查询
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public static Set<Object> rangeByScore(String key,double scoure,double scoure1){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    /**
     * 【描 述】：索引区间返回有序集合指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列
     *
     * @param key
     * @param start
     * @param end
     * @return
     * @since Mar 8, 2019
     */
    public static Set<Object> zRange(String key, Long start, Long end){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.range(key, start, end);
    }



    public static String getString(String string) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(string).toString();
    }

    /**
     * 设置某个key的有效期为永久
     * @param key
     * @return
     * @author mapper
     * @since  2019-05-10
     */
    public static boolean persist(String key) {
        return redisTemplate.persist(key);
    }
}
