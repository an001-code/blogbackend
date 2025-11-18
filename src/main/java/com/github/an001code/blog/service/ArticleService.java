package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleQuery;
import com.github.an001code.blog.pojo.PageResult;

import java.util.List;

public interface ArticleService {
    PageResult<Article> getArticleList(ArticleQuery articleQuery);

    Article getById(Long id);

    int addArticle(Article article);

    int update(ArticleQuery articleQuery);

    boolean delete(List<Integer> ids);
}
