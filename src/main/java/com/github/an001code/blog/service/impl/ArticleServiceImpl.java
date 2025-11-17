package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.ArticleMapper;
import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticlePageBean;
import com.github.an001code.blog.service.ArticleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public ArticlePageBean getArticleList(String query, Integer articleId,
                                          Integer userId, String tag, Integer status, Integer isDeleted,
                                          LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Article> articles = articleMapper.getArticleList(query,articleId,userId,tag,status,isDeleted,begin,end);
        Page<Article> p = (Page<Article>) articles;
        ArticlePageBean articlePageBean = new ArticlePageBean(p.getTotal(),p.getResult());
        return articlePageBean;
    }

    public void increaseViewCount(Long id){        //增加文章阅读量
        articleMapper.increaseViewCount(id);
    }

    @Override
    @Transactional
    public Article getById(Long id) {
        increaseViewCount(id);    //查询相应文章的具体信息的时候，先增加阅读量。
        Article article = articleMapper.getById(id);
        return article;
    }

    @Override
    public int addArticle(Article article) {
        article.setPublishedAt(LocalDateTime.now());
        int affectRows = articleMapper.insert(article);
        return affectRows;
    }

    @Override
    public int update(Article article) {
       int affectRows = articleMapper.update(article);
       return affectRows;
    }
}
