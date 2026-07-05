package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.*;
import com.github.an001code.blog.service.ArticleService;
import com.github.an001code.blog.service.ArticleVectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleVectorService articleVectorService;

    /**
     * 文章列表查询
     * @param articleQuery
     * @return
     */
    @GetMapping("/articles/select")
    public Result getArticleList(ArticleQuery articleQuery){
            log.info("进入getArticleList");
            if(articleQuery.getStatus() == null){
                return Result.error("status不能为空");
            }
           PageResult<Article> pageResult = articleService.getArticleList(articleQuery);
            return Result.success(pageResult);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/articles/{id:\\d+}")
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

    /**
     * 文章修改
     * @param articleQuery
     * @return
     */
    @PutMapping("/articles")
    public Result update(@RequestBody ArticleQuery articleQuery){
        log.info("进入update");
        if(articleQuery.getArticleId() == null){
            return Result.error("articleId不能为空");
        }
        int affectRows = articleService.update(articleQuery);
        if(affectRows < 1){
            return Result.error("修改失败");
        }
        return Result.success();
    }

    /**
     * 文章删除（逻辑删除）
     * @param ids
     * @return
     */
    @DeleteMapping("/articles/{ids:[\\d,]+}")
    public Result delete(@PathVariable List<Integer> ids){
        log.info("进入delete");
        if(articleService.delete(ids)){
            return Result.success();
        }
        return Result.error("删除失败");
    }

    /**
     * 将已发布文章同步到 ES 向量库
     */
    @PostMapping("/articles/sync-vectors")
    public Result syncToVectorStore() {
        log.info("开始同步文章到向量库");
        int count = articleVectorService.indexPublishedArticles();
        return Result.success("成功同步 " + count + " 篇文章到向量库");
    }

    /**
     * 检查 ES 向量库状态
     */
    @GetMapping("/articles/vector-status")
    public Result vectorStatus() {
        long count = articleVectorService.countDocuments();
        return Result.success(Map.of("docCount", count, "isEmpty", count == 0));
    }

}
