package com.transo.hualiantou.ReturnsStyle;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/11/8 14:12
 */
public class RemindResultStyle<T> implements Serializable{
    private long total;
    private List<T> rows;
    private Integer systemRemindCount;
    private Integer deliveryRemindCount;
    private Integer termSheetRemindCount;


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

    public RemindResultStyle(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Integer getSystemRemindCount() {
        return systemRemindCount;
    }

    public void setSystemRemindCount(Integer systemRemindCount) {
        this.systemRemindCount = systemRemindCount;
    }

    public Integer getDeliveryRemindCount() {
        return deliveryRemindCount;
    }

    public void setDeliveryRemindCount(Integer deliveryRemindCount) {
        this.deliveryRemindCount = deliveryRemindCount;
    }

    public Integer getTermSheetRemindCount() {
        return termSheetRemindCount;
    }

    public void setTermSheetRemindCount(Integer termSheetRemindCount) {
        this.termSheetRemindCount = termSheetRemindCount;
    }

    public RemindResultStyle() {
    }
}
