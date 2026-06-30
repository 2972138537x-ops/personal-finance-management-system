package com.study.usermanagement.service;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.exception.BusinessException;
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
            throw new BusinessException("userId不能为空");
        }
        if (type == null || type.isEmpty()) {
            throw new BusinessException("分类类型不能为空");
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            throw new BusinessException("类型必须是 income 或者 expense");
        }
        if (year == null || year <= 0) {
            throw new BusinessException("年份不能为空 且 不能小于零");
        }
        if (month == null || (month <= 0 || month > 12)) {
            throw new BusinessException("月份不能为空 且 必须在1~12之间");
        }
        return new Result(true, "查询成功", transactionStatsMapper.sumAmountByMonthAndType(userId, type, year, month));
    }

    // 月度总览：收入合计 - 支出合计 = 结余
    // 月次サマリー：収入合計 - 支出合計 = 残高
    public Result getMonthlyStats(Integer userId, Integer year, Integer month) {
        if (userId == null) {
            throw new BusinessException("userId不能为空");
        }
        if (year == null || year <= 0) {
            throw new BusinessException("年份不能为空 且 不能小于零");
        }
        if (month == null || (month <= 0 || month > 12)) {
            throw new BusinessException("月份不能为空 且 必须在1~12之间");
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
            throw new BusinessException("userId不能为空");
        }
        if (type == null || type.isEmpty()) {
            throw new BusinessException("分类类型不能为空");
        }
        if (!"income".equals(type) && !"expense".equals(type)) {
            throw new BusinessException("类型必须是 income 或者 expense");
        }
        if (year == null || year <= 0) {
            throw new BusinessException("年份不能为空 且 不能小于零");
        }
        if (month == null || (month <= 0 || month > 12)) {
            throw new BusinessException("月份不能为空 且 必须在1~12之间");
        }
        return new Result(true, "查询成功", transactionStatsMapper.sumByCategoryAndType(userId, type, year, month));
    }
}
