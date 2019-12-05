package com.transo.hualiantou.ReturnsStyle;


import java.io.Serializable;
import java.util.List;

/**
 * @author 花落泪知雨
 * @create 2019/8/26
 */
public class PageResult<T> implements Serializable {
    private long total;
    private List<T> rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageResult() {
    }
}
