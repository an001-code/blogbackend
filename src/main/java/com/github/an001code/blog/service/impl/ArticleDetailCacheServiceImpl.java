package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.ArticleMapper;
import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleQuery;
import com.github.an001code.blog.service.ArticleDetailCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ArticleDetailCacheServiceImpl implements ArticleDetailCacheService {
    @Autowired
    @Qualifier("articleDetailTemplate")
    private RedisTemplate<String, Article> redisTemplate;
    @Autowired
    ArticleMapper articleMapper;

    private static final String ARTICLE_KEY_PREFIX = "article:";

    private static final long ARTICLE_TTL = 10*60; // 10分钟

    /**
     * 缓存文章信息
     * @param article
     */
    @Override
    public void cacheArticle(Article article) {
        try{
            String key = ARTICLE_KEY_PREFIX + article.getArticleId();
            redisTemplate.opsForValue().set(key,article,ARTICLE_TTL,TimeUnit.SECONDS);
            log.debug("缓存文章：{}",article.getArticleId());
        }catch (Exception e){
            log.error("缓存文章失败: {}", article.getArticleId(), e);
        }
    }

    /**
     * 从缓存获取文章
     * @param articleId
     * @return
     */
    @Override
    public Article getArticleFromCache(Long articleId) {
        try{
            String key = ARTICLE_KEY_PREFIX + articleId;
            Article article = (Article) redisTemplate.opsForValue().get(key);
            if(article != null){
                log.debug("从缓存中命中文章:{}",articleId);
            }
            return article;
        }catch (Exception e){
            log.error("从缓存获取文章失败: {}", articleId, e);
            return null;
        }
    }

    /**
     * 删除缓存中的文章
     * @param articleId
     */
    @Override
    public void evictArticleCache(Long articleId) {
        try {
            String key = ARTICLE_KEY_PREFIX + articleId;
            redisTemplate.delete(key);
            log.debug("删除文章缓存: {}", articleId);
        } catch (Exception e) {
            log.error("删除文章缓存失败: {}", articleId, e);
        }
    }

    /**
     * 更新文章缓存（重新从数据库加载）
     * @param articleId
     */
    @Override
    public void updateArticleCache(Long articleId) {
        try {
            // 先删除旧缓存
            evictArticleCache(articleId);

            Article latestArticle = loadLatestArticle(articleId);
            if (latestArticle != null) {
                cacheArticle(latestArticle);
                log.debug("更新文章缓存成功: {}", articleId);
            }
        } catch (Exception e) {
            log.error("触发文章缓存更新失败: {}", articleId, e);
        }
    }

    private Article loadLatestArticle(Long articleId){
        return articleMapper.getById(articleId);
    }

}
