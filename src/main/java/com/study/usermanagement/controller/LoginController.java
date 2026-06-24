package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    // 用户登录：账号密码正确时，Service 会生成 token 并返回给前端
    // ログイン：ユーザー名とパスワードが正しい場合、Service が token を生成して返す
    @PostMapping("/login")
    public Result login(@RequestBody @Valid User user) {
        return userService.login(user);
    }

    // 退出登录：从 request 中取出拦截器保存的 currentUser，然后清空 token
    // ログアウト：Interceptor が request に保存した currentUser を取り出し、token をクリアする
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        return userService.logout(currentUser.getUsername());
    }
}
