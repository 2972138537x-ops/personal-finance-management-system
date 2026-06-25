package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@Tag(name = "用户注册", description = "处理普通用户注册接口")
public class UserController {
    @Autowired
    private UserService userService;

    // 注册用户：从 JSON 请求体接收 username/password，并触发参数校验
    // ユーザー登録：JSONリクエストボディから username/password を受け取り、入力チェックを行う
    @Operation(summary = "注册用户", description = "接收用户名和密码，创建一个新的普通用户账号，并触发参数校验。")
    @PostMapping
    public Result register(@RequestBody @Valid User user) {
        return userService.register(user);
    }
}
