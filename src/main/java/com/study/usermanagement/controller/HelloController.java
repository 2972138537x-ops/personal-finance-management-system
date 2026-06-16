package com.study.usermanagement.controller;

import com.study.usermanagement.entity.User;
import org.springframework.web.bind.annotation.*;
import com.study.usermanagement.common.Result;

@RestController
public class HelloController {
    /*@GetMapping("/hello")
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
    public Result register(@RequestBody User user) {
        return new Result(true,"注册成功",user.getUsername());
    }

    //GET /course/{course}
    //返回：正在学习：course
    @GetMapping("/course/{course}")
    public String study(@PathVariable String course) {
        return "正在学习：" + course;
    }

    //GET /add?a=10&b=20
    //返回：30
    @GetMapping("/add")
    public int plusMethod(@RequestParam int a, @RequestParam int b) {
        return a + b;
    }
    //访问 /users/tom
    //返回一个 Result JSON
    //success: true
    //message: 查询成功
    //data: tom
    @GetMapping("/users/{username}")
    public Result findMethod(@PathVariable String username){
        return new Result(true,"查询成功",username);
    }

    //如果 username 是 tom，并且 password 是 123456
    //返回 Result(true, "登录成功", username)
    //
    //否则
    //返回 Result(false, "用户名或密码错误", null)
    @GetMapping("/users")
    public Result login(@RequestParam String username,@RequestParam String password){
        if(username.equals("tom")){
            if(password.equals("123456")){
                return new Result(true,"登录成功",username);
            }
        }
        return new Result(false,"用户名或密码错误",null);
    }
*/
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (user.getUsername().equals("tom") && user.getPassword().equals("123456")) {
            return new Result(true, "登录成功", user.getUsername());
        }
        return new Result(false, "用户名或密码错误", null);
    }
}

