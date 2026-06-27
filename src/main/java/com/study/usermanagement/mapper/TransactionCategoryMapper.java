package com.study.usermanagement.mapper;

import com.study.usermanagement.entity.TransactionCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransactionCategoryMapper {

    int insert(TransactionCategory category);

    List<TransactionCategory> findByUserId(@Param("userId") Integer userId);

    int updateByIdAndUserId(@Param("id") Integer id,
                            @Param("userId") Integer userId,
                            @Param("category") TransactionCategory category);

    int deleteByIdAndUserId(@Param("id") Integer id,
                            @Param("userId") Integer userId);

    TransactionCategory findByIdAndUserId(@Param("id") Integer id,
                                          @Param("userId") Integer userId);

    TransactionCategory findByUserIdAndNameAndType(
            @Param("userId") Integer userId,
            @Param("name") String name,
            @Param("type") String type
    );

    // 查询该用户是否已经初始化过默认分类，避免重复插入
    int countDefaultCategories(@Param("userId") Integer userId);

    int deleteByUserId(@Param("userId") Integer userId);
}
