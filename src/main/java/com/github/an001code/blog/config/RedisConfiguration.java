package com.github.an001code.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.an001code.blog.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@Slf4j
public class RedisConfiguration {

    /* 统一 ObjectMapper */
    private ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    /* 文章详情 */
    @Bean("articleDetailTemplate")
    public RedisTemplate<String, Article> articleDetailTemplate(RedisConnectionFactory factory) {
        log.info("创建文章详情Redis模板");
        RedisTemplate<String, Article> t = new RedisTemplate<>();
        t.setConnectionFactory(factory);

        // 正确写法：只传 ObjectMapper + 类型
        Jackson2JsonRedisSerializer<Article> ser =
                new Jackson2JsonRedisSerializer<>(redisObjectMapper(), Article.class);

        t.setKeySerializer(new StringRedisSerializer());
        t.setValueSerializer(ser);
        t.setHashKeySerializer(new StringRedisSerializer());
        t.setHashValueSerializer(ser);
        t.afterPropertiesSet();
        return t;
    }

    /* 文章列表 */
    @Bean("articleListTemplate")
    public RedisTemplate<String, List<Article>> articleListTemplate(RedisConnectionFactory factory) {
        log.info("创建文章列表Redis模板");
        RedisTemplate<String, List<Article>> t = new RedisTemplate<>();
        t.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer<List> ser =
                new Jackson2JsonRedisSerializer<>(redisObjectMapper(), List.class);

        t.setKeySerializer(new StringRedisSerializer());
        t.setValueSerializer(ser);
        t.afterPropertiesSet();
        return t;
    }

    /* 通用模板 */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        log.info("创建通用Redis模板");
        RedisTemplate<String, Object> t = new RedisTemplate<>();
        t.setConnectionFactory(factory);

        GenericJackson2JsonRedisSerializer generic =
                new GenericJackson2JsonRedisSerializer(redisObjectMapper());

        t.setKeySerializer(new StringRedisSerializer());
        t.setValueSerializer(generic);
        t.setHashKeySerializer(new StringRedisSerializer());
        t.setHashValueSerializer(generic);
        t.afterPropertiesSet();
        return t;
    }
}