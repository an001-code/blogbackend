package com.github.an001code.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 收藏记录实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleFavorite {
    private Long favoriteId; //收藏记录Id
    private Long articleId;  //文章Id
    private Long userId;  //收藏用户Id
    private Integer status = 1;  //收藏状态，1为收藏，0为未收藏
    private LocalDateTime createdAt;  //创建时间
    private LocalDateTime updatedAt;  //更新时间
}
