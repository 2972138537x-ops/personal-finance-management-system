package com.study.usermanagement.controller;

import com.study.usermanagement.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello spring boot";
    }

    @GetMapping("/hello/{name}")
    public String helloName(@PathVariable String name) {
        return "hello " + name;
    }

    @GetMapping("/search")
    public String helloJava(@RequestParam String course, @RequestParam String level) {
        return "课程" + course + " " + level;
    }

    @PostMapping("/users")
    public String register(@RequestBody User user) {
        return "注册用户： " + user.getUsername();
    }
}

