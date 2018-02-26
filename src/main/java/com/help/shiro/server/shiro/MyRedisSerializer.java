package com.help.shiro.server.shiro;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.SerializationUtils;

/**
 * @author created by BangZhuLi
 * @date 2018/1/30  17:53
 * 自定义实现序列化
 */
public class MyRedisSerializer<T> implements RedisSerializer<T> {
    @Override
    public byte[] serialize(T t) throws SerializationException {
        return SerializationUtils.serialize(t);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        T rs =  (T) SerializationUtils.deserialize(bytes);
        return rs;
    }
}
