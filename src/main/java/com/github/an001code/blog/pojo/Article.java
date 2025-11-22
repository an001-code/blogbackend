package com.github.an001code.blog.pojo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文章实体类
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Long articleId;  //文章Id
    private Long userId;  //作者Id
    private Long tagId;  //标签id
    private String tagName; //标签内容
    private String title; //标题
    private String content; //内容
    private String summary; //摘要
    private String coverImage;  //封面图片
    private Integer status = 1;  //文章状态，0为草稿，1为已发布
    private Integer viewCount = 0; //阅读量
    private Integer likeCount = 0;  //点赞量
    private Integer favoriteCount = 0;  //收藏量
    private Integer commentCount = 0;  //评论量
    private Integer isDeleted = 0;  //逻辑删除，0为正常，1为已删除
    private LocalDateTime createdAt;  //创建时间
    private LocalDateTime updatedAt;  //更新时间
    private LocalDateTime publishedAt; //发布时间
}
