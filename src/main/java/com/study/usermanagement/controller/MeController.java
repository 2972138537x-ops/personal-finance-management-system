package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.dto.PasswordRequest;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
public class MeController {
    @Autowired
    private UserService userService;

    // 当前登录用户查看自己的信息，token 从请求头 Authorization 里取
    // ログイン中のユーザーが自分の情報を取得する。token は Authorization ヘッダーから受け取る
    @GetMapping
    public Result getMe(@RequestHeader("Authorization") String token) {
        return userService.findByToken(token);
    }

    // 当前登录用户修改自己的密码：用户从拦截器放进 request 的 currentUser 里取
    // ログイン中のユーザーが自分のパスワードを変更する。ユーザー情報は Interceptor が request に保存した currentUser から取得する
    @PutMapping("/password")
    public Result changeMyPassword(HttpServletRequest request, @RequestBody @Valid PasswordRequest passwordRequest) {
        User currentUser = (User) request.getAttribute("currentUser");
        return userService.changeMyPassword(currentUser.getUsername(), passwordRequest.getPassword());
    }


}
