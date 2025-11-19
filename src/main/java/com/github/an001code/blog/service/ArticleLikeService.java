package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.ArticleLike;

public interface ArticleLikeService {
    boolean toggleLike(ArticleLike articleLike);
}
