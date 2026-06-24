package com.study.usermanagement.controller;

import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.TransactionCategoryService;
import com.study.usermanagement.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction-categories")
public class TransactionCategoryController {
    @Autowired
    private TransactionCategoryService transactionCategoryService;

    // 新增当前登录用户自己的收支分类
    // ログイン中ユーザー本人の収支カテゴリを追加する
    @PostMapping
    public Result addCategory(HttpServletRequest request, @RequestBody @Valid TransactionCategory category) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.addCategory(userId, category);
    }

    // 查询当前登录用户自己的全部分类
    // ログイン中ユーザー本人の全カテゴリを取得する
    @GetMapping
    public Result findMyCategories(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.findByUserId(userId);
    }

    // 修改当前登录用户自己的分类
    // ログイン中ユーザー本人のカテゴリを更新する
    @PutMapping("/{id}")
    public Result updateMyCategory(@PathVariable Integer id, HttpServletRequest request, @RequestBody @Valid TransactionCategory category) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.updateByIdAndUserId(id, userId, category);
    }

    // 删除当前登录用户自己的分类
    // ログイン中ユーザー本人のカテゴリを削除する
    @DeleteMapping("/{id}")
    public Result deleteMyCategory(@PathVariable Integer id, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.deleteByIdAndUserId(id, userId);
    }

}
