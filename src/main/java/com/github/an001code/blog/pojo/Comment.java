package com.github.an001code.blog.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long commentId;  //评论id
    private Long articleId;  //文章id
    private Long userId;  //用户id
    private Long parentId;  //父评论id
    private String content;  //评论内容
    private Integer isDeleted = 0; //评论状态，0为正常，1为已删除
    private String deleteReason;  //删除原因
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;  //更新时间
}
