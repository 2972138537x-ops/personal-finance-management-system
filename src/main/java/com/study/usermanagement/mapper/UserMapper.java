package com.study.usermanagement.mapper;

import com.study.usermanagement.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    // 根据用户名查询用户；SQL 写在 UserMapper.xml 里
    // ユーザー名でユーザーを検索する。SQL は UserMapper.xml に書く
    User findByUsername(@Param("username") String username);

    // 根据 token 查询用户；SQL 写在 UserMapper.xml 里
    // token でユーザーを検索する。SQL は UserMapper.xml に書く
    User findByToken(@Param("token") String token);

    // 查询所有用户
    // 全ユーザーを取得する
    List<User> findAll();

    // 新增用户
    // ユーザーを新規登録する
    int insert(User user);

    // 根据 username 修改 password
    // username を条件に password を更新する
    int updatePassword(@Param("username") String username,
                       @Param("password") String password);

    // 根据 username 删除用户
    // username を条件にユーザーを削除する
    int deleteByUsername(@Param("username") String username);

    // 登录成功后保存新 token
    // ログイン成功後、新しい token を保存する
    int updateToken(@Param("username") String username,
                    @Param("token") String token);

    // 根据 username 清空 token，表示退出登录
    // username を条件に token を NULL にし、ログアウト状態にする
    int clearToken(@Param("username") String username);
}
