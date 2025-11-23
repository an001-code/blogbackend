package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.Follow;
import com.github.an001code.blog.pojo.FollowQueryParams;
import com.github.an001code.blog.pojo.PageResult;
import com.github.an001code.blog.pojo.User;

public interface FollowService {
    boolean follow(Follow follow);

    PageResult<User> getFollowingListByFollowerId(FollowQueryParams params);

    PageResult<User> getFollowerList(FollowQueryParams params);
}
