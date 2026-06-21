package com.study.usermanagement.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class User {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6,max = 12, message = "密码长度必须是6到12位")
    private String password;

    private String role;

    private String token;

    // 获取 token，token 用来表示用户登录状态
    public String getToken() {
        return token;
    }

    // 设置 token，登录成功后会把新 token 放进 User 对象或数据库
    public void setToken(String token) {
        this.token = token;
    }

    // 获取角色，比如 ADMIN 或 USER
    public String getRole() {
        return role;
    }

    // 设置角色
    public void setRole(String role) {
        this.role = role;
    }

    // 无参构造方法：Spring Boot 把 JSON 转成 User 对象时需要用
    public User() {
    }

    // 有参构造方法：方便自己 new User(username, password)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 获取用户名
    public String getUsername() {
        return username;
    }

    // 设置用户名，Spring Boot 接收 JSON 时会用到
    public void setUsername(String username) {
        this.username = username;
    }

    // 获取密码，登录和修改密码时会用到
    public String getPassword() {
        return password;
    }

    // 设置密码，Spring Boot 接收 JSON 时会用到
    public void setPassword(String password) {
        this.password = password;
    }

    // 把 User 对象转成字符串，主要方便调试查看
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
