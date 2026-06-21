package com.study.usermanagement.service;


import com.study.usermanagement.common.Result;
import com.study.usermanagement.dao.UserDao;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.vo.LoginVO;
import com.study.usermanagement.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    //username 为空 -> 查询用户名不能为空
    //否则 -> 查询成功
    // 根据用户名查询用户；返回 UserVO，避免把 password 返回给前端
    public Result findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new Result(false, "查询用户名不能为空", null);
        }
        User user = userDao.findByUsername(username);
        if (user == null) {
            return new Result(false, "没有该用户", null);
        }
        return new Result(true, "查询成功", new UserVO(user.getUsername(), user.getRole()));
    }

    //如果 password 长度小于 6 或大于 12
    //返回 Result(false, "密码长度必须是6到12位", null)
    //否则
    //返回 Result(true, "注册成功", user.getUsername())
    //如果 password 是 null
    //返回 Result(false, "密码不能为空", null)
    // 注册用户：校验用户名和密码，再判断用户名是否已存在，最后插入数据库
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
        User oldUser = userDao.findByUsername(username);
        if (oldUser != null) {
            return new Result(false, "用户名已存在", null);
        }
        int rows = userDao.insert(user);
        if (rows > 0) {
            return new Result(true, "注册成功", username);
        }
        return new Result(false, "注册失败", null);
    }

    //1. username 不能为空
    //2. 新密码 password 不能为空
    //3. 新密码长度必须是 6 到 12 位
    //4. 如果都没问题，返回修改密码成功
    // 修改密码旧写法：从 User 对象里取 password，目前主要保留作学习对比
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
        User oldUser = userDao.findByUsername(username);
        if (oldUser == null) {
            return new Result(false, "该用户名不存在", null);
        }
        int rows = userDao.updatePassword(username, password);
        if (rows > 0) {
            return new Result(true, "修改密码成功", username);
        }
        return new Result(false, "修改失败", null);
    }

    // 修改密码新写法：username 表示要修改谁，newPassword 表示新密码
    public Result changeMyPassword(String username, String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            return new Result(false, "新密码不能为空", null);
        }
        if (newPassword.length() < 6 || newPassword.length() > 12) {
            return new Result(false, "新密码长度必须是6到12位", null);
        }

        User oldUser = userDao.findByUsername(username);
        if (oldUser == null) {
            return new Result(false, "该用户名不存在", null);
        }

        int rows = userDao.updatePassword(username, newPassword);
        if (rows > 0) {
            return new Result(true, "修改密码成功", username);
        }
        return new Result(false, "修改失败", null);
    }

    //username 为空 -> 删除用户名不能为空
    //否则 -> 删除成功
    // 删除用户：先确认用户存在，再执行 delete
    public Result deleteByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new Result(false, "删除用户名不能为空", null);
        }
        User oldUser = userDao.findByUsername(username);
        if (oldUser == null) {
            return new Result(false, "该用户名不存在", null);
        }
        int rows = userDao.deleteByUsername(username);
        if (rows > 0) {
            return new Result(true, "删除成功", username);
        }
        return new Result(false, "删除失败", null);
    }

    // 查询全部用户：把 User 列表转换成 UserVO 列表，避免返回密码
    public Result findAll() {
        List<User> users = userDao.findAll();
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : users) {
            userVOList.add(new UserVO(user.getUsername(),user.getRole()));
        }
        return new Result(true, "查询成功", userVOList);
    }

    // 登录：校验账号密码，成功后生成 token，保存到数据库并返回 LoginVO
    public Result login(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (username == null || username.isEmpty()) {
            return new Result(false, "用户名不能为空", null);
        }
        if (password == null || password.isEmpty()) {
            return new Result(false, "密码不能为空", null);
        }
        User user1 = userDao.findByUsername(username);
        if (user1 != null) {
            if (!password.equals(user1.getPassword())) {
                return new Result(false, "密码错误", null);
            }
            String token = UUID.randomUUID().toString();
            userDao.updateToken(username, token);
            LoginVO loginVO = new LoginVO(user1.getUsername(), user1.getRole(), token);
            return new Result(true, "登录成功", loginVO);
        }
        return new Result(false, "用户不存在", null);
    }

    // 根据 token 查询当前登录用户，常用于 /me 接口
    public Result findByToken(String token) {
        if (token == null || token.isEmpty()) {
            return new Result(false, "请先登录", null);
        }
        User user = userDao.findByToken(token);
        if (user == null) {
            return new Result(false, "登录状态无效，请重新登录", null);
        }

        return new Result(true, "查询成功", new UserVO(user.getUsername(), user.getRole()));
    }

    // 退出登录：清空当前用户的 token
    public Result logout(String username) {
        int rows = userDao.clearToken(username);

        if (rows > 0) {
            return new Result(true, "退出登录成功", null);
        }

        return new Result(false, "退出登录失败", null);
    }
}
