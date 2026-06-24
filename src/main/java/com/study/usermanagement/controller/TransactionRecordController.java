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

    // 新增收支记录
    // 収支記録を追加する
    @PostMapping
    public Result addTransactionRecord(HttpServletRequest request, @RequestBody @Valid TransactionRecord record) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.addRecord(userId, record);
    }

    // 查询当前登录用户自己的全部收支记录
    // ログイン中ユーザー本人の全収支記録を取得する
    @GetMapping
    public Result findMyRecords(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserId(userId);
    }

    // 修改当前登录用户自己的收支记录
    // ログイン中ユーザー本人の収支記録を更新する
    @PutMapping("/{id}")
    public Result updateMyRecord(@PathVariable Integer id, HttpServletRequest request, @RequestBody @Valid TransactionRecord record) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.updateByIdAndUserId(id, userId, record);
    }

    // 删除当前登录用户自己的收支记录
    // ログイン中ユーザー本人の収支記録を削除する
    @DeleteMapping("/{id}")
    public Result deleteMyRecord(@PathVariable Integer id,
                                 HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.deleteByIdAndUserId(id, userId);
    }

    // 按类型查询自己的收支记录
    // タイプ別に本人の収支記録を取得する
    @GetMapping("/type/{type}")
    public Result findMyRecordsByType(HttpServletRequest request,
                                      @PathVariable String type) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserIdAndType(userId, type);
    }

    // 按日期范围查询自己的收支记录
    // 日付範囲で本人の収支記録を取得する
    @GetMapping("/range")
    public Result findMyRecordsByDateRange(HttpServletRequest request,
                                           @RequestParam LocalDate startRecordDate,
                                           @RequestParam LocalDate endOfRecordDate) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserIdAndDateRange(userId, startRecordDate, endOfRecordDate);
    }

    // 按分类 id 查询该分类下的具体收支记录
    // カテゴリIDで、そのカテゴリに属する本人の収支記録を取得する
    @GetMapping("/category/{categoryId}")
    public Result findMyRecordsByCategoryId(HttpServletRequest request, @PathVariable Integer categoryId) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionRecordService.findByUserIdAndCategoryId(userId, categoryId);
    }
}
