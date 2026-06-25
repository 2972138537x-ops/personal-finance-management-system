package com.study.usermanagement.mapper;

import com.study.usermanagement.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
// 收支记录 Mapper：定义 transaction_record 表的数据库操作
// 収支記録 Mapper：transaction_record テーブルのDB操作を定義する
public interface TransactionRecordMapper {

    // 新增收支记录
    // 収支記録を追加する
    int insert(TransactionRecord record);

    // 查询当前登录用户自己的全部收支记录
    // ログイン中ユーザー本人の全収支記録を取得する
    List<TransactionRecord> findByUserId(@Param("userId") Integer userId);

    // 修改当前登录用户自己的收支记录
    // ログイン中ユーザー本人の収支記録を更新する
    int updateByIdAndUserId(@Param("id") Integer id,
                            @Param("userId") Integer userId,
                            @Param("record") TransactionRecord record);

    // 删除当前登录用户自己的收支记录
    // ログイン中ユーザー本人の収支記録を削除する
    int deleteByIdAndUserId(@Param("id") Integer id,
                            @Param("userId") Integer userId);

    // 按类型查询自己的收支记录
    // タイプ別に本人の収支記録を取得する
    List<TransactionRecord> findByUserIdAndType(@Param("userId") Integer userId,
                                                @Param("type") String type);

    // 按日期范围查询自己的收支记录
    // 日付範囲で本人の収支記録を取得する
    List<TransactionRecord> findByUserIdAndDateRange(@Param("userId") Integer userId,
                                                     @Param("startRecordDate") LocalDate startRecordDate,
                                                     @Param("endOfRecordDate") LocalDate endOfRecordDate);

    // 按分类 id 查询该分类下的具体收支记录
    // カテゴリIDで、そのカテゴリに属する本人の収支記録を取得する
    List<TransactionRecord> findByUserIdAndCategoryId(@Param("userId") Integer userId,
                                                      @Param("categoryId") Integer categoryId);
    // 根据页码和每页条数查询当前用户的收支记录
    // ページ番号と件数に基づき、現在ユーザーの収支記録を取得する
    List<TransactionRecord> findByUserIdPage(@Param("userId") Integer userId,
                                             @Param("offset") Integer offset,
                                             @Param("size") Integer size);
    // 查询当前用户一共有多少条收支记录
    // 現在ユーザーの収支記録総件数を取得する
    Integer countByUserId(@Param("userId") Integer userId);

    // 根据多种条件筛选并分页查询
    // 複数条件で絞り込み、ページング検索する
    List<TransactionRecord> searchByUserIdPage(@Param("userId") Integer userId,
                                               @Param("type") String type,
                                               @Param("categoryId") Integer categoryId,
                                               @Param("startRecordDate") LocalDate startRecordDate,
                                               @Param("endOfRecordDate") LocalDate endOfRecordDate,
                                               @Param("offset") Integer offset,
                                               @Param("size") Integer size);

    // 查询组合条件筛选后的总条数
    // 複合条件で絞り込んだ後の総件数を取得する
    Integer countSearchByUserId(@Param("userId") Integer userId,
                                @Param("type") String type,
                                @Param("categoryId") Integer categoryId,
                                @Param("startRecordDate") LocalDate startRecordDate,
                                @Param("endOfRecordDate") LocalDate endOfRecordDate);
}
