package com.github.an001code.blog.controller;


import com.github.an001code.blog.pojo.Comment;
import com.github.an001code.blog.pojo.CommentQuery;
import com.github.an001code.blog.pojo.PageResult;
import com.github.an001code.blog.pojo.Result;
import com.github.an001code.blog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;

//    评论列表分页查询
    @GetMapping("/comments")
    public Result getCommentList(CommentQuery commentQuery){
        log.info("分页查询评论列表,{}",commentQuery);
        PageResult<Comment> pageResult = commentService.getCommentList(commentQuery);
        return Result.success(pageResult);
    }


}
