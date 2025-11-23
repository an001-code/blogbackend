package com.github.an001code.blog.mapper;


import com.github.an001code.blog.pojo.User;
import com.github.an001code.blog.pojo.UserQuery;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserMapper {


    List<User> getUserList(UserQuery userQuery);

    @Select("select * from user where user_id = #{id}")
    User getById(Integer id);

    @Select("select * from user where email = #{email}")
    User getByEmail(String email);

    @Select("select password from user where user_id = #{id}")
    String getPasswordById(Long id);


    Integer insert(User user);

    @Update("update user set identifier = #{identifier} where user_id = #{id}")
    void updateIdentifier(Long id, String identifier);

    int updateById(UserQuery userQuery);

    @Update("update user set password = #{newEncodedPassword} where user_id = #{userId}")
    int updatePassword(@NotNull(message = "用户ID不能为空") Long userId, String newEncodedPassword);

    int delete(List<Long> ids);



}
