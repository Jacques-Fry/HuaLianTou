package com.transo.hualiantou.pojo;


import java.io.Serializable;

/**
 * @author Jack_YD
 * @create 2019/11/12 15:13
 */
public class DataStatistics implements Serializable{
    private Integer year;
    private Integer month;
    private double money;
    private Integer num;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }


    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "{\"DataStatistics\":{"
                + "\"year\":"
                + year
                + ",\"month\":"
                + month
                + ",\"money\":"
                + money
                + ",\"num\":"
                + num
                + "}}";

    }
}
