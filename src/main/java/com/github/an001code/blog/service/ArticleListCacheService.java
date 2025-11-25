package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleQuery;

import java.util.List;

public interface ArticleListCacheService {
    //缓存文章列表
    public void cacheArticleList(ArticleQuery query, List<Article> articles);

    //从缓存获取文章列表
    public List<Article> getArticleListFromCache(ArticleQuery query);
}
