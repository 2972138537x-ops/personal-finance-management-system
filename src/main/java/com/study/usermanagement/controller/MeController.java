package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.dto.PasswordRequest;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@Tag(name = "当前用户", description = "处理当前登录用户查看个人信息和修改自己密码的接口")
public class MeController {
    @Autowired
    private UserService userService;

    // 当前登录用户查看自己的信息，token 从请求头 Authorization 里取
    // ログイン中のユーザーが自分の情報を取得する。token は Authorization ヘッダーから受け取る
    @Operation(summary = "查看当前用户信息", description = "根据 Authorization 请求头中的 token 查询当前登录用户的个人信息。")
    @GetMapping
    public Result getMe(@Parameter(description = "登录成功后返回的 token，需要放在 Authorization 请求头中")
                        @RequestHeader("Authorization") String token) {
        return userService.findByToken(token);
    }

    // 当前登录用户修改自己的密码：用户从拦截器放进 request 的 currentUser 里取
    // ログイン中のユーザーが自分のパスワードを変更する。ユーザー情報は Interceptor が request に保存した currentUser から取得する
    @Operation(summary = "修改当前用户密码", description = "当前登录用户修改自己的密码，用户身份由 token 和拦截器确认，不需要在路径中传 username。")
    @PutMapping("/password")
    public Result changeMyPassword(HttpServletRequest request, @RequestBody @Valid PasswordRequest passwordRequest) {
        User currentUser = (User) request.getAttribute("currentUser");
        return userService.changeMyPassword(currentUser.getUsername(), passwordRequest.getOldPassword(), passwordRequest.getNewPassword());
    }


}
