package com.study.usermanagement.mapper;

import com.study.usermanagement.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    // 根据用户名查询用户；SQL 写在 UserMapper.xml 里
    User findByUsername(@Param("username") String username);

    // 根据 token 查询用户；SQL 写在 UserMapper.xml 里
    User findByToken(@Param("token") String token);

    // 查询所有用户
    List<User> findAll();

    // 新增用户
    int insert(User user);

    // 根据 username 修改 password
    int updatePassword(@Param("username") String username,
                       @Param("password") String password);

    // 根据 username 删除用户
    int deleteByUsername(@Param("username") String username);

    // 登录成功后保存新 token
    int updateToken(@Param("username") String username,
                    @Param("token") String token);

    // 根据 username 清空 token，表示退出登录
    int clearToken(@Param("username") String username);
}
