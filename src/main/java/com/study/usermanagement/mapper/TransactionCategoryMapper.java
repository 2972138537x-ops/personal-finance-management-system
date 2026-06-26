package com.study.usermanagement.mapper;

import com.study.usermanagement.entity.TransactionCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
// 收支分类 Mapper：定义 transaction_category 表的数据库操作
// 収支カテゴリ Mapper：transaction_category テーブルのDB操作を定義する
public interface TransactionCategoryMapper {

    // 新增收支分类
    // 収支カテゴリを追加する
    int insert(TransactionCategory category);

    // 查询当前登录用户自己的分类
    // ログイン中ユーザー本人のカテゴリを取得する
    List<TransactionCategory> findByUserId(@Param("userId") Integer userId);

    // 修改分类：同时用 id 和 userId 限制，避免修改别人的分类
    // カテゴリ更新：id と userId の両方で制限し、他人のカテゴリ更新を防ぐ
    int updateByIdAndUserId(@Param("id") Integer id,
                            @Param("userId") Integer userId,
                            @Param("category") TransactionCategory category);


    // 删除分类：同时用 id 和 userId 限制，避免删除别人的分类
    // カテゴリ削除：id と userId の両方で制限し、他人のカテゴリ削除を防ぐ
    int deleteByIdAndUserId(@Param("id") Integer id,
                            @Param("userId") Integer userId);

    // 判断分类 id 是否属于该用户
    // カテゴリ id がそのユーザーに属するか確認する
    TransactionCategory findByIdAndUserId(@Param("id") Integer id,
                                          @Param("userId") Integer userId);

    // 新增或修改分类时，检查当前用户是否已经有同名同类型分类
    // カテゴリ追加・更新時、同じユーザーに同名・同タイプのカテゴリがあるか確認する
    TransactionCategory findByUserIdAndNameAndType(
            @Param("userId") Integer userId,
            @Param("name") String name,
            @Param("type") String type
    );

    // 注销账号时删除当前用户的全部收支分类
    // アカウント退会時、現在ユーザーの全収支カテゴリを削除する
    int deleteByUserId(@Param("userId") Integer userId);
}
