package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.CommentMapper;
import com.github.an001code.blog.pojo.Comment;
import com.github.an001code.blog.pojo.CommentQuery;
import com.github.an001code.blog.pojo.PageResult;
import com.github.an001code.blog.service.CommentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleServiceImpl articleService;
    @Override
    public PageResult<Comment> getCommentList(CommentQuery commentQuery) {
        log.info("进入getCommentList");
        PageHelper.startPage(commentQuery.getPage(),commentQuery.getPageSize());
        List<Comment> commentList = commentMapper.getCommentList(commentQuery);
        Page p = (Page) commentList;
        PageResult<Comment> commentPage = new PageResult<>(p.getTotal(),p.getResult());
        return commentPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addComment(Comment comment) {
//       添加评论同时要增加文章的评论量
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        Integer affectRows = commentMapper.addComment(comment);
        if(affectRows > 0){
            articleService.increaseCommentCount(comment.getArticleId());
        }
        return affectRows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        // 1. 获取所有要删除的评论（包括子孙评论）
        List<Comment> allCommentsToDelete = commentMapper.getAllDescendantsAndSelf(ids);

        if (allCommentsToDelete.isEmpty()) {
            return true; // 无数据可删
        }

        // 2. 提取所有评论ID（用于批量逻辑删除）
        List<Long> commentIdsToDelete = allCommentsToDelete.stream()
                .map(Comment::getCommentId)
                .collect(Collectors.toList());

        // 3. 按文章ID统计每个文章需要减少的评论数量
        Map<Long, Long> articleCommentDecrementMap = allCommentsToDelete.stream()
                .collect(Collectors.groupingBy(
                        Comment::getArticleId,
                        Collectors.counting()
                ));

        // 4. 批量逻辑删除评论
        commentMapper.batchLogicDelete(commentIdsToDelete);

        // 5. 批量减少文章评论数（articleService 中提供批量接口）
        for (Map.Entry<Long, Long> entry : articleCommentDecrementMap.entrySet()) {
            articleService.decreaseCommentCounts(entry.getKey(), entry.getValue().intValue());
        }
        return true;
    }
}
