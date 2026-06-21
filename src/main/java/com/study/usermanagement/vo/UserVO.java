package com.study.usermanagement.vo;

public class UserVO {
    private String username;
    private String role;

    // 无参构造方法：给框架或工具创建对象使用
    public UserVO() {
    }

    // 有参构造方法：封装要返回给前端看的 username 和 role
    public UserVO(String username,String role) {
        this.username = username;
        this.role = role;
    }

    // 获取用户名
    public String getUsername() {
        return username;
    }

    // 设置用户名
    public void setUsername(String username) {
        this.username = username;
    }

    // 获取角色
    public String getRole() {
        return role;
    }

    // 设置角色
    public void setRole(String role) {
        this.role = role;
    }
}
