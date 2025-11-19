package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.Comment;
import com.github.an001code.blog.pojo.CommentQuery;
import com.github.an001code.blog.pojo.PageResult;

import java.util.List;

public interface CommentService {
    PageResult<Comment> getCommentList(CommentQuery commentQuery);

    Integer addComment(Comment comment);

    boolean delete(List<Integer> ids);
}
