package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.dto.PasswordRequest;
import com.study.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "管理员用户管理", description = "管理员查看用户、查询指定用户、重置密码和删除用户的接口")
public class AdminUserController {
    @Autowired
    private UserService userService;

    // 管理员查询全部用户
    // 管理者が全ユーザーを取得する
    @Operation(summary = "查询全部用户", description = "管理员查看系统中的全部用户信息，只有 ADMIN 角色可以访问。")
    @GetMapping
    public Result getAll() {
        return userService.findAll();
    }

    // 管理员根据路径里的 username 查询单个用户
    // パス内の username を使って、管理者が単一ユーザーを取得する
    @Operation(summary = "查询指定用户", description = "管理员根据用户名查询某一个用户的信息。")
    @GetMapping("/{username}")
    public Result findByUsername(@Parameter(description = "要查询的用户名")
                                 @PathVariable String username) {
        return userService.findByUsername(username);
    }

    // 管理员重置指定用户密码：username 来自路径，password 来自请求体
    // 管理者が指定ユーザーのパスワードをリセットする。username はパス、password はリクエストボディから受け取る
    @Operation(summary = "重置指定用户密码", description = "管理员根据用户名重置指定用户的密码，新密码从请求体中读取。")
    @PutMapping("/{username}/password")
    public Result resetPassword(@Parameter(description = "要重置密码的用户名")
                                @PathVariable String username,
                                @RequestBody @Valid PasswordRequest passwordRequest) {
        return userService.resetPassword(username, passwordRequest.getNewPassword());
    }

    // 管理员根据路径里的 username 删除用户
    // パス内の username を使って、管理者がユーザーを削除する
    @Operation(summary = "删除指定用户", description = "管理员根据用户名删除指定用户。")
    @DeleteMapping("/{username}")
    public Result deleteByUsername(@Parameter(description = "要删除的用户名")
                                   @PathVariable String username) {
        return userService.deleteByUsername(username);
    }

}
