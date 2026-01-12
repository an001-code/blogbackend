package com.github.an001code.blog.controller;

import com.github.an001code.blog.pojo.*;
import com.github.an001code.blog.service.UserService;
import com.sun.istack.NotNull;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@Validated
public class UserController {
        @Autowired
         UserService userService;

    /**
     * 用户列表查询
     * @param userQuery
     * @return
     */
        @GetMapping("/users/select")
        public Result getUserList(UserQuery userQuery){
            log.info("进入getUserList");
            PageResult<User> userPage = userService.getUserList(userQuery);
            log.info("得到userPageBean");
            return Result.success(userPage);
        }

    /**
     * 根据id查询
     * @param id
     * @return
     */

    @GetMapping("/users/{id}")
        public Result getById(@PathVariable Integer id ){
            log.info("进入getById");
            if(id == null){
                return Result.error("id不能为空");
            }
            User user = userService.getById(id);
            log.info("得到user");
            return Result.success(user);
        }

    /**
     * 用户信息添加
     * @param user
     * @return
     */

    @PostMapping("/users")
        public Result addUser(@RequestBody User user){
            log.info("进入addUser");
            Long userId = userService.addUser(user);
            log.info("完成添加");
            if(userId != null){
                if(userId < 0){
                    return Result.error("账号已经存在，添加邮箱失败");
                }
                else{
                    return Result.success();
                }
            }
            return Result.error("添加失败");
        }

    /**
     * 用户信息更改
     * @param userQuery
     * @return
     */
    @PutMapping("/users")
        public Result updateUser(@RequestBody UserQuery userQuery){
            log.info("进入updateUser");
            if(userQuery.getUserId() == null){
                return Result.error("id不能为空");
            }
            if(userService.updateById(userQuery)){
                return Result.success();
            }
            else{
                return Result.error("更新失败");
            }

        }

    /**
     * 用户密码更改
     * @param updateDTO
     * @return
     */
    @PutMapping("/users/password")
        public Result updatePassword(@Valid @RequestBody PasswordUpdateDTO updateDTO){
             log.info("进入updatePassword");
             if(userService.updatePassword(updateDTO)){
                 return Result.success();
             }
             else{
                 return Result.error("账号或密码错误");
             }
        }

    /**
     * 用户信息删除，可能用不上
     * @param ids
     * @return
     */

    @DeleteMapping("/users/{ids}")
        public Result delete(@PathVariable List<Long> ids){
            if(userService.delete(ids)){
                return Result.success();
            }
            else{
                return Result.error("删除失败");
            }
        }

}
