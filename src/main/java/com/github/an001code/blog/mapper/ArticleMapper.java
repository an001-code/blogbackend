package com.github.an001code.blog.mapper;

import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ArticleMapper {

    List<Article> getArticleList(ArticleQuery articleQuery);

    @Select("select a.*,t.tag_name from article a left join tag t on a.tag_id = t.tag_id where article_id = #{id}")
    Article getById(Long id);

    @Select("select tag_id from article where article_id = #{articleId}")
    Long getTagId(Long articleId);


    int insert(Article article);

    int update(ArticleQuery articleQuery);

    int logicDelete(List<Integer> ids);

    @Update("update article set view_count = view_count+1 where article_id = #{id} and status = 1")
    void increaseViewCount(Long id);

    @Update("update article set like_count = like_count+1 where article_id = #{id} and status = 1")
    void increaseLikeCount(Long id);

    @Update("update article set favorite_count = favorite_count+1 where article_id = #{id} and status = 1")
    void increaseFavoriteCount(Long id);

    @Update("update article set comment_count = comment_count+1 where article_id = #{id} and status = 1")
    void increaseCommentCount(Long id);

    @Update("update article set like_count = GREATEST(like_count - 1, 0) where article_id = #{id} and status = 1")
    void decreaseLikeCount(Long id);

    @Update("update article set favorite_count = GREATEST(favorite_count - 1, 0) where article_id = #{id} and status = 1")
    void decreaseFavoriteCount(Long id);

    @Update("update article set comment_count = GREATEST(comment_count - 1, 0) where article_id = #{id} and status = 1")
    void decreaseCommentCount(Long id);


    void decreaseCommentCounts(@Param("articleId") Long key, @Param("count") int i);

    List<Article> getFavortieList(List<Long> articleIdList);
}
