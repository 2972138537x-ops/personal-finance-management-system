package com.study.usermanagement.service;


import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.mapper.UserMapper;
import com.study.usermanagement.vo.LoginVO;
import com.study.usermanagement.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
// 用户业务层：处理注册、登录、退出、密码修改和管理员用户管理
// ユーザー業務層：登録、ログイン、ログアウト、パスワード変更、管理者のユーザー管理を処理する
public class UserService {
    @Autowired
    private UserMapper userMapper;

    // 根据用户名查询用户；返回 UserVO，避免把 password 返回给前端
    // ユーザー名でユーザーを検索する。password を返さないように UserVO に変換する
    public Result findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new Result(false, "查询用户名不能为空", null);
        }
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return new Result(false, "没有该用户", null);
        }
        return new Result(true, "查询成功", new UserVO(user.getUsername(), user.getRole()));
    }

    // 注册用户：校验用户名和密码，再判断用户名是否已存在，最后插入数据库
    // ユーザー登録：ユーザー名とパスワードを検証し、重複を確認してから DB に登録する
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
        User oldUser = userMapper.findByUsername(username);
        if (oldUser != null) {
            return new Result(false, "用户名已存在", null);
        }
        int rows = userMapper.insert(user);
        if (rows > 0) {
            return new Result(true, "注册成功", username);
        }
        return new Result(false, "注册失败", null);
    }

    // 修改密码旧写法：从 User 对象里取 password，目前主要保留作学习对比
    // パスワード変更の旧実装：User オブジェクトから password を取得する。学習比較用として残している
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
        User oldUser = userMapper.findByUsername(username);
        if (oldUser == null) {
            return new Result(false, "该用户名不存在", null);
        }
        int rows = userMapper.updatePassword(username, password);
        if (rows > 0) {
            return new Result(true, "修改密码成功", username);
        }
        return new Result(false, "修改失败", null);
    }

    // 修改密码新写法：username 表示要修改谁，newPassword 表示新密码
    // パスワード変更の新実装：username は対象ユーザー、newPassword は新しいパスワードを表す
    public Result changeMyPassword(String username, String oldPassword, String newPassword) {
        if (oldPassword == null || oldPassword.isEmpty()) {
            return new Result(false, "当前密码不能为空", null);
        }
        if (newPassword == null || newPassword.isEmpty()) {
            return new Result(false, "新密码不能为空", null);
        }
        if (newPassword.length() < 6 || newPassword.length() > 12) {
            return new Result(false, "新密码长度必须是6到12位", null);
        }

        User oldUser = userMapper.findByUsername(username);
        if (oldUser == null) {
            return new Result(false, "该用户名不存在", null);
        }
        if (!oldUser.getPassword().equals(oldPassword)) {
            return new Result(false, "当前密码不正确", null);
        }
        if (oldUser.getPassword().equals(newPassword)) {
            return new Result(false, "新密码不能和当前密码相同", null);
        }
        int rows = userMapper.updatePassword(username, newPassword);
        if (rows > 0) {
            return new Result(true, "修改密码成功", username);
        }
        return new Result(false, "修改失败", null);
    }

    // 管理员重置密码：不需要旧密码，但要确认目标用户存在并校验新密码
    // 管理者のパスワードリセット：旧パスワードは不要だが、対象ユーザーの存在と新パスワードを確認する
    public Result resetPassword(String username, String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            return new Result(false, "新密码不能为空", null);
        }

        if (newPassword.length() < 6 || newPassword.length() > 12) {
            return new Result(false, "新密码长度必须是6到12位", null);
        }

        User oldUser = userMapper.findByUsername(username);
        if (oldUser == null) {
            return new Result(false, "该用户名不存在", null);
        }

        if (oldUser.getPassword().equals(newPassword)) {
            return new Result(false, "新密码不能和原密码相同", null);
        }

        int rows = userMapper.updatePassword(username, newPassword);
        if (rows > 0) {
            return new Result(true, "重置密码成功", username);
        }

        return new Result(false, "重置密码失败", null);
    }

    // 删除用户：先确认用户存在，再执行 delete
    // ユーザー削除：対象ユーザーが存在することを確認してから delete を実行する
    public Result deleteByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new Result(false, "删除用户名不能为空", null);
        }
        User oldUser = userMapper.findByUsername(username);
        if (oldUser == null) {
            return new Result(false, "该用户名不存在", null);
        }
        int rows = userMapper.deleteByUsername(username);
        if (rows > 0) {
            return new Result(true, "删除成功", username);
        }
        return new Result(false, "删除失败", null);
    }

    // 查询全部用户：把 User 列表转换成 UserVO 列表，避免返回密码
    // 全ユーザー取得：User のリストを UserVO に変換し、password を返さない
    public Result findAll() {
        List<User> users = userMapper.findAll();
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : users) {
            userVOList.add(new UserVO(user.getUsername(), user.getRole()));
        }
        return new Result(true, "查询成功", userVOList);
    }

    // 登录：校验账号密码，成功后生成 token，保存到数据库并返回 LoginVO
    // ログイン：ユーザー名とパスワードを検証し、成功したら token を生成して DB に保存し、LoginVO を返す
    public Result login(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (username == null || username.isEmpty()) {
            return new Result(false, "用户名不能为空", null);
        }
        if (password == null || password.isEmpty()) {
            return new Result(false, "密码不能为空", null);
        }
        User user1 = userMapper.findByUsername(username);
        if (user1 != null) {
            if (!password.equals(user1.getPassword())) {
                return new Result(false, "密码错误", null);
            }
            String token = UUID.randomUUID().toString();
            userMapper.updateToken(username, token);
            LoginVO loginVO = new LoginVO(user1.getUsername(), user1.getRole(), token);
            return new Result(true, "登录成功", loginVO);
        }
        return new Result(false, "用户不存在", null);
    }

    // 根据 token 查询当前登录用户，常用于 /me 接口
    // token で現在ログイン中のユーザーを検索する。主に /me API で使用する
    public Result findByToken(String token) {
        if (token == null || token.isEmpty()) {
            return new Result(false, "请先登录", null);
        }
        User user = userMapper.findByToken(token);
        if (user == null) {
            return new Result(false, "登录状态无效，请重新登录", null);
        }

        return new Result(true, "查询成功", new UserVO(user.getUsername(), user.getRole()));
    }

    // 退出登录：清空当前用户的 token
    // ログアウト：現在ユーザーの token をクリアする
    public Result logout(String username) {
        int rows = userMapper.clearToken(username);

        if (rows > 0) {
            return new Result(true, "退出登录成功", null);
        }

        return new Result(false, "退出登录失败", null);
    }
}
