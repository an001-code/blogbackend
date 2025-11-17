package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.ArticlePageBean;
import com.github.an001code.blog.pojo.Result;
import com.github.an001code.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
public class ArticleController {
    @Autowired
    ArticleService articleService;


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
}
