package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleQuery;
import com.github.an001code.blog.service.ArticleListCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ArticleListCacheServiceImpl implements ArticleListCacheService {
    @Autowired
    @Qualifier("articleListTemplate")
    private RedisTemplate<String, List<Article>> redisTemplate;

    private static final String LIST_KEY_PREFIX = "article:list:";
    private static final long TTL = 10; // 10秒
    /**
     * 缓存文章列表
     * @param query
     * @param articles
     */
    @Override
    public void cacheArticleList(ArticleQuery query, List<Article> articles) {
        try {
            String key = generateListKey(query);
            redisTemplate.opsForValue().set(key, articles, TTL, TimeUnit.SECONDS); // 列表缓存1分钟
        } catch (Exception e) {
            log.error("缓存文章列表失败", e);
        }
    }

    /**
     * 从缓存获取文章列表
     * @param query
     * @return
     */
    @Override
    public List<Article> getArticleListFromCache(ArticleQuery query) {
        try {
            String key = generateListKey(query);
            return (List<Article>) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("从缓存获取文章列表失败", e);
            return null;
        }
    }


    //生成缓存列表key
    private String generateListKey(ArticleQuery query) {
        return LIST_KEY_PREFIX+ Objects.hash(query.getPage(), query.getPageSize(),
                query.getStatus(), query.getTagId(), query.getQuery(),query.getBegin(),query.getEnd());
    }
}
