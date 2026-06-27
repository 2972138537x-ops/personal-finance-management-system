package com.study.usermanagement.vo;

// 收支分类 VO：返回给前端的分类数据
public class TransactionCategoryVO {
    private Integer id;
    private String name;
    private String type;
    private String code;
    private Boolean isDefault;

    public TransactionCategoryVO() {
    }

    public TransactionCategoryVO(Integer id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public TransactionCategoryVO(Integer id, String name, String type, String code, Boolean isDefault) {
        this.id = id;
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
}
