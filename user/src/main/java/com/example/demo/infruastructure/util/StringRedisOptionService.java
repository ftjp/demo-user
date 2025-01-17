package com.example.demo.infruastructure.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class StringRedisOptionService {
    public static final ThreadLocal<Set<String>> lockThreadLocal = new ThreadLocal();
    private static final Logger log = LoggerFactory.getLogger(StringRedisOptionService.class);
    private StringRedisTemplate stringRedisTemplate;

    public StringRedisOptionService() {
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置键的序列化器
        template.setKeySerializer(new StringRedisSerializer());

        // 设置值的序列化器
        template.setValueSerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        this.stringRedisTemplate = template;
        return template;
    }

    public Boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                return this.stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception var5) {
            Exception e = var5;
            log.error("指定缓存过期时间异常", e);
        }

        return false;
    }

    public Long getExpire(String key) {
        return this.stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public Boolean hasKey(String key) {
        try {
            return this.stringRedisTemplate.hasKey(key);
        } catch (Exception var3) {
            Exception e = var3;
            log.error("判断key是否存在异常", e);
            return false;
        }
    }

    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                this.stringRedisTemplate.delete(key[0]);
            } else {
                Set occ = new HashSet(CollectionUtils.arrayToList(key));
                this.stringRedisTemplate.delete(occ);
            }
        }

    }

    public String get(String key) {
        return key == null ? null : this.stringRedisTemplate.opsForValue().get(key);
    }

    public List<String> multiGet(List<String> keyList) {
        ValueOperations<String, String> valueOperations = this.stringRedisTemplate.opsForValue();
        return valueOperations.multiGet(keyList);
    }

    public boolean set(String key, String value) {
        try {
            this.stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception var4) {
            Exception e = var4;
            log.error("数据保存到redis异常", e);
            return false;
        }
    }

    public boolean set(String key, String value, long time) {
        try {
            if (time > 0L) {
                this.stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                this.set(key, value);
            }

            return true;
        } catch (Exception var6) {
            Exception e = var6;
            log.error("数据保存到redis并设置过期时间异常", e);
            return false;
        }
    }

}
