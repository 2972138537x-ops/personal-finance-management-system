package com.study.usermanagement.mapper;

import com.study.usermanagement.entity.TransactionCategory;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TransactionCategoryMapper {

    //新增收支分类
    int insert(TransactionCategory category);


}
