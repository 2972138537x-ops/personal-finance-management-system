package com.study.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordRequest {
    @NotBlank(message = "新密码不能为空")
    @Size(min=6, max=12,message = "新密码长度必须是6到12位")
    private String password;

    // 无参构造方法：Spring Boot 把 JSON 转成 Java 对象时需要用
    public PasswordRequest() {}

    // 有参构造方法：方便自己 new PasswordRequest("123456")
    public PasswordRequest(String password) {
        this.password = password;
    }

    // getter：让外部代码读取 password
    public String getPassword() {
        return password;
    }

    // setter：Spring Boot 接收 JSON 时，会通过它把 password 放进对象
    public void setPassword(String password) {
        this.password = password;
    }
}
