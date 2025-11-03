package com.github.an001code.blog.exception;

import com.github.an001code.blog.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)  //捕获所有的异常
    public Result ex(Exception ex){
         ex.printStackTrace();
         return Result.error("操作失败，请联系管理员");
    }
}
