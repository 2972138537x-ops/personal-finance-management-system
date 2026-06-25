package com.study.usermanagement.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

// 收支记录实体：对应 transaction_record 表
// 収支記録エンティティ：transaction_record テーブルに対応する
public class TransactionRecord {
    // 收支记录主键 id，由数据库自增生成
    // 収支記録の主キー id。DB の自動採番で生成される
    private Integer id;

    // 记录所属用户 id，由 token 对应的当前用户决定
    // 記録所有者のユーザー id。token に対応する現在ユーザーから決まる
    private Integer userId;

    // 分类 id，必须属于当前登录用户
    // カテゴリ id。ログイン中ユーザー本人のカテゴリである必要がある
    @NotNull(message = "分类id不能为空")
    private Integer categoryId;

    // 记录类型：income 表示收入，expense 表示支出
    // 記録タイプ：income は収入、expense は支出
    @NotBlank(message = "类型不能为空")
    private String type;

    // 金额使用 BigDecimal，避免 double 的精度误差
    // 金額は BigDecimal を使い、double の精度誤差を避ける
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    // 备注，可选填写
    // メモ。任意入力
    private String remark;

    // 记录日期，只保存年月日，不保存具体时间
    // 記録日。年月日のみ保存し、時刻は保存しない
    @NotNull(message = "日期不能为空")
    private LocalDate recordDate;

    public TransactionRecord() {
    }

    public TransactionRecord(Integer id, Integer userId, Integer categoryId, String type, BigDecimal amount, String remark, LocalDate recordDate) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.type = type;
        this.amount = amount;
        this.remark = remark;
        this.recordDate = recordDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", remark='" + remark + '\'' +
                ", recordDate=" + recordDate +
                '}';
    }
}
