package com.github.an001code.blog.mapper;

import com.github.an001code.blog.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ArticleMapper {

    List<Article> getArticleList(String query, Integer articleId, Integer userId, String tag, Integer status
            , Integer isDeleted, LocalDate begin, LocalDate end);

}
