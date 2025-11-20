package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.*;
import com.github.an001code.blog.service.ArticleFavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ArticleFavoriteController {
    @Autowired
    ArticleFavoriteService articleFavoriteService;

    /**
     * 收藏状态切换
     * @param articleFavorite
     * @return
     */
    @PostMapping("/article-favorite")
    public Result toggleFavorite(@RequestBody ArticleFavorite articleFavorite){
        log.info("进入toggleFavorite:{}",articleFavorite);
        if(articleFavorite.getArticleId() == null || articleFavorite.getUserId() == null){
            return Result.error("articleId或者userId不能为空");
        }
        if(articleFavoriteService.toggleFavorite(articleFavorite)){
            return Result.success();
        }
        return Result.error("收藏状态转换失败");
    }

    @GetMapping("/article-favorite")
    public Result getFavoriteList(ArticleFavoriteQuery favoriteQuery){
        if(favoriteQuery.getUserId() == null){
            return Result.error("userId不能为空");
        }
        PageResult<Article> articlePage = articleFavoriteService.getFavoriteList(favoriteQuery);
        return Result.success(articlePage);
    }
}
