package com.github.an001code.blog.service.impl;

import com.github.an001code.blog.mapper.UserMapper;
import com.github.an001code.blog.pojo.*;
import com.github.an001code.blog.service.UserService;
import com.github.an001code.blog.utils.IdentifierUtils;
import com.github.an001code.blog.utils.JwtUtils;
import com.github.an001code.blog.utils.UserContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public PageResult<User> getUserList(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPage(), userQuery.getPageSize());
        List<User> userList = userMapper.getUserList(userQuery);
        Page<User> p = (Page<User>) userList;
        PageResult<User> userPage = new PageResult<>(p.getTotal(), p.getResult());
        return userPage;
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
        if (userMapper.getByEmail(user.getEmail()) != null) {  //检验账号是否被注册过
            return -1L;
        }
        if(user.getUserName() == null){
            user.setUserName(user.getEmail());
        }
        // 加密密码
        if (user.getPassword().equals("")) {
            return -1L;
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        int row = userMapper.insert(user);
        Long id = user.getUserId();    //获取新注册账号的id
        System.out.println(id);
        if (id != null) {
            String identifier = IdentifierUtils.generateIdentifier(id); //生成相应的特征码
            userMapper.updateIdentifier(id, identifier);
        }
        return id;
    }

    @Override
    public boolean updateById(UserQuery userQuery) {
        int affectRows = userMapper.updateById(userQuery);
        if (affectRows < 1) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updatePassword(PasswordUpdateDTO updateDTO) {
        String savedPassword = userMapper.getPasswordById(updateDTO.getUserId());//获取旧的加密密码
        if (passwordEncoder.matches(updateDTO.getOldPassword(), savedPassword)) {    //验证密码是否正确
            String newEncodedPassword = passwordEncoder.encode(updateDTO.getNewPassword());    //产生新的密码
            int effectRows = userMapper.updatePassword(updateDTO.getUserId(), newEncodedPassword);
            if (effectRows < 1) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(List<Long> ids) {
        int effectRows = userMapper.delete(ids);
        if (effectRows < 1) {
            return false;
        }
        return true;
    }

    @Override
    public User login(User user) {
        User u = userMapper.getByEmail(user.getEmail());
        if(u == null) {
            return null;  // 用户不存在
        }
        String savedPassword = u.getPassword();
        if(!passwordEncoder.matches(user.getPassword(), savedPassword)){      //验证密码
            return null;
        }
        else{
            Map<String, Object> claims = new HashMap<>();
            //UserContext.setUser(user.getUserId());
            claims.put("userId", u.getUserId());
            claims.put("email", u.getEmail());
            String jwt = JwtUtils.generateJwt(claims);
            u.setJwt(jwt);
            u.setPassword(null);
            return u;
        }
    }

    @Override
    @Transactional
    public User register(User user) {
        Long id = addUser(user);
        if (id == null || id <= 0) {
            log.warn("注册失败: email={}, reason={}", user.getEmail(), id == null ? "插入后未获取到ID" : "邮箱已存在或密码为空");
            return null;
        }
        User u = userMapper.getByEmail(user.getEmail());
        if (u == null) {
            log.error("注册异常: 插入成功但查询不到用户, email={}", user.getEmail());
            return null;
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", u.getUserId());
        claims.put("email", u.getEmail());
        String jwt = JwtUtils.generateJwt(claims);
        u.setJwt(jwt);
        u.setPassword(null);
        log.info("注册成功: userId={}, email={}", u.getUserId(), u.getEmail());
        return u;
    }
}
