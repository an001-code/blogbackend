package com.github.an001code.blog.mapper;


import com.github.an001code.blog.pojo.Comment;
import com.github.an001code.blog.pojo.CommentQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> getCommentList(CommentQuery commentQuery);
}
