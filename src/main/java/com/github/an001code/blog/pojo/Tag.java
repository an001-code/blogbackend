package com.github.an001code.blog.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 标签信息实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private Long tagId;    //标签的id
    private String tagName;  //标签名
    private Long useCount = 0L;   //标签使用次数
    private Integer status = 1;   //状态，0为禁用，1为启用
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;    //更新时间
}
