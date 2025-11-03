package com.github.an001code.blog.pojo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 点赞表实体类
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLike {
    private Long likeId;  //点赞记录id
    private Long articleId; //文章Id
    private Long userId; //用户Id
    private Integer status = 1; //点赞状态，1为有效，0为无效
    private LocalDateTime createdAt; //创建时间
    private LocalDateTime updatedAt; //更新时间
}
