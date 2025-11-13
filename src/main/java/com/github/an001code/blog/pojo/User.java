package com.github.an001code.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long userId;//用户Id
    private String identifier;//用户特征码
    private String userName;//用户名
    private String email;//注册邮箱
    private String password;//密码
    private Integer role = 0;//用户身份，0为普通用户，1为管理员
    private Integer status = 0;//用户状态，0为正常，1为禁用
    private String avatar;//头像
    private Integer gender = 0;//性别，0为保密，1为男，2为女
    private LocalDate birthday;//生日
    private String signature;//签名
    private LocalDateTime createdAt;//创建时间
    private LocalDateTime updatedAt;//更新时间
    private String jwt;  //jwt令牌

}


