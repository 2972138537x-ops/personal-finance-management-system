package com.study.usermanagement.service;


import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    //username 为空 -> 查询用户名不能为空
    //否则 -> 查询成功
    public Result findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new Result(false, "查询用户名不能为空", null);
        }
        return new Result(true, "查询成功", username);
    }

    //如果 password 长度小于 6 或大于 12
    //返回 Result(false, "密码长度必须是6到12位", null)
    //否则
    //返回 Result(true, "注册成功", user.getUsername())
    //如果 password 是 null
    //返回 Result(false, "密码不能为空", null)
    public Result register(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (username == null || username.isEmpty()) {
            return new Result(false, "用户名不能为空", null);
        }
        if (password == null || password.isEmpty()) {
            return new Result(false, "密码不能为空", null);
        }
        if (password.length() < 6 || password.length() > 12) {
            return new Result(false, "密码长度必须是6到12位", null);
        }
        return new Result(true, "注册成功", username);
    }

    //1. username 不能为空
    //2. 新密码 password 不能为空
    //3. 新密码长度必须是 6 到 12 位
    //4. 如果都没问题，返回修改密码成功
    public Result changePassword(String username, User user) {
        String password = user.getPassword();
        if (username == null || username.isEmpty()) {
            return new Result(false, "用户名不能为空", null);
        }
        if (password == null || password.isEmpty()) {
            return new Result(false, "新密码不能为空", null);
        }
        if (password.length() < 6 || password.length() > 12) {
            return new Result(false, "新密码长度必须是6到12位", null);
        }
        return new Result(true, "修改密码成功", username);
    }

    //username 为空 -> 删除用户名不能为空
    //否则 -> 删除成功
    public Result deleteByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new Result(false, "删除用户名不能为空", null);
        }
        return new Result(true, "删除成功", username);
    }
}
