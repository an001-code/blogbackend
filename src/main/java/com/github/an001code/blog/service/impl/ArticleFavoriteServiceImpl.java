package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.ArticleFavortieMapper;
import com.github.an001code.blog.pojo.*;
import com.github.an001code.blog.service.ArticleFavoriteService;
import com.github.an001code.blog.service.ArticleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleFavoriteServiceImpl implements ArticleFavoriteService {
    @Autowired
    ArticleFavortieMapper articleFavortieMapper;
    @Autowired
    ArticleService articleService;

    @Override
    @Transactional
    public boolean toggleFavorite(ArticleFavorite articleFavorite) {
        ArticleFavorite favorite = articleFavortieMapper.getArticleFavortie(articleFavorite);
        if(favorite == null){
            int affectRows = articleFavortieMapper.insert(articleFavorite);
            articleService.increaseFavoriteCount(articleFavorite.getArticleId());   //增加文章收藏量
            if(affectRows < 1) return false;
        }
        else{
            if(favorite.getStatus() == 0){      //未收藏，重新点赞
                favorite.setStatus(1);
                int affectRows = articleFavortieMapper.update(favorite);
                articleService.increaseFavoriteCount(favorite.getArticleId()); //增加文章收藏量
                if(affectRows < 1) return false;
            }
            else{                                 //已经收藏，取消点赞
                favorite.setStatus(0);
                int affectRows = articleFavortieMapper.update(favorite);
                articleService.decreaseFavoriteCount(favorite.getArticleId());   //减少文章收藏量
                if(affectRows < 1) return false;
            }
        }
        return true;
    }

    @Override
    public PageResult<Article> getFavoriteList(ArticleFavoriteQuery favoriteQuery) {
        //获取收藏的文章的id的列表
        List<Long> articleIdList = articleFavortieMapper.getArticleIdList(favoriteQuery);
        log.info("userId:{}",favoriteQuery.getUserId());
        log.info("articleList:{}",articleIdList);

        //根据文章的id列表获取文章的内容列表
        PageHelper.startPage(favoriteQuery.getPage(),favoriteQuery.getPageSize());
        List<Article> articles = articleService.getFavoriteList(articleIdList);
        Page<Article> p = (Page<Article>) articles;
        PageResult<Article> pageResult = new PageResult<>(p.getTotal(),p.getResult());
        return pageResult;

    }
}
