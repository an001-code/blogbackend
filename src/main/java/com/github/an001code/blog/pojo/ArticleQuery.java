package com.github.an001code.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleQuery {
    private String query;
    private Long articleId;
    private Long userId;
    private String tag;
    private Long tagId;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private Integer status;
    private Integer viewCount ;
    private Integer likeCount ;
    private Integer favoriteCount ;
    private Integer commentCount ;
    private Integer isDeleted;
    private String begin;
    private String end;
    private String publishedAt;
    private Integer page = 1;
    private Integer pageSize = 10;
}
