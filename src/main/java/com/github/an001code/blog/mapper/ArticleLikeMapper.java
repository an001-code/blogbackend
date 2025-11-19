package com.github.an001code.blog.mapper;

import com.github.an001code.blog.pojo.ArticleLike;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ArticleLikeMapper {

    @Select("select * from article_like where article_id = #{articleId} and user_id = #{userId}")
    ArticleLike getArticleLike(ArticleLike articleLike);

    @Insert("insert into article_like(article_id,user_id) values(#{articleId},#{userId})")
    int insert(ArticleLike articleLike);

    @Update("update article_like set status = #{status} where article_id = #{articleId} and user_id = #{userId}")
    int update(ArticleLike articleLike);
}
