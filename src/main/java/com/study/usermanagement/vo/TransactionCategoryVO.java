package com.study.usermanagement.vo;

// 收支分类展示对象：只返回前端需要看的分类信息
// 収支カテゴリ表示用オブジェクト：フロントに見せるカテゴリ情報だけを返す
public class TransactionCategoryVO {
    private Integer id;
    private String name;
    private String type;

    public TransactionCategoryVO() {
    }

    public TransactionCategoryVO(Integer id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
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
}
