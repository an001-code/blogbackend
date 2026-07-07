package com.github.an001code.blog.tools;

import com.github.an001code.blog.config.ToolResultHolder;
import com.github.an001code.blog.mapper.ArticleMapper;
import com.github.an001code.blog.pojo.Article;
import com.github.an001code.blog.pojo.ArticleQuery;
import com.github.an001code.blog.service.ArticleService;
import constants.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogTools {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;
    private final VectorStore vectorStore;
    
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
        log.info("addBlogById 工具被调用, userId={}, title={}", userId, title);

        Article article = Article.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .summary(summary)
                .status(0) // AI 生成的文章默认草稿，用户确认后发布
                .build();

        int rows = articleService.addArticle(article);

        if (rows > 0) {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", true, "articleId", article.getArticleId(), "message", "博客创建成功"));
            return "博客《" + title + "》已生成草稿，请前往用户中心确认内容后发布";
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

        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder().query(keyword).topK(3).similarityThreshold(0.3).build());

        if (docs.isEmpty()) {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", false, "message", "未找到与《" + keyword + "》相关的文章"));
            return "未找到与《" + keyword + "》相关的文章";
        }

        List<Map<String, Object>> articles = docs.stream()
                .map(d -> Map.<String, Object>of(
                        "articleId", d.getMetadata().getOrDefault("articleId", ""),
                        "title", d.getMetadata().getOrDefault("title", ""),
                        "summary", d.getMetadata().getOrDefault("summary", ""),
                        "tagName", d.getMetadata().getOrDefault("tagName", "")))
                .collect(Collectors.toList());

        ToolResultHolder.put(requestId, "blog",
                Map.of("success", true, "articles", articles));

        String articleList = articles.stream()
                .map(a -> "- 《" + a.get("title") + "》" +
                        (a.get("tagName").toString().isEmpty() ? "" : " [" + a.get("tagName") + "]") +
                        "\n  " + a.get("summary"))
                .collect(Collectors.joining("\n"));

        return "找到以下与《" + keyword + "》相关的文章：\n" + articleList;
    }
}
