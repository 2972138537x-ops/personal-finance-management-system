package com.study.usermanagement.vo;

// 登录结果展示对象：登录成功后返回用户名、角色和 token
// ログイン結果表示用オブジェクト：ログイン成功後にユーザー名、権限、token を返す
public class LoginVO {
    private String username;
    private String role;
    private String token;

    // 无参构造方法：给框架或工具创建对象使用
    public LoginVO() {
    }

    // 有参构造方法：登录成功时一次性封装 username、role、token
    public LoginVO(String username, String role, String token) {
        this.username = username;
        this.role = role;
        this.token = token;
    }

    // 获取用户名
    public String getUsername() {
        return username;
    }

    // 设置用户名
    public void setUsername(String username) {
        this.username = username;
    }

    // 获取角色，比如 ADMIN 或 USER
    public String getRole() {
        return role;
    }

    // 设置角色
    public void setRole(String role) {
        this.role = role;
    }

    // 获取登录 token
    public String getToken() {
        return token;
    }

    // 设置登录 token
    public void setToken(String token) {
        this.token = token;
    }
}
