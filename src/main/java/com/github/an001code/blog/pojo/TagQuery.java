package com.github.an001code.blog.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagQuery {
    private String begin;
    private String end;
    private Integer status;
    private Integer page = 1;
    private Integer pageSize = 10;
}
