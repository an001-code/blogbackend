package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.Comment;
import com.github.an001code.blog.pojo.CommentQuery;
import com.github.an001code.blog.pojo.PageResult;

public interface CommentService {
    PageResult<Comment> getCommentList(CommentQuery commentQuery);
}
