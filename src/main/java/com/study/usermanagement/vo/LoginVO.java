package com.study.usermanagement.vo;

// 登录结果展示对象：登录成功后返回用户名、角色和 token
// ログイン結果表示用オブジェクト：ログイン成功後にユーザー名、権限、token を返す
public class LoginVO {
    // 登录用户的用户名
    // ログインユーザーのユーザー名
    private String username;

    // 登录用户的角色
    // ログインユーザーのロール
    private String role;

    // 登录成功后返回给前端的 token
    // ログイン成功後にフロントへ返す token
    private String token;

    // 无参构造方法：给框架或工具创建对象使用
    // デフォルトコンストラクタ：フレームワークやツールがオブジェクトを作るときに使う
    public LoginVO() {
    }

    // 有参构造方法：登录成功时一次性封装 username、role、token
    // 引数付きコンストラクタ：ログイン成功時に username、role、token をまとめて詰める
    public LoginVO(String username, String role, String token) {
        this.username = username;
        this.role = role;
        this.token = token;
    }

    // 获取用户名
    // ユーザー名を取得する
    public String getUsername() {
        return username;
    }

    // 设置用户名
    // ユーザー名をセットする
    public void setUsername(String username) {
        this.username = username;
    }

    // 获取角色，比如 ADMIN 或 USER
    // ロールを取得する。例：ADMIN / USER
    public String getRole() {
        return role;
    }

    // 设置角色
    // ロールをセットする
    public void setRole(String role) {
        this.role = role;
    }

    // 获取登录 token
    // ログイン token を取得する
    public String getToken() {
        return token;
    }

    // 设置登录 token
    // ログイン token をセットする
    public void setToken(String token) {
        this.token = token;
    }
}
