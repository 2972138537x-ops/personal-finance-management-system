package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    //GET    /users/{username}
    @GetMapping("/{username}")
    public Result searchUsername(@PathVariable String username) {
        return new Result(true, "查询成功", username);
    }

    //POST   /users
    @PostMapping
    public Result register(@RequestBody User user) {
        return new Result(true, "注册成功", user.getUsername());
    }

    //PUT    /users/{username}/password
    @PutMapping("/{username}/password")
    public Result changePassword(@PathVariable String username, @RequestBody User user) {
        return new Result(true, "修改密码成功", username);
    }

    //DELETE /users/{username}
    @DeleteMapping("/{username}")
    public Result deleteUser(@PathVariable String username) {
        return new Result(true, "删除成功", username);
    }
}
