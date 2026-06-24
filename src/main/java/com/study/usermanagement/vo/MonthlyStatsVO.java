package com.study.usermanagement.vo;

import java.math.BigDecimal;

// 月度统计响应对象：收入合计、支出合计、结余
// 月別集計レスポンス：収入合計、支出合計、残高
public class MonthlyStatsVO {
    private BigDecimal incomeTotal;
    private BigDecimal expenseTotal;
    private BigDecimal balance;

    public MonthlyStatsVO() {
    }

    public MonthlyStatsVO(BigDecimal incomeTotal, BigDecimal expenseTotal, BigDecimal balance) {
        this.incomeTotal = incomeTotal;
        this.expenseTotal = expenseTotal;
        this.balance = balance;
    }

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public BigDecimal getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(BigDecimal expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
