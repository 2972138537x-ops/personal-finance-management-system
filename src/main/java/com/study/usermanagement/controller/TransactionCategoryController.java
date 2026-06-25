package com.study.usermanagement.controller;

import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.TransactionCategoryService;
import com.study.usermanagement.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction-categories")
@Tag(name = "收支分类", description = "当前登录用户维护自己的收入和支出分类")
public class TransactionCategoryController {
    @Autowired
    private TransactionCategoryService transactionCategoryService;

    // 新增当前登录用户自己的收支分类
    // ログイン中ユーザー本人の収支カテゴリを追加する
    @Operation(summary = "新增收支分类", description = "为当前登录用户新增收入或支出分类，userId 由 token 对应的当前用户决定，前端不需要传 userId。")
    @PostMapping
    public Result addCategory(HttpServletRequest request, @RequestBody @Valid TransactionCategory category) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.addCategory(userId, category);
    }

    // 查询当前登录用户自己的全部分类
    // ログイン中ユーザー本人の全カテゴリを取得する
    @Operation(summary = "查询我的全部分类", description = "查询当前登录用户自己的全部收支分类。")
    @GetMapping
    public Result findMyCategories(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.findByUserId(userId);
    }

    // 修改当前登录用户自己的分类
    // ログイン中ユーザー本人のカテゴリを更新する
    @Operation(summary = "修改我的分类", description = "根据分类 id 修改当前登录用户自己的分类，只能修改属于自己的分类。")
    @PutMapping("/{id}")
    public Result updateMyCategory(@Parameter(description = "要修改的分类 id")
                                   @PathVariable Integer id,
                                   HttpServletRequest request,
                                   @RequestBody @Valid TransactionCategory category) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.updateByIdAndUserId(id, userId, category);
    }

    // 删除当前登录用户自己的分类
    // ログイン中ユーザー本人のカテゴリを削除する
    @Operation(summary = "删除我的分类", description = "根据分类 id 删除当前登录用户自己的分类，只能删除属于自己的分类。")
    @DeleteMapping("/{id}")
    public Result deleteMyCategory(@Parameter(description = "要删除的分类 id")
                                   @PathVariable Integer id,
                                   HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionCategoryService.deleteByIdAndUserId(id, userId);
    }

}
