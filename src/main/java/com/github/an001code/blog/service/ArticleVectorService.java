package com.github.an001code.blog.service;

import com.github.an001code.blog.mapper.ArticleMapper;
import com.github.an001code.blog.pojo.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleVectorService {

    private final ArticleMapper articleMapper;
    private final VectorStore vectorStore;

    private static final int BATCH_SIZE = 10;
    private static final int MAX_CONTENT_LENGTH = 3000;

    /**
     * 将 MySQL 中所有已发布文章同步到 ES 向量库
     * @return 成功索引的文章数量
     */
    public int indexPublishedArticles() {
        List<Article> articles = articleMapper.findAllPublished();
        if (articles.isEmpty()) {
            log.info("没有需要索引的已发布文章");
            return 0;
        }

        List<Document> batch = new ArrayList<>();
        int total = 0;

        for (Article a : articles) {
            batch.add(new Document(
                    String.valueOf(a.getArticleId()),
                    buildDocumentText(a),
                    buildMetadata(a)));

            if (batch.size() >= BATCH_SIZE) {
                vectorStore.add(batch);
                total += batch.size();
                log.info("已索引 {}/{} 篇", total, articles.size());
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            vectorStore.add(batch);
            total += batch.size();
        }

        log.info("向量库同步完成，共索引 {} 篇文章", total);
        return total;
    }

    /**
     * 检查向量库状态，返回近似文档数量
     */
    public long countDocuments() {
        var results = vectorStore.similaritySearch(
                SearchRequest.builder().query("test").topK(1000).similarityThreshold(0.01).build());
        return results.size();
    }

    /**
     * 索引单篇文章到向量库（异步调用）
     */
    public void indexArticle(Long articleId) {
        Article article = articleMapper.getById(articleId);
        if (article == null || article.getStatus() != 1 || article.getIsDeleted() == 1) {
            return;
        }
        Document doc = new Document(
                String.valueOf(article.getArticleId()),
                buildDocumentText(article),
                buildMetadata(article));
        vectorStore.add(List.of(doc));
        log.info("文章 {} 已同步到向量库", articleId);
    }

    /**
     * 从向量库删除文章（异步调用）
     */
    public void removeArticle(Long articleId) {
        vectorStore.delete(List.of(String.valueOf(articleId)));
        log.info("文章 {} 已从向量库移除", articleId);
    }

    private Map<String, Object> buildMetadata(Article a) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("articleId", a.getArticleId());
        metadata.put("title", a.getTitle());
        metadata.put("summary", a.getSummary() != null ? a.getSummary() : "");
        if (a.getTagName() != null) {
            metadata.put("tagName", a.getTagName());
        }
        return metadata;
    }

    private String buildDocumentText(Article a) {
        StringBuilder sb = new StringBuilder();
        sb.append(a.getTitle());
        if (a.getSummary() != null && !a.getSummary().isEmpty()) {
            sb.append("\n").append(a.getSummary());
        }
        if (a.getContent() != null && !a.getContent().isEmpty()) {
            String content = a.getContent();
            if (content.length() > MAX_CONTENT_LENGTH) {
                content = content.substring(0, MAX_CONTENT_LENGTH);
            }
            sb.append("\n").append(content);
        }
        return sb.toString();
    }
}
