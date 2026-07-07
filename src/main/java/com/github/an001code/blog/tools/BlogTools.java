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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogTools {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;
    private final VectorStore vectorStore;

    @Tool(description = Constant.Tools.CREATE_BLOG_BY_ID)
    public String addBlogById(
            @ToolParam(description = "博客标题") String title,
            @ToolParam(description = "博客正文内容，markdown 格式") String content,
            @ToolParam(description = "博客摘要，不超过 200 字") String summary,
            ToolContext toolContext) {

        Map<String, Object> ctx = toolContext.getContext();
        Long userId = (Long) ctx.get(Constant.USER_ID);
        String requestId = (String) ctx.get(Constant.REQUEST_ID);
        log.info("addBlogById tool called, userId={}, title={}", userId, title);

        Article article = Article.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .summary(summary)
                .status(0)
                .build();

        int rows = articleService.addArticle(article);

        if (rows > 0) {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", true, "articleId", article.getArticleId(), "message", "博客草稿创建成功"));
            return "博客《" + title + "》已生成草稿，请在前端确认内容后发布。";
        }

        ToolResultHolder.put(requestId, "blog", Map.of("success", false, "message", "博客创建失败"));
        return "博客创建失败，请重试。";
    }

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
            return "未找到标题为《" + title + "》的博客，请确认标题是否正确。";
        }

        ArticleQuery query = new ArticleQuery();
        query.setArticleId(article.getArticleId());
        query.setContent(content);
        query.setSummary(summary);

        int rows = articleService.update(query);

        if (rows > 0) {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", true, "articleId", article.getArticleId(), "message", "博客修改成功"));
            return "博客《" + title + "》修改成功。";
        }

        ToolResultHolder.put(requestId, "blog", Map.of("success", false, "message", "博客修改失败"));
        return "博客修改失败，请重试。";
    }

    @Tool(description = Constant.Tools.SEARCH_ARTICLES)
    public String searchArticles(
            @ToolParam(description = "搜索关键词或主题") String keyword,
            ToolContext toolContext) {

        String requestId = (String) toolContext.getContext().get(Constant.REQUEST_ID);
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder().query(keyword).topK(10).similarityThreshold(0.35).build());

        Map<Long, Article> uniqueArticles = new LinkedHashMap<>();
        for (Document doc : docs) {
            Long articleId = parseArticleId(doc.getMetadata().get("articleId"));
            if (articleId == null || uniqueArticles.containsKey(articleId)) {
                continue;
            }

            Article article = articleMapper.getById(articleId);
            if (!isSearchableArticle(article) || !isKeywordRelevant(keyword, article)) {
                continue;
            }

            uniqueArticles.put(articleId, article);
            if (uniqueArticles.size() >= 3) {
                break;
            }
        }

        if (uniqueArticles.isEmpty()) {
            ToolResultHolder.put(requestId, "blog",
                    Map.of("success", false, "message", "未找到与「" + keyword + "」相关的已发布文章"));
            return "未找到与「" + keyword + "」相关的已发布文章。";
        }

        List<Map<String, Object>> articles = uniqueArticles.values().stream()
                .map(a -> Map.<String, Object>of(
                        "articleId", a.getArticleId(),
                        "title", nullToEmpty(a.getTitle()),
                        "summary", nullToEmpty(a.getSummary()),
                        "tagName", nullToEmpty(a.getTagName())))
                .collect(Collectors.toList());

        ToolResultHolder.put(requestId, "blog", Map.of("success", true, "articles", articles));

        String articleList = articles.stream()
                .map(a -> "- 《" + a.get("title") + "》" +
                        (a.get("tagName").toString().isEmpty() ? "" : " [" + a.get("tagName") + "]") +
                        "\n  " + a.get("summary"))
                .collect(Collectors.joining("\n"));

        return "找到以下与「" + keyword + "」相关的文章：\n" + articleList;
    }

    private Long parseArticleId(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Long.valueOf(String.valueOf(value));
        } catch (NumberFormatException e) {
            log.warn("Invalid articleId in vector search result: {}", value);
            return null;
        }
    }

    private boolean isSearchableArticle(Article article) {
        if (article == null || article.getArticleId() == null) {
            return false;
        }
        if (!Objects.equals(article.getStatus(), 1) || Objects.equals(article.getIsDeleted(), 1)) {
            return false;
        }
        return hasText(article.getTitle()) && (hasText(article.getSummary()) || hasText(article.getContent()));
    }

    private boolean isKeywordRelevant(String keyword, Article article) {
        List<String> terms = extractSearchTerms(keyword);
        if (terms.isEmpty()) {
            return true;
        }

        String haystack = (nullToEmpty(article.getTitle()) + " " +
                nullToEmpty(article.getSummary()) + " " +
                nullToEmpty(article.getContent()) + " " +
                nullToEmpty(article.getTagName())).toLowerCase();

        return terms.stream().anyMatch(haystack::contains);
    }

    private List<String> extractSearchTerms(String keyword) {
        String normalized = nullToEmpty(keyword)
                .toLowerCase()
                .replaceAll("[\\p{Punct}\\s]+", "")
                .replace("帮我", "")
                .replace("找找", "")
                .replace("看看", "")
                .replace("查找", "")
                .replace("搜索", "")
                .replace("推荐", "")
                .replace("相关", "")
                .replace("有关", "")
                .replace("关于", "")
                .replace("博客", "")
                .replace("文章", "")
                .replace("内容", "")
                .replace("一下", "")
                .replace("一些", "")
                .replace("和", "")
                .replace("与", "")
                .replace("的", "");

        if (!hasText(normalized)) {
            return List.of();
        }
        if (normalized.length() <= 4) {
            return List.of(normalized);
        }
        return List.of(normalized, normalized.substring(0, 4), normalized.substring(0, 2));
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
