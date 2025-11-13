package com.github.an001code.blog.service;

import com.github.an001code.blog.pojo.PasswordUpdateDTO;
import com.github.an001code.blog.pojo.User;
import com.github.an001code.blog.pojo.UserPageBean;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    UserPageBean getUserList(String query, Integer userId, Integer status, LocalDate begin, LocalDate end, Integer page, Integer pageSize);

    User getById(Integer id);

    Long addUser(User user);

    boolean updateById(User user);

    boolean updatePassword(PasswordUpdateDTO updateDTO);

    boolean delete(List<Long> ids);
}
