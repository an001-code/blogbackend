package com.github.an001code.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleFavoriteQuery {
    private Long userId;
    private Integer page = 1;
    private Integer pageSize = 10;
}
