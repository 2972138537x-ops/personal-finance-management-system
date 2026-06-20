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

    public List<User> findAll() {
        String sql = "SELECT username, password, role FROM `user` ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            return user;
        });
    }

    public User findByUsername(String username) {
        String sql = "SELECT username, password,role FROM `user` WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            return user;
        }, username);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    public int insert(User user) {
        String sql = "INSERT INTO `user`(username, password, role, register_date) VALUES(?, ?, 'USER', CURDATE())";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }

    public int updatePassword(String username, String password) {
        String sql = "UPDATE `user` SET password = ? WHERE username = ? ";
        return jdbcTemplate.update(sql, password, username);
    }

    public int deleteByUsername(String username) {
        String sql = "DELETE FROM `user` WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }
}
