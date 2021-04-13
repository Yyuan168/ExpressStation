package com.kkb.bean;

import java.util.ArrayList;
import java.util.List;

public class ResultData<T> {
    // 每次查询的数据集合
    private List<T> rows = new ArrayList<>();
    // 总数量
    private Integer total;

    public ResultData() {
    }

    public ResultData(List<T> rows, Integer total) {
        this.rows = rows;
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
