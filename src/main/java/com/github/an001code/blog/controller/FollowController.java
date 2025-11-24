package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.*;
import com.github.an001code.blog.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FollowController {
    @Autowired
    private FollowService followService;

    /*关注添加/取消*/
    @PostMapping("/user-follow")
    public Result follow(@RequestBody Follow follow){
        log.info("关注添加/取消:{}",follow);
        if(follow.getFollowerId() == null || follow.getFollowingId() == null){
            return Result.error("userId或者followUserId不能为空");
        }
        if(followService.follow(follow)){
            return Result.success();
        }
        return Result.error("关注失败");
    }

    /*分页查询用户关注列表*/
    @GetMapping("/user-follow/following")
    public Result getFollowingList(FollowQueryParams params){
        if (params.getFollowerId() == null) {
            throw new IllegalArgumentException("followerId 不能为空");
        }
        log.info("分页查询用户关注列表:{}",params);
        PageResult<User> pageResult = followService.getFollowingListByFollowerId(params);
        return Result.success(pageResult);
    }

    /*分页查询用户粉丝列表*/
    @GetMapping("/user-follow/follower")
    public Result getFollowerList(FollowQueryParams params){
        if (params.getFollowingId() == null) {
            throw new IllegalArgumentException("followingId 不能为空");
        }
        log.info("分页查询用户粉丝列表:{}",params);
        PageResult<User> pageResult = followService.getFollowerList(params);
        return Result.success(pageResult);
    }



}
