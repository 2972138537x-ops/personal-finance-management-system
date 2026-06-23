package com.study.usermanagement.mapper;

import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TransactionRecordMapper {

    //新增收支记录
    int insert(TransactionRecord record);

    //查询当前登录用户自己的收支记录
    List<TransactionRecord> findByUserId(@Param("userId") Integer userId);

    //修改自己的收支记录
    int updateByIdAndUserId(@Param("id") Integer id,
                            @Param("userId") Integer userId,
                            @Param("record") TransactionRecord record);

    //删除自己的收支记录
    int deleteByIdAndUserId(@Param("id") Integer id,
                            @Param("userId") Integer userId);

    //按类型查询自己的收支记录
    List<TransactionRecord> findByUserIdAndType(@Param("userId") Integer userId,
                                                @Param("type") String type);

    //按日期范围查询自己的收支记录
    List<TransactionRecord> findByUserIdAndDateRange(@Param("userId") Integer userId,
                                                     @Param("startRecordDate") LocalDate startRecordDate,
                                                     @Param("endOfRecordDate") LocalDate endOfRecordDate);



}
