package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.CommentMapper;
import com.github.an001code.blog.pojo.Comment;
import com.github.an001code.blog.pojo.CommentQuery;
import com.github.an001code.blog.pojo.PageResult;
import com.github.an001code.blog.service.CommentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public PageResult<Comment> getCommentList(CommentQuery commentQuery) {
        log.info("进入getCommentList");
        PageHelper.startPage(commentQuery.getPage(),commentQuery.getPageSize());
        List<Comment> commentList = commentMapper.getCommentList(commentQuery);
        Page p = (Page) commentList;
        PageResult<Comment> commentPage = new PageResult<>(p.getTotal(),p.getResult());
        return commentPage;
    }
}
