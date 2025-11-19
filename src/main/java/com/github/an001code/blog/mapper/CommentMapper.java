package com.github.an001code.blog.mapper;


import com.github.an001code.blog.pojo.Comment;
import com.github.an001code.blog.pojo.CommentQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> getCommentList(CommentQuery commentQuery);

    Integer addComment(Comment comment);

    void deleteComments(@Param("ids") List<Long> commentIdsToDelete);

    Comment getById(Integer id);

    List<Comment> getChildCommentList(Integer id);

    List<Comment> getAllDescendantsAndSelf(@Param("ids") List<Integer> ids);

    void batchLogicDelete(@Param("ids") List<Long> commentIdsToDelete);
}
