package com.study.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordRequest {
    @NotBlank(message = "新密码不能为空")
    @Size(min=6, max=12,message = "新密码长度必须是6到12位")
    private String password;

    public PasswordRequest() {}
    public PasswordRequest(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
