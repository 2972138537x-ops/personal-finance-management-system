package com.study.usermanagement.service;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.mapper.TransactionCategoryMapper;
import com.study.usermanagement.vo.TransactionCategoryVO;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionCategoryService {
    @Autowired
    private TransactionCategoryMapper transactionCategoryMapper;

    //添加收支分类
    public Result addCategory(Integer userId, TransactionCategory category) {
        if (category == null) {
            return new Result(false, "请求体不能为空", null);
        }
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
        category.setUserId(userId);
        int rows = transactionCategoryMapper.insert(category);
        if (rows > 0) {
            return new Result(true, "新增分类成功", new TransactionCategoryVO(category.getId(), category.getName(), category.getType()));
        }
        return new Result(false, "新增分类失败", null);
    }

    //查询当前登录用户自己的分类
    public Result findByUserId(Integer userId) {
        if (userId == null) {
            return new Result(false, "登录状态异常，请重新登录", null);
        }
        List<TransactionCategory> categoryList = transactionCategoryMapper.findByUserId(userId);
        List<TransactionCategoryVO> categoryVOList = new ArrayList<>();
        for (TransactionCategory category : categoryList) {
            categoryVOList.add(new TransactionCategoryVO(category.getId(), category.getName(), category.getType()));
        }
        return new Result(true, "查询成功", categoryVOList);
    }

    //修改分类
    public Result updateByIdAndUserId(Integer id, Integer userId, TransactionCategory category) {
        if (category == null) {
            return new Result(false, "请求体不能为空", null);
        }
        String name = category.getName();
        String type = category.getType();
        if (id == null) {
            return new Result(false, "分类id不能为空", null);
        }
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
        int rows = transactionCategoryMapper.updateByIdAndUserId(id, userId, category);
        if (rows > 0) {
            return new Result(true, "修改成功", new TransactionCategoryVO(id, category.getName(), category.getType()));
        }
        return new Result(false, "修改失败", null);
    }

    //删除分类
    public Result deleteByIdAndUserId(Integer id, Integer userId) {
        if (id == null) {
            return new Result(false, "分类id不能为空", null);
        }
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        int rows = transactionCategoryMapper.deleteByIdAndUserId(id, userId);
        if (rows > 0) {
            return new Result(true, "删除成功", id);
        }
        return new Result(false, "删除失败", null);
    }
}
