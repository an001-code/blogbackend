package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.Comment;
import com.github.an001code.blog.pojo.CommentQuery;
import com.github.an001code.blog.pojo.PageResult;
import com.github.an001code.blog.pojo.Result;
import com.github.an001code.blog.service.CommentService;
import com.github.an001code.blog.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 评论列表分页查询
    @GetMapping("/comments")
    public Result getCommentList(CommentQuery commentQuery) {
        log.info("分页查询评论列表,{}", commentQuery);
        PageResult<Comment> pageResult = commentService.getCommentList(commentQuery);
        return Result.success(pageResult);
    }

    // 添加评论
    @PostMapping("/comments")
    public Result addComment(@RequestBody Comment comment, HttpServletRequest request) {
        log.info("添加评论,{}", comment);

        // 1. 从请求头获取 token（字段名需与前端一致）
        String token = request.getHeader("token");
        if (token == null || token.trim().isEmpty()) {
            return Result.error("请先登录");
        }
        try {
            // 2. 解析 JWT 获取用户 ID
            Claims claims = JwtUtils.parseJwt(token);
            Integer userId = (Integer) claims.get("id"); // 确保登录时 put("id", ...)

            if (userId == null) {
                return Result.error("用户身份无效");
            }

            // 3. 设置必要字段（由后端控制，不信任前端传的 userId！）
            comment.setUserId(Long.valueOf(userId));
            // 4. 调用 service 保存
            Integer affectRows = commentService.addComment(comment);
            if (affectRows < 1) {
                return Result.error("评论添加失败");
            }
            return Result.success("评论添加成功");

        } catch (Exception e) {
            log.error("解析 token 或保存评论失败", e);
            return Result.error("评论失败，请登录后重试");
        }
    }

    // 根据 ids 批量删除评论（逻辑删除）
    @DeleteMapping("/comments")
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("批量删除评论,{}", ids);
        boolean affectRows = commentService.delete(ids);
        if (!affectRows) {
            return Result.error("评论删除失败");
        }
        return Result.success("评论删除成功");
    }
}