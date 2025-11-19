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

    //增加阅读量
     void increaseViewCount(Long id);

    //增加点赞量
     void increaseLikeCount(Long id);

    //增加收藏量
     void increaseFavoriteCount(Long id);

    //增加评论量
    void increaseCommentCount(Long id);

    //减少点赞量
     void decreaseLikeCount(Long id);

    //减少收藏量
     void decreaseFavoriteCount(Long id);

    //减少评论量
    void decreaseCommentCount(Long id);
//  按键值减少评论量
    void decreaseCommentCounts(Long key, int i);
}
