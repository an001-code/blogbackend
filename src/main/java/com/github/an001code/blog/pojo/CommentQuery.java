package com.github.an001code.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentQuery {
    private Integer articleId;
    private Integer parentId;
    private Integer userId;
//   默认page=1 pagesize=10
    private Integer page = 1;
    private Integer pageSize = 10;
}
