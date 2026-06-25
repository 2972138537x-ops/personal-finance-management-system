package com.study.usermanagement.vo;


import java.util.List;

// 分页响应对象：返回当前页数据、总条数、当前页码和每页条数
// ページングレスポンス：現在ページのデータ、総件数、ページ番号、1ページ件数を返す
public class PageVO {
    // 当前页的数据
    // 現在ページのデータ
    private List<TransactionRecordVO> list;

    // 总条数
    // 条件に一致する総件数
    private Integer total;

    // 当前第几页
    // 現在のページ番号
    private Integer page;

    // 每页多少条
    // 1ページあたりの件数
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
