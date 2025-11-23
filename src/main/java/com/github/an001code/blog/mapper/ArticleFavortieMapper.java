package com.github.an001code.blog.mapper;

import com.github.an001code.blog.pojo.ArticleFavorite;
import com.github.an001code.blog.pojo.ArticleFavoriteQuery;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ArticleFavortieMapper {

    @Select("select * from article_favorite where article_id = #{articleId} and user_id = #{userId}")
    ArticleFavorite getArticleFavortie(ArticleFavorite articleFavorite);

    @Insert("insert into article_favorite(article_id,user_id) values(#{articleId},#{userId})")
    int insert(ArticleFavorite articleFavorite);

    @Update("update article_favorite set status = #{status} where article_id = #{articleId} and user_id = #{userId}")
    int update(ArticleFavorite articleFavortie);

    @Select("SELECT article_id FROM article_favorite WHERE user_id = #{userId} AND status = 1")
    List<Long> getArticleIdList(ArticleFavoriteQuery favoriteQuery);
}
