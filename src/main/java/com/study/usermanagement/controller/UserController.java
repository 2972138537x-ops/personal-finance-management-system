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
    //GET    /users/{username}
    @GetMapping("/{username}")
    public Result searchUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    //POST   /users
    @PostMapping
    public Result register(@RequestBody @Valid User user) {
        return userService.register(user);
    }

    //PUT    /users/{username}/password
    @PutMapping("/{username}/password")
    public Result changePassword(@PathVariable String username, @RequestBody @Valid User user) {
        return userService.changePassword(username,user);
    }

    //DELETE /users/{username}
    @DeleteMapping("/{username}")
    public Result deleteUser(@PathVariable String username) {
        return userService.deleteByUsername(username);
    }

    @GetMapping
    public Result findAll() {
        return userService.findAll();
    }
}
