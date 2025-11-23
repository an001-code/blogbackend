package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.Result;
import com.github.an001code.blog.pojo.User;
import com.github.an001code.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RegisterController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        if(user.getPassword() == null || user.getEmail() == null){
            return Result.error("登录失败");
        }
        User u = userService.register(user);
        if(u == null){
            return Result.error("ACCOUNT ALREADY EXISTS");
        }
        return Result.success(u);
    }
}
