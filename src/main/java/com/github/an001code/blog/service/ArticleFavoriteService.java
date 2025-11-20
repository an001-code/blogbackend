package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleFavorite;
import com.github.an001code.blog.pojo.ArticleFavoriteQuery;
import com.github.an001code.blog.pojo.PageResult;

public interface ArticleFavoriteService {
    boolean toggleFavorite(ArticleFavorite articleFavorite);

    PageResult<Article> getFavoriteList(ArticleFavoriteQuery favoriteQuery);
}
