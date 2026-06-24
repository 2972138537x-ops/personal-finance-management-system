package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.TransactionStatsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction-stats")
public class TransactionStatsController {
    @Autowired
    private TransactionStatsService transactionStatsService;

    // 月度总览：统计当前登录用户某月的总收入、总支出和结余
    // 月次サマリー：ログイン中ユーザーの指定月の収入合計、支出合計、残高を集計する
    @GetMapping("/month")
    public Result getMonthlyStats(HttpServletRequest request,
                                  @RequestParam Integer year,
                                  @RequestParam Integer month) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionStatsService.getMonthlyStats(userId, year, month);
    }

    // 分类统计：按收入或支出类型，统计某月每个分类的总金额
    // カテゴリ別集計：収入または支出タイプごとに、指定月の各カテゴリ合計金額を集計する
    @GetMapping("/category/{type}")
    public Result getStatsByCategoryAndType(HttpServletRequest request,
                                            @PathVariable String type,
                                            @RequestParam Integer year,
                                            @RequestParam Integer month) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionStatsService.getStatsByCategoryAndType(userId, type, year, month);
    }

    // 类型总额：统计某月 income 或 expense 的总金额
    // タイプ別合計：指定月の income または expense の合計金額を集計する
    @GetMapping("/type-total")
    public Result getTypeTotal(HttpServletRequest request,
                               @RequestParam String type,
                               @RequestParam Integer year,
                               @RequestParam Integer month) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionStatsService.sumAmountByMonthAndType(userId, type, year, month);
    }

}
