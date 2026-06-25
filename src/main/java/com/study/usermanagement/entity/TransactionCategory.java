package com.study.usermanagement.entity;

import jakarta.validation.constraints.NotBlank;

// 收支分类实体：对应 transaction_category 表
// 収支カテゴリエンティティ：transaction_category テーブルに対応する
public class TransactionCategory {
    // 分类主键 id，由数据库自增生成
    // カテゴリ主キー id。DB の自動採番で生成される
    private Integer id;

    // 分类所属用户 id，不能由前端随意指定
    // カテゴリ所有者のユーザー id。フロントから自由に指定させない
    private Integer userId;

    // 分类名称，例如 餐饮、工资、交通
    // カテゴリ名。例：食費、給料、交通
    @NotBlank(message = "分类名不能为空")
    private String name;

    // 分类类型：income 表示收入，expense 表示支出
    // カテゴリタイプ：income は収入、expense は支出
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
