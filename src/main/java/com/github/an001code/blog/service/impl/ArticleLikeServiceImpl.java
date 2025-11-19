package com.github.an001code.blog.service.impl;


import com.github.an001code.blog.mapper.ArticleLikeMapper;
import com.github.an001code.blog.pojo.ArticleLike;
import com.github.an001code.blog.service.ArticleLikeService;
import com.github.an001code.blog.service.ArticleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleLikeServiceImpl implements ArticleLikeService {
    @Autowired
    ArticleLikeMapper articleLikeMapper;
    @Autowired
    ArticleService articleService;

    @Override
    @Transactional
    public boolean toggleLike(ArticleLike articleLike) {
          ArticleLike like = articleLikeMapper.getArticleLike(articleLike);
          if(like == null){
              int affectRows = articleLikeMapper.insert(articleLike);
              articleService.increaseLikeCount(articleLike.getArticleId());   //增加文章点赞量
              if(affectRows < 1) return false;
          }
          else{
              if(like.getStatus() == 0){      //未点赞，重新点赞
                  like.setStatus(1);
                  int affectRows = articleLikeMapper.update(like);
                  articleService.increaseLikeCount(articleLike.getArticleId());  //增加文章点赞量
                  if(affectRows < 1) return false;
              }
              else{                                 //已经点赞，取消点赞
                   like.setStatus(0);
                   int affectRows = articleLikeMapper.update(like);
                   articleService.decreaseLikeCount(articleLike.getArticleId());    //减少文章点赞量
                   if(affectRows < 1) return false;
              }
          }
          return true;
    }
}
