package com.mapper.core.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.jms.ConnectionFactory;
import java.nio.charset.Charset;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisPoolFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisPoolFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new RedisObjectSerializer<Object>());
        return template;
    }

    /**
     * 使用fastjson序列化对象
     * @author jim
     *
     * @param <T>
     */
    static class RedisObjectSerializer<T> implements RedisSerializer<T> {

        public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
        private Class<T> clazz;

        static {
            ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        }

        public RedisObjectSerializer() {
        }

        public RedisObjectSerializer(Class<T> clazz) {
            super();
            this.clazz = clazz;
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length <= 0) {
                return null;
            }

            String str = new String(bytes, DEFAULT_CHARSET);
            if (clazz == null) {
                return (T) JSON.parse(str);
            }
            return (T) JSON.parseObject(str, clazz);
        }

        @Override
        public byte[] serialize(T t) throws SerializationException {
            if (t == null) {
                return new byte[0];
            }
            return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
        }

    }
}
