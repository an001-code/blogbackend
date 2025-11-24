package com.github.an001code.blog.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowQueryParams {
    private Long followerId;
    private Long followingId;
    Integer page = 1;
    Integer pageSize = 10;
}
