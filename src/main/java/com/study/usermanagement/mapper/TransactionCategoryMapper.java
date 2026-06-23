package com.study.usermanagement.mapper;

import com.study.usermanagement.entity.TransactionCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TransactionCategoryMapper {

    //新增收支分类
    int insert(TransactionCategory category);

    //查询当前登录用户自己的分类
    List<TransactionCategory> findByUserId(@Param("userId") Integer userId);

    //修改分类
    int updateByIdAndUserId(@Param("id") Integer id,
                            @Param("userId") Integer userId,
                            @Param("category") TransactionCategory category);


    //删除分类
    int deleteByIdAndUserId(@Param("id") Integer id,
                            @Param("userId") Integer userId);

    //分类id是否属于该用户
    TransactionCategory findByIdAndUserId(@Param("id") Integer id,
                                          @Param("userId") Integer userId);
}
