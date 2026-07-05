package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.ArticleMapper;
import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleQuery;
import com.github.an001code.blog.pojo.PageResult;
import com.github.an001code.blog.service.ArticleDetailCacheService;
import com.github.an001code.blog.service.ArticleListCacheService;
import com.github.an001code.blog.service.ArticleService;
import com.github.an001code.blog.service.ArticleVectorService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    TagServiceImpl tagService;
    @Autowired
    private ArticleDetailCacheService articleDetailCacheService;
    @Autowired
    private ArticleListCacheService articleListCacheService;
    @Autowired
    private ArticleVectorService articleVectorService;

    //获取文章列表
    @Override
    public PageResult<Article> getArticleList(ArticleQuery articleQuery) {
        if (articleQuery.getIsDeleted() == null) {
            articleQuery.setIsDeleted(0); // 默认过滤已删除文章
        }
        PageHelper.startPage(articleQuery.getPage(),articleQuery.getPageSize());
        List<Article> articles = articleMapper.getArticleList(articleQuery);
        Page<Article> p = (Page<Article>) articles;
        PageResult<Article> pageResult = new PageResult<>(p.getTotal(),p.getResult());
        return pageResult;
    }

    //根据文章id列表查询收藏文章
    @Override
    public List<Article> getFavoriteList(List<Long> articleIdList){
        List<Article> articleList = articleMapper.getFavortieList(articleIdList);
        return articleList;
    }


    @Override
    @Transactional
    public Article getById(Long id) {
        // 先尝试从缓存获取
        Article cachedArticle = articleDetailCacheService.getArticleFromCache(id);
        if (cachedArticle != null) {
            log.info("文章详情缓存命中: {}", id);
            // 异步更新阅读量（不影响主流程）
            CompletableFuture.runAsync(() -> increaseViewCount(id));
            return cachedArticle;
        }
        // 缓存未命中，查询数据库
        increaseViewCount(id);
        Article article = articleMapper.getById(id);

        if (article != null) {
            // 异步缓存文章
            CompletableFuture.runAsync(() -> {
                articleDetailCacheService.cacheArticle(article);
            });
        }

        return article;
    }

    @Override
    @Transactional
    public int addArticle(Article article) {
        article.setPublishedAt(LocalDateTime.now());
        int affectRows = articleMapper.insert(article);
        if(article.getTagId() != null){
            tagService.increaseUseCount(article.getTagId());    //添加文章的时候，增加相应文章的标签的使用量
        }
        if (affectRows > 0) {
            runAfterCommit(() -> articleVectorService.indexArticle(article.getArticleId()));
        }
        return affectRows;
    }

    @Override
    @Transactional
    public int update(ArticleQuery articleQuery) {
        if(articleQuery.getTagId() != null){
            Long tagId = articleMapper.getTagId(articleQuery.getArticleId());
            if(tagId != articleQuery.getTagId()){
                tagService.decreaseUseCount(tagId);
                tagService.increaseUseCount(articleQuery.getTagId());
            }
        }
       int affectRows = articleMapper.update(articleQuery);
        if (affectRows > 0) {
            articleDetailCacheService.updateArticleCache(articleQuery.getArticleId());
            runAfterCommit(() -> articleVectorService.indexArticle(articleQuery.getArticleId()));
        }
       return affectRows;
    }

    @Override
    @Transactional
    public boolean delete(List<Integer> ids) {
        int affectRows = articleMapper.logicDelete(ids);
        if(affectRows > 0){
            ids.forEach(id -> articleDetailCacheService.evictArticleCache(id.longValue()));
            ids.forEach(id -> runAfterCommit(() -> articleVectorService.removeArticle(id.longValue())));
            return true;
        }
        return false;
    }

    private void runAfterCommit(Runnable task) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    CompletableFuture.runAsync(task)
                            .exceptionally(ex -> { log.error("向量库操作失败", ex); return null; });
                }
            });
        } else {
            CompletableFuture.runAsync(task)
                    .exceptionally(ex -> { log.error("向量库操作失败", ex); return null; });
        }
    }


    //增加阅读量
    @Override
    public void increaseViewCount(Long id){

        articleMapper.increaseViewCount(id);
        articleDetailCacheService.updateArticleCache(id);
    }

    //增加点赞量
    @Override
    public void increaseLikeCount(Long id){

        articleMapper.increaseLikeCount(id);
        articleDetailCacheService.updateArticleCache(id);
    }

    //增加收藏量
    @Override
    public void increaseFavoriteCount(Long id){

        articleMapper.increaseFavoriteCount(id);
        articleDetailCacheService.updateArticleCache(id);
    }

    //增加评论量
    @Override
    public void increaseCommentCount(Long id){
        articleMapper.increaseCommentCount(id);
        articleDetailCacheService.updateArticleCache(id);
    }

    //减少点赞量
    @Override
    public void decreaseLikeCount(Long id){
        articleMapper.decreaseLikeCount(id);
        articleDetailCacheService.updateArticleCache(id);
    }

    //减少收藏量
    @Override
    public void decreaseFavoriteCount(Long id){
        articleMapper.decreaseFavoriteCount(id);
        articleDetailCacheService.updateArticleCache(id);
    }

    //减少评论量
    @Override
    public void decreaseCommentCount(Long id){
        articleMapper.decreaseCommentCount(id);
        articleDetailCacheService.updateArticleCache(id);
    }

    @Override
//  根据i减少相应评论量
    public void decreaseCommentCounts(Long key, int i) {
        articleMapper.decreaseCommentCounts(key,i);
        articleDetailCacheService.updateArticleCache(key);
    }


}
