package com.github.an001code.blog.mapper;


import com.github.an001code.blog.pojo.Follow;
import com.github.an001code.blog.pojo.FollowQueryParams;
import com.github.an001code.blog.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper {
    boolean updateFollow(Follow follow);

    Follow getFollow(Follow follow);

    boolean addfollow(Follow follow);

    List<User> getFollowingListByFollowerId(FollowQueryParams params);

    List<User> getFollowerList(FollowQueryParams params);
}
