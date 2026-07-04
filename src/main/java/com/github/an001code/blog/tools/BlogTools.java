package com.github.an001code.blog.tools;

import com.github.an001code.blog.config.ToolResultHolder;
import com.github.an001code.blog.mapper.ArticleMapper;
import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleQuery;
import com.github.an001code.blog.pojo.PageResult;
import com.github.an001code.blog.service.ArticleService;
import constants.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BlogTools {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;
    
    //新增博客工具
    @Tool(description = Constant.Tools.CREATE_BLOG_BY_ID)
    public String addBlogById(
            @ToolParam(description = "博客标题") String title,
            @ToolParam(description = "博客正文内容，markdown格式") String content,
            @ToolParam(description = "博客摘要，不超过200字") String summary,
            ToolContext toolContext) {

        Map<String, Object> ctx = toolContext.getContext();
        Long userId = (Long) ctx.get(Constant.USER_ID);
        String requestId = (String) ctx.get(Constant.REQUEST_ID);

        Article article = Article.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .summary(summary)
                .status(1)
                .build();

        int rows = articleService.addArticle(article);

        if (rows > 0) {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", true, "articleId", article.getArticleId(), "message", "博客创建成功"));
            return "博客《" + title + "》创建成功，文章ID: " + article.getArticleId();
        } else {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", false, "message", "博客创建失败"));
            return "博客创建失败，请重试";
        }
    }

    //修改博客工具
    @Tool(description = Constant.Tools.UPDATE_BLOG_BY_TITLE)
    public String updateBlogByTitle(
            @ToolParam(description = "要修改的博客标题") String title,
            @ToolParam(description = "修改后的正文内容") String content,
            @ToolParam(description = "修改后的摘要") String summary,
            ToolContext toolContext) {

        Map<String, Object> ctx = toolContext.getContext();
        Long userId = (Long) ctx.get(Constant.USER_ID);
        String requestId = (String) ctx.get(Constant.REQUEST_ID);

        Article article = articleMapper.findByTitleAndUserId(title, userId);
        if (article == null) {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", false, "message", "未找到标题为《" + title + "》的博客"));
            return "未找到标题为《" + title + "》的博客，请确认标题是否正确";
        }

        ArticleQuery query = new ArticleQuery();
        query.setArticleId(article.getArticleId());
        query.setContent(content);
        query.setSummary(summary);

        int rows = articleService.update(query);

        if (rows > 0) {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", true, "articleId", article.getArticleId(), "message", "博客修改成功"));
            return "博客《" + title + "》修改成功";
        } else {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", false, "message", "博客修改失败"));
            return "博客修改失败，请重试";
        }
    }

    //查询文章工具
    @Tool(description = Constant.Tools.SEARCH_ARTICLES)
    public String searchArticles(
            @ToolParam(description = "搜索关键词或主题") String keyword,
            ToolContext toolContext) {

        String requestId = (String) toolContext.getContext().get(Constant.REQUEST_ID);

        ArticleQuery query = new ArticleQuery();
        query.setQuery(keyword);
        query.setStatus(1);
        query.setPage(1);
        query.setPageSize(3);

        PageResult<Article> result = articleService.getArticleList(query);

        if (result.getRows().isEmpty()) {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", false, "message", "未找到与《" + keyword + "》相关的文章"));
            return "未找到与《" + keyword + "》相关的文章";
        }

        List<Map<String, Object>> articles = result.getRows().stream()
                .map(a -> Map.<String, Object>of(
                        "articleId", a.getArticleId(),
                        "title", a.getTitle(),
                        "summary", a.getSummary() != null ? a.getSummary() : "",
                        "tagName", a.getTagName() != null ? a.getTagName() : ""))
                .collect(Collectors.toList());

        ToolResultHolder.put(requestId, "blog",
                Map.of("success", true, "articles", articles, "total", result.getTotal()));

        String articleList = articles.stream()
                .map(a -> "- 《" + a.get("title") + "》" +
                        (a.get("tagName").toString().isEmpty() ? "" : " [" + a.get("tagName") + "]") +
                        "\n  " + a.get("summary"))
                .collect(Collectors.joining("\n"));

        return "找到 " + result.getTotal() + " 篇与《" + keyword + "》相关的文章，以下是前 " + articles.size() + " 篇：\n" + articleList;
    }
}
