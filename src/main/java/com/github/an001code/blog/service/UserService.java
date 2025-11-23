package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.*;

import java.util.List;

public interface UserService {
    PageResult<User> getUserList(UserQuery userQuery);

    User getById(Integer id);

    Long addUser(User user);

    boolean updateById(UserQuery userQuery);

    boolean updatePassword(PasswordUpdateDTO updateDTO);

    boolean delete(List<Long> ids);

    User login(User user);

    User register(User user);
}
