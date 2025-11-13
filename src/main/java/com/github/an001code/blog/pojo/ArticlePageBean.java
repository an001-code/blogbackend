package com.github.an001code.blog.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePageBean {
    private Long total;   //总记录数
    private List<Article> rows;  //数据
}
