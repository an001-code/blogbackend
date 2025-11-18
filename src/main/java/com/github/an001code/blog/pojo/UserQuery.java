package com.github.an001code.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuery {
    private String query;
    private Long userId;//用户Id
    private String identifier;//用户特征码
    private String userName;//用户名
    private String email;//注册邮箱
    private String password;//密码
    private Integer role;//用户身份，0为普通用户，1为管理员
    private Integer status;//用户状态，0为正常，1为禁用
    private String avatar;//头像
    private Integer gender;//性别，0为保密，1为男，2为女
    private LocalDate birthday;//生日
    private String signature;//签名

    private String begin;
    private String end;
    private Integer page = 1;
    private Integer pageSize = 10;
}
