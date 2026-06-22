package com.study.usermanagement.controller;

import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.TransactionCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.study.usermanagement.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction-categories")
public class TransactionCategoryController {
    @Autowired
    private TransactionCategoryService transactionCategoryService;

    //新增分类
    @PostMapping
    public Result addCategory(HttpServletRequest request, @RequestBody @Valid TransactionCategory category) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.addCategory(userId, category);
    }

    //查询当前登录用户自己的分类
    @GetMapping
    public Result findMyCategories(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.findByUserId(userId);
    }

    //修改分类
    @PutMapping("/{id}")
    public Result updateMyCategory(@PathVariable Integer id,HttpServletRequest request, @RequestBody @Valid TransactionCategory category) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.updateByIdAndUserId(id, userId, category);
    }

    //删除分类
    @DeleteMapping("/{id}")
    public Result deleteMyCategory(@PathVariable Integer id,HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.deleteByIdAndUserId(id, userId);
    }

}
