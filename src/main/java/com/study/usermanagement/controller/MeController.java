package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
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
    public Result getMe(@RequestHeader("Authorization") String token){
        return userService.findByToken(token);
    }

    @PutMapping("/password")
    public Result changeMyPassword(HttpServletRequest request,@RequestBody @Valid User user){
        User currentUser = (User) request.getAttribute("currentUser");
        return userService.changePassword(currentUser.getUsername(), user);
    }


}
