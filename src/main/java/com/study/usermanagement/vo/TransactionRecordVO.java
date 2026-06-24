package com.study.usermanagement.vo;


import java.math.BigDecimal;
import java.time.LocalDate;

// 收支记录响应对象：给前端看的记录数据，包含分类名，不直接暴露 categoryId
// 取引記録レスポンス：画面表示用データ。categoryId ではなくカテゴリ名を返す
public class TransactionRecordVO {
    private Integer id;
    private String categoryName;
    private String type;
    private BigDecimal amount;
    private String remark;
    private LocalDate recordDate;

    public TransactionRecordVO() {
    }

    public TransactionRecordVO(Integer id, String categoryName, String type, BigDecimal amount, String remark, LocalDate recordDate) {
        this.id = id;
        this.categoryName = categoryName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
}
