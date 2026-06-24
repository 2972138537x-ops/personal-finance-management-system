package com.study.usermanagement.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

// 收支记录实体：对应 transaction_record 表
// 収支記録エンティティ：transaction_record テーブルに対応する
public class TransactionRecord {
    private Integer id;
    private Integer userId;
    @NotNull(message = "分类id不能为空")
    private Integer categoryId;
    @NotBlank(message = "类型不能为空")
    private String type;
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;
    private String remark;
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
