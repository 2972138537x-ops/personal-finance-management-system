package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.study.usermanagement.service.UserService;
@RestController
@RequestMapping("/me")
public class MeController {
    @Autowired
    private  UserService userService;

    @GetMapping
    public Result getMe(@RequestParam String username){
        return userService.findByUsername(username);
    }

    @PutMapping("/password")
    public Result changeMyPassword(@RequestParam String username,@RequestBody @Valid User user){
        return userService.changePassword(username,user);
    }


}
