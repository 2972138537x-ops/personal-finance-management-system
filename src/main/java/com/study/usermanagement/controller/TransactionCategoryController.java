package com.study.usermanagement.controller;

import com.study.usermanagement.entity.TransactionCategory;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.TransactionCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.study.usermanagement.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction-categories")
public class TransactionCategoryController {
    @Autowired
    private TransactionCategoryService transactionCategoryService;

    //新增分类
    @PostMapping
    public Result addCategory(HttpServletRequest request, @RequestBody @Valid TransactionCategory category) {
        User currentUser = (User) request.getAttribute("currentUser");
        category.setUserId(currentUser.getId());
        return transactionCategoryService.addCategory(category);
    }


}
