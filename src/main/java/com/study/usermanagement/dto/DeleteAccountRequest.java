package com.study.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 注销账号请求体：注销前要求用户输入两遍当前密码
// アカウント退会リクエストDTO：退会前に現在のパスワードを2回入力させる
public class DeleteAccountRequest {
    // 当前密码：用于确认操作者确实知道自己的账号密码
    // 現在のパスワード：本人がアカウントのパスワードを知っているか確認する
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 12, message = "密码长度必须是6到12位")
    private String password;

    // 确认密码：必须和 password 一致，避免误输入后直接注销账号
    // 確認用パスワード：誤入力のまま退会しないように password と一致させる
    @NotBlank(message = "确认密码不能为空")
    @Size(min = 6, max = 12, message = "确认密码长度必须是6到12位")
    private String confirmPassword;

    public DeleteAccountRequest() {
    }

    public DeleteAccountRequest(String password, String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
