package com.github.an001code.blog.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;  //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;  //更新时间
}
