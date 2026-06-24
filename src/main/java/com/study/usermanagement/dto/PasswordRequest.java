package com.study.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 修改密码专用请求体，只接收新密码
// パスワード変更専用のリクエスト DTO。新しいパスワードだけを受け取る
public class PasswordRequest {
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 12, message = "新密码长度必须是6到12位")
    private String password;

    // 无参构造方法：Spring Boot 把 JSON 转成 Java 对象时需要用
    // デフォルトコンストラクタ：Spring Boot が JSON を Java オブジェクトへ変換するときに使う
    public PasswordRequest() {
    }

    // 有参构造方法：方便自己 new PasswordRequest("123456")
    // 引数付きコンストラクタ：自分で new PasswordRequest("123456") するときに便利
    public PasswordRequest(String password) {
        this.password = password;
    }

    // getter：让外部代码读取 password
    // getter：外部コードが password を読み取るために使う
    public String getPassword() {
        return password;
    }

    // setter：Spring Boot 接收 JSON 时，会通过它把 password 放进对象
    // setter：Spring Boot が JSON を受け取るとき、このメソッドで password をセットする
    public void setPassword(String password) {
        this.password = password;
    }
}
