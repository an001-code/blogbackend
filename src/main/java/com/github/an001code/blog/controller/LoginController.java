package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.Result;
import com.github.an001code.blog.pojo.User;
import com.github.an001code.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;

    /*用户登陆*/
    @PostMapping
    public Result login(@RequestBody User user) {
        log.info("用户登陆");
        User u =userService.login(user);
        if(u != null){
            return Result.success(u);
        }
        return Result.error("登陆失败");
    }

}
