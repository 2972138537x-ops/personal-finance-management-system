package com.study.usermanagement.service;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.mapper.TransactionCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionCategoryService {
    @Autowired
    private TransactionCategoryMapper transactionCategoryMapper;

    //添加收支分类
    public Result addCategory(TransactionCategory category) {
        Integer userId = category.getUserId();
        String name = category.getName();
        String type = category.getType();
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if (name == null || name.isEmpty()) {
            return new Result(false, "分类名不能为空", null);
        }
        if (type == null || type.isEmpty()) {
            return new Result(false, "分类类型不能为空", null);
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            return new Result(false, "类型必须是 income 或者 expense", null);
        }
        int rows = transactionCategoryMapper.insert(category);
        if (rows > 0) {
            return new Result(true, "新增分类成功", name);
        }
        return new Result(false, "新增分类失败", null);

    }
}
