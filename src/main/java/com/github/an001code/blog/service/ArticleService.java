package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticlePageBean;

import java.time.LocalDate;

public interface ArticleService {
    ArticlePageBean getArticleList(String query, Integer articleId, Integer userId, String tag, Integer status, Integer isDeleted, LocalDate begin, LocalDate end, Integer page, Integer pageSize);

    Article getById(Long id);

    int addArticle(Article article);

    int update(Article article);
}
