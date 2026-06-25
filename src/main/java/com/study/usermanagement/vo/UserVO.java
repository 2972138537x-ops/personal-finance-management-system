package com.study.usermanagement.vo;

// 用户展示对象：不返回 password，只返回前端需要看的用户信息
// ユーザー表示用オブジェクト：password は返さず、フロントに必要なユーザー情報だけを返す
public class UserVO {
    // 用户名，允许返回给前端
    // ユーザー名。フロントへ返してよい情報
    private String username;

    // 用户角色，允许返回给前端
    // ユーザーロール。フロントへ返してよい情報
    private String role;

    // 无参构造方法：给框架或工具创建对象使用
    // デフォルトコンストラクタ：フレームワークやツールがオブジェクトを作るときに使う
    public UserVO() {
    }

    // 有参构造方法：封装要返回给前端看的 username 和 role
    // 引数付きコンストラクタ：フロントに返す username と role をまとめる
    public UserVO(String username, String role) {
        this.username = username;
        this.role = role;
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

    // 获取角色
    // ロールを取得する
    public String getRole() {
        return role;
    }

    // 设置角色
    // ロールをセットする
    public void setRole(String role) {
        this.role = role;
    }
}
