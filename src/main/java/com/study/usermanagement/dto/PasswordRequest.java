package com.study.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 修改密码请求体：普通用户修改密码时使用 oldPassword，管理员重置密码时主要使用 newPassword
// パスワード変更リクエストDTO：一般ユーザー変更時は oldPassword、管理者リセット時は主に newPassword を使う
public class PasswordRequest {
    // 当前密码：普通用户修改自己的密码时用于校验
    // 現在のパスワード：一般ユーザーが自分のパスワードを変更するときの確認用
    private String oldPassword;

    // 新密码：修改密码或管理员重置密码时使用
    // 新しいパスワード：パスワード変更または管理者リセット時に使う
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 12, message = "新密码长度必须是6到12位")
    private String newPassword;

    public PasswordRequest() {
    }

    public PasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "PasswordRequest{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
