package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.TransactionRecord;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.TransactionRecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/transaction-records")
public class TransactionRecordController {
    @Autowired
    private TransactionRecordService transactionRecordService;

    //新增收支记录
    @PostMapping
    public Result addTransactionRecord(HttpServletRequest request, @RequestBody @Valid TransactionRecord record) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.addRecord(userId, record);
    }

    //查询当前登录用户自己的收支记录
    @GetMapping
    public Result findMyRecords(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserId(userId);
    }

    //修改自己的收支记录
    @PutMapping("/{id}")
    public Result updateMyRecord(@PathVariable Integer id, HttpServletRequest request, @RequestBody @Valid TransactionRecord record) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.updateByIdAndUserId(id, userId, record);
    }

    //删除自己的收支记录
    @DeleteMapping("/{id}")
    public Result deleteMyRecord(@PathVariable Integer id,
                                 HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.deleteByIdAndUserId(id, userId);
    }

    //按类型查询自己的收支记录
    @GetMapping("/type/{type}")
    public Result findMyRecordsByType(HttpServletRequest request,
                                      @PathVariable String type) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserIdAndType(userId, type);
    }

    //按日期范围查询自己的收支记录
    @GetMapping("/range")
    public Result findMyRecordsByDateRange(HttpServletRequest request,
                                           @RequestParam LocalDate startRecordDate,
                                           @RequestParam LocalDate endOfRecordDate) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserIdAndDateRange(userId, startRecordDate, endOfRecordDate);

    }

    //分类id 看看下面有哪些具体记录。按分类查询当前登录用户自己的收支记录
    @GetMapping("/category/{categoryId}")
    public Result findMyRecordsByCategoryId(HttpServletRequest request, @PathVariable Integer categoryId) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserIdAndCategoryId(userId, categoryId);
    }
}
