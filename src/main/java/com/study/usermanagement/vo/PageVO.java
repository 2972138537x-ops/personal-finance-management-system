package com.study.usermanagement.vo;


import java.util.List;

public class PageVO {
    // 当前页的数据
    private List<TransactionRecordVO> list;

    // 总条数
    private Integer total;

    // 当前第几页
    private Integer page;

    // 每页多少条
    private Integer size;

    public PageVO() {
    }

    public PageVO(List<TransactionRecordVO> list, Integer total, Integer page, Integer size) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public List<TransactionRecordVO> getList() {
        return list;
    }

    public void setList(List<TransactionRecordVO> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageVO{" +
                "list=" + list +
                ", total=" + total +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
