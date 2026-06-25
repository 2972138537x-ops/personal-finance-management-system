package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.service.TransactionStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction-stats")
@Tag(name = "收支统计", description = "当前登录用户按月份、分类和类型查看收支统计结果")
public class TransactionStatsController {
    @Autowired
    private TransactionStatsService transactionStatsService;

    // 月度总览：统计当前登录用户某月的总收入、总支出和结余
    // 月次サマリー：ログイン中ユーザーの指定月の収入合計、支出合計、残高を集計する
    @Operation(summary = "查询月度收支总览", description = "统计当前登录用户某一年某一个月的收入合计、支出合计和结余。")
    @GetMapping("/month")
    public Result getMonthlyStats(HttpServletRequest request,
                                  @Parameter(description = "年份，例如 2026")
                                  @RequestParam Integer year,
                                  @Parameter(description = "月份，范围 1 到 12")
                                  @RequestParam Integer month) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionStatsService.getMonthlyStats(userId, year, month);
    }

    // 分类统计：按收入或支出类型，统计某月每个分类的总金额
    // カテゴリ別集計：収入または支出タイプごとに、指定月の各カテゴリ合計金額を集計する
    @Operation(summary = "按分类统计月度金额", description = "按 income 或 expense 类型，统计当前登录用户某个月每个分类的合计金额。")
    @GetMapping("/category/{type}")
    public Result getStatsByCategoryAndType(HttpServletRequest request,
                                            @Parameter(description = "收支类型：income 表示收入，expense 表示支出")
                                            @PathVariable String type,
                                            @Parameter(description = "年份，例如 2026")
                                            @RequestParam Integer year,
                                            @Parameter(description = "月份，范围 1 到 12")
                                            @RequestParam Integer month) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionStatsService.getStatsByCategoryAndType(userId, type, year, month);
    }

    // 类型总额：统计某月 income 或 expense 的总金额
    // タイプ別合計：指定月の income または expense の合計金額を集計する
    @Operation(summary = "统计某类型月度总金额", description = "统计当前登录用户某个月 income 或 expense 的总金额。")
    @GetMapping("/type-total")
    public Result getTypeTotal(HttpServletRequest request,
                               @Parameter(description = "收支类型：income 表示收入，expense 表示支出")
                               @RequestParam String type,
                               @Parameter(description = "年份，例如 2026")
                               @RequestParam Integer year,
                               @Parameter(description = "月份，范围 1 到 12")
                               @RequestParam Integer month) {
        User currentUser = (User) request.getAttribute("currentUser");
        Integer userId = currentUser.getId();
        return transactionStatsService.sumAmountByMonthAndType(userId, type, year, month);
    }

}
