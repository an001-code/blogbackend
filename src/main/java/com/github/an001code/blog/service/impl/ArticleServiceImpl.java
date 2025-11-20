package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.ArticleMapper;
import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleQuery;
import com.github.an001code.blog.pojo.PageResult;
import com.github.an001code.blog.service.ArticleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    TagServiceImpl tagService;

    @Override
    public PageResult<Article> getArticleList(ArticleQuery articleQuery) {
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
        increaseViewCount(id);    //查询相应文章的具体信息的时候，先增加阅读量。
        Article article = articleMapper.getById(id);
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
        return affectRows;
    }

    @Override
    @Transactional
    public int update(ArticleQuery articleQuery) {
        if(articleQuery.getTagId() != null){
            Long tagId = articleMapper.getTagId(articleQuery.getArticleId());//判断之前的标签与改后的标签时候一样，如果不同需要对标签的数量进行更改
            if(tagId != articleQuery.getTagId()){
                tagService.decreaseUseCount(tagId);
                tagService.increaseUseCount(articleQuery.getTagId());
            }
        }
       int affectRows = articleMapper.update(articleQuery);
       return affectRows;
    }

    @Override
    public boolean delete(List<Integer> ids) {
        int affectRows = articleMapper.logicDelete(ids);
        if(affectRows < 1){
            return false;
        }
        return true;
    }


    //增加阅读量
    @Override
    public void increaseViewCount(Long id){
        articleMapper.increaseViewCount(id);
    }

    //增加点赞量
    @Override
    public void increaseLikeCount(Long id){
        articleMapper.increaseLikeCount(id);
    }

    //增加收藏量
    @Override
    public void increaseFavoriteCount(Long id){
        articleMapper.increaseFavoriteCount(id);
    }

    //增加评论量
    @Override
    public void increaseCommentCount(Long id){
        articleMapper.increaseCommentCount(id);
    }

    //减少点赞量
    @Override
    public void decreaseLikeCount(Long id){
        articleMapper.decreaseLikeCount(id);
    }

    //减少收藏量
    @Override
    public void decreaseFavoriteCount(Long id){
        articleMapper.decreaseFavoriteCount(id);
    }

    //减少评论量
    @Override
    public void decreaseCommentCount(Long id){
        articleMapper.decreaseCommentCount(id);
    }

    @Override
//  根据i减少相应评论量
    public void decreaseCommentCounts(Long key, int i) {
        articleMapper.decreaseCommentCounts(key,i);
    }


}
