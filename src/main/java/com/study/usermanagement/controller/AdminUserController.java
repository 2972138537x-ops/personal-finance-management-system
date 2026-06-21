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

    @GetMapping
    public Result getAll(){
        return userService.findAll();
    }

    @GetMapping("/{username}")
    public Result findByUsername(@PathVariable String username){
        return userService.findByUsername(username);
    }

    @PutMapping("/{username}/password")
    public Result resetPassword(@PathVariable String username, @RequestBody @Valid PasswordRequest passwordRequest){
        return userService.changeMyPassword(username,passwordRequest.getPassword());
    }

    @DeleteMapping("/{username}")
    public Result deleteByUsername(@PathVariable String username){
        return userService.deleteByUsername(username);
    }

}
