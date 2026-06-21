package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.dto.PasswordRequest;
import com.study.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    @Autowired
    private UserService userService;

    // 管理员查询全部用户
    @GetMapping
    public Result getAll(){
        return userService.findAll();
    }

    // 管理员根据路径里的 username 查询单个用户
    @GetMapping("/{username}")
    public Result findByUsername(@PathVariable String username){
        return userService.findByUsername(username);
    }

    // 管理员重置指定用户密码：username 来自路径，password 来自请求体
    @PutMapping("/{username}/password")
    public Result resetPassword(@PathVariable String username, @RequestBody @Valid PasswordRequest passwordRequest){
        return userService.changeMyPassword(username,passwordRequest.getPassword());
    }

    // 管理员根据路径里的 username 删除用户
    @DeleteMapping("/{username}")
    public Result deleteByUsername(@PathVariable String username){
        return userService.deleteByUsername(username);
    }

}
