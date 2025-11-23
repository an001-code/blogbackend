package com.github.an001code.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
    private Long followId;//主键
    private Long followerId;//关注者ID
    private Long followingId;//被关注者ID
    private Integer status;
    private String createAt;
    private String updateAt;
}
