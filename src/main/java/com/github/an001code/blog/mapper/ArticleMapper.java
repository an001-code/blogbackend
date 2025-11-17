package com.github.an001code.blog.mapper;

import com.github.an001code.blog.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ArticleMapper {

    List<Article> getArticleList(String query, Integer articleId, Integer userId, String tag, Integer status
            , Integer isDeleted, LocalDate begin, LocalDate end);

    @Select("select a.*,t.tag_name from article a left join tag t on a.tag_id = t.tag_id where article_id = #{id}")
    Article getById(Long id);

    @Update("update article set view_count = view_count+1 where article_id = #{id} and status = 1")
    void increaseViewCount(Long id);


    int insert(Article article);

    int update(Article article);
}
