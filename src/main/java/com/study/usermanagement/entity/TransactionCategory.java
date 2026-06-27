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

    // 默认分类编码，例如 SALARY、FOOD。用户自定义分类为 null
    private String code;

    // 是否系统默认分类：true 表示默认分类，false 表示用户自定义分类
    private Boolean isDefault;

    public TransactionCategory() {
    }

    public TransactionCategory(Integer id, Integer userId, String name, String type) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
    }

    public TransactionCategory(Integer id, Integer userId, String name, String type, String code, Boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.code = code;
        this.isDefault = isDefault;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "TransactionCategory{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
