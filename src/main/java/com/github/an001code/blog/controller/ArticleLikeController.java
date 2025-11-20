package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.ArticleLike;
import com.github.an001code.blog.pojo.Result;
import com.github.an001code.blog.service.ArticleLikeService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
public class ArticleLikeController {
    @Autowired
    ArticleLikeService articleLikeService;

    /**
     * 点赞状态切换
     * @param articleLike
     * @return
     */
    @PostMapping("/article-like")
    public Result toggleLike(@RequestBody ArticleLike articleLike){
        if(articleLike.getArticleId() == null || articleLike.getUserId() == null){
            return Result.error("articleId和userId不能为空");
        }
        if(articleLikeService.toggleLike(articleLike)){
            return Result.success();
        }
        return Result.error("点赞状态切换失败");
    }
}
