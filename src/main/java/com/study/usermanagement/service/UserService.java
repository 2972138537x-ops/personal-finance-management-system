package com.study.usermanagement.service;


import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.exception.BusinessException;
import com.study.usermanagement.mapper.TransactionCategoryMapper;
import com.study.usermanagement.mapper.TransactionRecordMapper;
import com.study.usermanagement.mapper.UserMapper;
import com.study.usermanagement.vo.LoginVO;
import com.study.usermanagement.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
// 用户业务层：处理注册、登录、退出、密码修改和管理员用户管理
// ユーザー業務層：登録、ログイン、ログアウト、パスワード変更、管理者のユーザー管理を処理する
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    @Autowired
    private TransactionCategoryMapper transactionCategoryMapper;

    @Autowired
    private TransactionCategoryService transactionCategoryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 根据用户名查询用户；返回 UserVO，避免把 password 返回给前端
    // ユーザー名でユーザーを検索する。password を返さないように UserVO に変換する
    public Result findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new BusinessException("查询用户名不能为空");
        }
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException("没有该用户");
        }
        return new Result(true, "查询成功", new UserVO(user.getUsername(), user.getRole()));
    }

    // 注册用户：校验用户名和密码，再判断用户名是否已存在，最后插入数据库
    // ユーザー登録：ユーザー名とパスワードを検証し、重複を確認してから DB に登録する
    @Transactional
    public Result register(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (username == null || username.isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        if (password.length() < 6 || password.length() > 12) {
            throw new BusinessException("密码长度必须是6到12位");
        }
        User oldUser = userMapper.findByUsername(username);
        if (oldUser != null) {
            throw new BusinessException("用户名已存在");
        }
        //对前端传来的密码 进行BCrypt哈希加密
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        int rows = userMapper.insert(user);
        if (rows > 0) {
            transactionCategoryService.createDefaultCategories(user.getId());
            return new Result(true, "注册成功", username);
        }
        throw new BusinessException("注册失败");
    }

    // 修改密码旧写法：从 User 对象里取 password，目前主要保留作学习对比
    // パスワード変更の旧実装：User オブジェクトから password を取得する。学習比較用として残している
    public Result changePassword(String username, User user) {
        String password = user.getPassword();
        if (username == null || username.isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            throw new BusinessException("新密码不能为空");
        }
        if (password.length() < 6 || password.length() > 12) {
            throw new BusinessException("新密码长度必须是6到12位");
        }
        User oldUser = userMapper.findByUsername(username);
        if (oldUser == null) {
            throw new BusinessException("该用户名不存在");
        }
        int rows = userMapper.updatePassword(username, password);
        if (rows > 0) {
            return new Result(true, "修改密码成功", username);
        }
        throw new BusinessException("修改失败");
    }

    // 修改密码新写法：username 表示要修改谁，newPassword 表示新密码
    // パスワード変更の新実装：username は対象ユーザー、newPassword は新しいパスワードを表す
    public Result changeMyPassword(String username, String oldPassword, String newPassword) {
        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new BusinessException("当前密码不能为空");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new BusinessException("新密码不能为空");
        }
        if (newPassword.length() < 6 || newPassword.length() > 12) {
            throw new BusinessException("新密码长度必须是6到12位");
        }

        User oldUser = userMapper.findByUsername(username);
        if (oldUser == null) {
            throw new BusinessException("该用户名不存在");
        }
        if (!passwordEncoder.matches(oldPassword, oldUser.getPassword())) {
            throw new BusinessException("当前密码不正确");
        }
        if (passwordEncoder.matches(newPassword, oldUser.getPassword())) {
            throw new BusinessException("新密码不能和当前密码相同");
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        int rows = userMapper.updatePassword(username, encodedPassword);
        if (rows > 0) {
            return new Result(true, "修改密码成功", username);
        }
        throw new BusinessException("修改失败");
    }

    // 管理员重置密码：不需要旧密码，但要确认目标用户存在并校验新密码
    // 管理者のパスワードリセット：旧パスワードは不要だが、対象ユーザーの存在と新パスワードを確認する
    public Result resetPassword(String username, String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            throw new BusinessException("新密码不能为空");
        }

        if (newPassword.length() < 6 || newPassword.length() > 12) {
            throw new BusinessException("新密码长度必须是6到12位");
        }

        User oldUser = userMapper.findByUsername(username);
        if (oldUser == null) {
            throw new BusinessException("该用户名不存在");
        }

        if (passwordEncoder.matches(newPassword, oldUser.getPassword())) {
            throw new BusinessException("新密码不能和原密码相同");
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        int rows = userMapper.updatePassword(username, encodedPassword);
        if (rows > 0) {
            return new Result(true, "重置密码成功", username);
        }

        throw new BusinessException("重置密码失败");
    }

    // 删除用户：先确认用户存在，再执行 delete
    // ユーザー削除：対象ユーザーが存在することを確認してから delete を実行する
    public Result deleteByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new BusinessException("删除用户名不能为空");
        }
        User oldUser = userMapper.findByUsername(username);
        if (oldUser == null) {
            throw new BusinessException("该用户名不存在");
        }
        int rows = userMapper.deleteByUsername(username);
        if (rows > 0) {
            return new Result(true, "删除成功", username);
        }
        throw new BusinessException("删除失败");
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
            throw new BusinessException("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        User user1 = userMapper.findByUsername(username);
        if (user1 != null) {
            // 业务失败时抛出 BusinessException，最终由 GlobalExceptionHandler 统一转成 Result
            // 業務エラーの場合は BusinessException を投げ、GlobalExceptionHandler で Result に変換する
            // BCrypt 哈希值匹配
            // BCrypt のハッシュ値を照合する
            if (!passwordEncoder.matches(password, user1.getPassword())) {
                throw new BusinessException("密码错误");
            }
            String token = UUID.randomUUID().toString();
            userMapper.updateToken(username, token);
            LoginVO loginVO = new LoginVO(user1.getUsername(), user1.getRole(), token);
            return new Result(true, "登录成功", loginVO);
        }
        throw new BusinessException("用户不存在");
    }

    // 根据 token 查询当前登录用户，常用于 /me 接口
    // token で現在ログイン中のユーザーを検索する。主に /me API で使用する
    public Result findByToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new BusinessException("请先登录");
        }
        User user = userMapper.findByToken(token);
        if (user == null) {
            throw new BusinessException("登录状态无效，请重新登录");
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

        throw new BusinessException("退出登录失败");
    }

    // 注销当前账号：先校验两遍密码和数据库密码，再删除自己的收支记录、分类和用户账号
    // アカウント退会：2回入力したパスワードとDBのパスワードを確認してから、本人の収支記録、カテゴリ、ユーザーアカウントを削除する
    @Transactional
    public Result deleteMe(User currentUser, String password, String confirmPassword) {
        if (currentUser == null) {
            throw new BusinessException("请先登录");
        }
        if (currentUser.getId() == null) {
            throw new BusinessException("用户信息无效");
        }
        if (password == null || password.isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            throw new BusinessException("确认密码不能为空");
        }
        if (!password.equals(confirmPassword)) {
            throw new BusinessException("两次输入的密码不一致");
        }

        User oldUser = userMapper.findByUsername(currentUser.getUsername());
        if (oldUser == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(password, oldUser.getPassword())) {
            throw new BusinessException("密码错误，不能注销账号");
        }

        transactionRecordMapper.deleteByUserId(currentUser.getId());
        transactionCategoryMapper.deleteByUserId(currentUser.getId());

        int rows = userMapper.deleteByUsername(currentUser.getUsername());
        if (rows > 0) {
            return new Result(true, "注销账号成功", currentUser.getUsername());
        }

        throw new BusinessException("注销账号失败");
    }
}
