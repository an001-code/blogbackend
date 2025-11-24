package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.FollowMapper;
import com.github.an001code.blog.pojo.Follow;
import com.github.an001code.blog.pojo.FollowQueryParams;
import com.github.an001code.blog.pojo.PageResult;
import com.github.an001code.blog.pojo.User;
import com.github.an001code.blog.service.FollowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowMapper followMapper;
    @Override
    public boolean follow(Follow follow) {
        // 1. 参数校验
        if (follow.getFollowerId() == null || follow.getFollowingId() == null) {
            log.warn("关注操作参数为空: followerId={}, followingId={}",
                    follow.getFollowerId(), follow.getFollowingId());
            return false;
        }

        // 2. 禁止自己关注自己
        if (follow.getFollowerId().equals(follow.getFollowingId())) {
            log.warn("用户尝试关注自己: userId={}", follow.getFollowerId());
            return false;
        }

        // 3. 查询当前关注关系
        Follow existing = followMapper.getFollow(follow);

        if (existing == null) {
            // 首次关注：插入新记录，状态为 1（已关注）
            follow.setStatus(1);
            boolean inserted = followMapper.addfollow(follow); // 方法名建议改为 addFollow
            return inserted ;
        } else {
            // 已存在：切换状态（1 → 0 取消，0 → 1 恢复）
            int newStatus = existing.getStatus() == 1 ? 0 : 1;
            existing.setStatus(newStatus);
            boolean updated = followMapper.updateFollow(existing); // 建议方法名改为 updateFollow
            return updated ;
        }
    }

    @Override
    public PageResult<User> getFollowingListByFollowerId(FollowQueryParams params) {
        PageHelper.startPage(params.getPage(), params.getPageSize());
        List<User> page = followMapper.getFollowingListByFollowerId(params);
//      设置用户密码为null 避免泄露给前端
        page.forEach(user -> user.setPassword(null));
        Page<User> p = (Page<User>) page;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public PageResult<User> getFollowerList(FollowQueryParams params) {
        PageHelper.startPage(params.getPage(), params.getPageSize());
        List<User> page = followMapper.getFollowerList(params);
        page.forEach(user -> user.setPassword(null));
        Page<User> p = (Page<User>) page;
        return new PageResult<>(p.getTotal(), p.getResult());
    }
}
