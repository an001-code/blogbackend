package com.github.an001code.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 关注表实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollow {
    private Long followId;  //关注记录id
    private Long followerId;  //关注者id(粉丝）
    private Long followingId;  //被关注者id（博主）
    private Integer status;  //关注状态，1为关注，0为未关注
    private LocalDateTime createdAt; //创建时间
    private LocalDateTime updatedAt; //更新时间
}
