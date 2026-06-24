package com.study.usermanagement.vo;

import java.math.BigDecimal;

// 分类统计响应对象：某个分类名对应的合计金额
// カテゴリ別集計レスポンス：カテゴリ名ごとの合計金額
public class CategoryStatsVO {
    private String categoryName;
    private BigDecimal totalAmount;

    public CategoryStatsVO() {
    }

    public CategoryStatsVO(String categoryName, BigDecimal totalAmount) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
