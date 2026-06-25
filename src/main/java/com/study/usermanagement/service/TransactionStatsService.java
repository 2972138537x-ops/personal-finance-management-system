package com.study.usermanagement.service;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.mapper.TransactionStatsMapper;
import com.study.usermanagement.vo.MonthlyStatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
// 收支统计业务层：按月份、类型和分类统计当前用户的金额
// 収支集計業務層：ログイン中ユーザーの金額を月・タイプ・カテゴリ別に集計する
public class TransactionStatsService {
    @Autowired
    private TransactionStatsMapper transactionStatsMapper;

    // 按月份和类型统计金额：给月度总览复用
    // 月・タイプ別金額集計：月次サマリーで再利用する
    public Result sumAmountByMonthAndType(Integer userId, String type, Integer year, Integer month) {
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if (type == null || type.isEmpty()) {
            return new Result(false, "分类类型不能为空", null);
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            return new Result(false, "类型必须是 income 或者 expense", null);
        }
        if (year == null || year <= 0) {
            return new Result(false, "年份不能为空 且 不能小于零", null);
        }
        if (month == null || (month <= 0 || month > 12)) {
            return new Result(false, "月份不能为空 且 必须在1~12之间", null);
        }
        return new Result(true, "查询成功", transactionStatsMapper.sumAmountByMonthAndType(userId, type, year, month));
    }

    // 月度总览：收入合计 - 支出合计 = 结余
    // 月次サマリー：収入合計 - 支出合計 = 残高
    public Result getMonthlyStats(Integer userId, Integer year, Integer month) {
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if (year == null || year <= 0) {
            return new Result(false, "年份不能为空 且 不能小于零", null);
        }
        if (month == null || (month <= 0 || month > 12)) {
            return new Result(false, "月份不能为空 且 必须在1~12之间", null);
        }
        BigDecimal incomeTotal =
                transactionStatsMapper.sumAmountByMonthAndType(userId, "income", year, month);
        BigDecimal expenseTotal =
                transactionStatsMapper.sumAmountByMonthAndType(userId, "expense", year, month);
        BigDecimal balance = incomeTotal.subtract(expenseTotal);
        return new Result(true, "查询成功", new MonthlyStatsVO(incomeTotal, expenseTotal, balance));
    }

    // 分类统计：按 income / expense 查询每个分类的合计金额
    // カテゴリ別集計：income / expense ごとに各カテゴリの合計金額を取得する
    public Result getStatsByCategoryAndType(Integer userId, String type, Integer year, Integer month) {
        if (userId == null) {
            return new Result(false, "userId不能为空", null);
        }
        if (type == null || type.isEmpty()) {
            return new Result(false, "分类类型不能为空", null);
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            return new Result(false, "类型必须是 income 或者 expense", null);
        }
        if (year == null || year <= 0) {
            return new Result(false, "年份不能为空 且 不能小于零", null);
        }
        if (month == null || (month <= 0 || month > 12)) {
            return new Result(false, "月份不能为空 且 必须在1~12之间", null);
        }
        return new Result(true, "查询成功", transactionStatsMapper.sumByCategoryAndType(userId, type, year, month));
    }
}
