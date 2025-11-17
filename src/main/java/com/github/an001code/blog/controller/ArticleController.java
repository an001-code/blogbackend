package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticlePageBean;
import com.github.an001code.blog.pojo.Result;
import com.github.an001code.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Slf4j
public class ArticleController {
    @Autowired
    ArticleService articleService;

    /**
     * 标签列表查询
     * @param query
     * @param articleId
     * @param userId
     * @param tag
     * @param status
     * @param isDeleted
     * @param begin
     * @param end
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/articles/select")
    public Result getArticleList(String query, Integer articleId, Integer userId, String tag,
                                 Integer status, Integer isDeleted,
                                 @DateTimeFormat(pattern = "yy-MM-dd") LocalDate begin,
                                 @DateTimeFormat(pattern = "yy-MM-dd")LocalDate end,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize){
            log.info("进入getArticleList");
            if(status == null){
                return Result.error("status不能为空");
            }
           ArticlePageBean articlePageBean = articleService.getArticleList(query,articleId,userId,tag,status,isDeleted,begin,end,page,pageSize);
            return Result.success(articlePageBean);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/articles/{id}")
    public Result getById(@PathVariable Long id){
        log.info("进入getById");
        if(id == null){
            return Result.error("id不能为空");
        }
        Article article = articleService.getById(id);
        return Result.success(article);
    }

    /**
     * 文章添加
     * @param article
     * @return
     */
    @PostMapping("/articles")
    public Result addArticle(@RequestBody Article article){
        log.info("进入addArticle");
        if(article.getUserId() == null){
            return Result.error("userId不能为空");
        }
        int affectRows = articleService.addArticle(article);
        if(affectRows < 1){
            return Result.error("添加失败");
        }
        return Result.success();
    }

    @PutMapping("/articles")
    public Result update(@RequestBody Article article){
        log.info("进入update");
        if(article.getArticleId() == null){
            return Result.error("articleId不能为空");
        }
        int affectRows = articleService.update(article);
        if(affectRows < 1){
            return Result.error("修改失败");
        }
        return Result.success();
    }


}
