package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "登录认证", description = "处理用户登录、退出登录和 token 登录状态相关接口")
public class LoginController {
    @Autowired
    private UserService userService;

    // 用户登录：账号密码正确时，Service 会生成 token 并返回给前端
    // ログイン：ユーザー名とパスワードが正しい場合、Service が token を生成して返す
    @Operation(summary = "用户登录", description = "根据用户名和密码进行登录，登录成功后返回 token，后续需要登录的接口要在 Authorization 请求头中携带该 token。")
    @PostMapping("/login")
    public Result login(@RequestBody @Valid User user) {
        return userService.login(user);
    }

    // 退出登录：从 request 中取出拦截器保存的 currentUser，然后清空 token
    // ログアウト：Interceptor が request に保存した currentUser を取り出し、token をクリアする
    @Operation(summary = "退出登录", description = "当前登录用户退出系统，后端会清空该用户保存在数据库中的 token，使旧 token 失效。")
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        return userService.logout(currentUser.getUsername());
    }
}
