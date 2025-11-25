package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleQuery;

import java.util.List;

public interface ArticleDetailCacheService {

    //缓存文章详情（单个文章）
    public void cacheArticle(Article article);

    //从缓存获取文章
    public Article getArticleFromCache(Long articleId);

    //删除文章缓存
    public void evictArticleCache(Long articleId);

    //更新文章缓存
    public void updateArticleCache(Long articleId);





}
