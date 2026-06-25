package com.study.usermanagement.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 用户实体类，对应数据库 user 表
// ユーザーエンティティ。DB の user テーブルに対応する
public class User {
    // 用户主键 id，由数据库自增生成
    // ユーザー主キー id。DB の自動採番で生成される
    private Integer id;

    // 用户名，注册和登录时使用
    // ユーザー名。登録とログインで使う
    @NotBlank(message = "用户名不能为空")
    private String username;

    // 密码，当前学习项目中直接保存，实际项目应使用加密
    // パスワード。現在の学習プロジェクトでは直接保存するが、実務では暗号化が必要
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 12, message = "密码长度必须是6到12位")
    private String password;

    // 用户角色：ADMIN 表示管理员，USER 表示普通用户
    // ユーザーロール：ADMIN は管理者、USER は一般ユーザー
    private String role;

    // 登录 token：登录成功后生成，用于后续接口认证
    // ログイン token：ログイン成功後に生成され、後続APIの認証に使う
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // 获取 token，token 用来表示用户登录状态
    // token を取得する。token はログイン状態を表す
    public String getToken() {
        return token;
    }

    // 设置 token，登录成功后会把新 token 放进 User 对象或数据库
    // token をセットする。ログイン成功後、新しい token を User オブジェクトまたは DB に保存する
    public void setToken(String token) {
        this.token = token;
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

    // 无参构造方法：Spring Boot 把 JSON 转成 User 对象时需要用
    // デフォルトコンストラクタ：Spring Boot が JSON を User オブジェクトへ変換するときに使う
    public User() {
    }

    // 有参构造方法：方便测试或手动创建完整 User 对象
    // 引数付きコンストラクタ：テストや手動で完全な User オブジェクトを作るときに便利
    public User(Integer id, String username, String password, String role, String token) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.token = token;
    }

    // 获取用户名
    // ユーザー名を取得する
    public String getUsername() {
        return username;
    }

    // 设置用户名，Spring Boot 接收 JSON 时会用到
    // ユーザー名をセットする。Spring Boot が JSON を受け取るときにも使う
    public void setUsername(String username) {
        this.username = username;
    }

    // 获取密码，登录和修改密码时会用到
    // パスワードを取得する。ログインやパスワード変更で使う
    public String getPassword() {
        return password;
    }

    // 设置密码，Spring Boot 接收 JSON 时会用到
    // パスワードをセットする。Spring Boot が JSON を受け取るときにも使う
    public void setPassword(String password) {
        this.password = password;
    }

    // 把 User 对象转成字符串，主要方便调试查看
    // User オブジェクトを文字列に変換する。主にデバッグ用
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
