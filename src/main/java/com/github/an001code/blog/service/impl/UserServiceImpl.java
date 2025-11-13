package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.UserMapper;
import com.github.an001code.blog.pojo.PasswordUpdateDTO;
import com.github.an001code.blog.pojo.User;
import com.github.an001code.blog.pojo.UserPageBean;
import com.github.an001code.blog.service.UserService;
import com.github.an001code.blog.utils.IdentifierUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserPageBean getUserList(String query, Integer userId, Integer status, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<User> userList = userMapper.getUserList(query,userId,status,begin,end);
        Page<User> p = (Page<User>) userList;
        UserPageBean userPageBean = new UserPageBean(p.getTotal(),p.getResult());
        return userPageBean;
    }

    @Override
    public User getById(Integer id) {
        User user = userMapper.getById(id);
        user.setPassword(null);
        return user;
    }

    @Override
    @Transactional
    public Long addUser(User user) {
          if(userMapper.getByEmail(user.getEmail()) !=null){  //检验账号是否被注册过
              return -1L;
          }
          // 加密密码
          if(user.getPassword().equals("")){
             return -1L;
          }
          String encodedPassword = passwordEncoder.encode(user.getPassword());
          user.setPassword(encodedPassword);
          int row = userMapper.insert(user);
          Long id = user.getUserId();    //获取新注册账号的id
          System.out.println(id);
          if(id!=null){
              String identifier = IdentifierUtils.generateIdentifier(id); //生成相应的特征码
              userMapper.updateIdentifier(id,identifier);
          }
          return id;
    }

    @Override
    public boolean updateById(User user) {
        int affectRows = userMapper.updateById(user);
        if(affectRows < 1){
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updatePassword(PasswordUpdateDTO updateDTO) {
            String savedPassword = userMapper.getPasswordById(updateDTO.getUserId());//获取旧的加密密码
            if(passwordEncoder.matches(updateDTO.getOldPassword(),savedPassword)){    //验证密码是否正确
                String newEncodedPassword = passwordEncoder.encode(updateDTO.getNewPassword());    //产生新的密码
                int effectRows = userMapper.updatePassword(updateDTO.getUserId(),newEncodedPassword);
                if(effectRows <1){
                    return false;
                }
                return true;
            }
            else{
                return false;
            }
    }

    @Override
    public boolean delete(List<Long> ids) {
          int effectRows = userMapper.delete(ids);
          if(effectRows < 1){
              return false;
          }
          return true;
    }
}
