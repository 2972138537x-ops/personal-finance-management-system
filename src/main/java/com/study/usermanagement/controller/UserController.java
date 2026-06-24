package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // 注册用户：从 JSON 请求体接收 username/password，并触发参数校验
    // ユーザー登録：JSONリクエストボディから username/password を受け取り、入力チェックを行う
    @PostMapping
    public Result register(@RequestBody @Valid User user) {
        return userService.register(user);
    }
}
