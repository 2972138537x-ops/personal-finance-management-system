package com.study.usermanagement.dao;

import com.study.usermanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 查询所有用户，jdbcTemplate.query 会把每一行结果转换成一个 User 对象
    public List<User> findAll() {
        String sql = "SELECT username, password, role, token FROM `user` ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            user.setToken(rs.getString("token"));
            return user;
        });
    }

    // 根据 username 查询用户；查不到时返回 null
    public User findByUsername(String username) {
        String sql = "SELECT username, password, role, token FROM `user` WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            user.setToken(rs.getString("token"));
            return user;
        }, username);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    // 新增用户；默认 role 是 USER，register_date 使用数据库当前日期
    public int insert(User user) {
        String sql = "INSERT INTO `user`(username, password, role,  register_date) VALUES(?, ?, 'USER', CURDATE())";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }

    // 根据 username 修改 password；返回影响行数
    public int updatePassword(String username, String password) {
        String sql = "UPDATE `user` SET password = ? WHERE username = ? ";
        return jdbcTemplate.update(sql, password, username);
    }

    // 根据 username 删除用户；返回影响行数
    public int deleteByUsername(String username) {
        String sql = "DELETE FROM `user` WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }

    // 登录成功后，把新 token 保存到数据库
    public int updateToken(String username, String token) {
        String sql = "UPDATE `user` SET token = ? WHERE username = ?";
        return jdbcTemplate.update(sql, token, username);
    }

    // 根据 token 查询用户；拦截器用它判断登录状态是否有效
    public User findByToken(String token) {
        String sql = "SELECT username, password, role, token FROM `user` WHERE token = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            user.setToken(rs.getString("token"));
            return user;
        }, token);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }
}
