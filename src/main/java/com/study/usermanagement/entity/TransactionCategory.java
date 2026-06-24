package com.study.usermanagement.entity;

import jakarta.validation.constraints.NotBlank;

// 收支分类实体：对应 transaction_category 表
// 収支カテゴリエンティティ：transaction_category テーブルに対応する
public class TransactionCategory {
    private Integer id;
    private Integer userId;
    @NotBlank(message = "分类名不能为空")
    private String name;
    @NotBlank(message = "类型不能为空")
    private String type;

    public TransactionCategory() {
    }

    public TransactionCategory(Integer id, Integer userId, String name, String type) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TransactionCategory{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
