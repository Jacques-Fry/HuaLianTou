package com.transo.hualiantou.ReturnsStyle;

import java.io.Serializable;

/**
 * @author Jack_YD
 * @create 2019/9/6 15:16
 */
public class Statistics implements Serializable{

    private String projectCount;//我的项目
    private String moneyCount;//我的资金
    private String projectCollect;//项目收藏
    private String moneyCollect;//资金收藏
    private String projectUnite ;//合作项目

    public String getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(String projectCount) {
        this.projectCount = projectCount;
    }

    public String getMoneyCount() {
        return moneyCount;
    }

    public void setMoneyCount(String moneyCount) {
        this.moneyCount = moneyCount;
    }

    public String getProjectCollect() {
        return projectCollect;
    }

    public void setProjectCollect(String projectCollect) {
        this.projectCollect = projectCollect;
    }

    public String getMoneyCollect() {
        return moneyCollect;
    }

    public void setMoneyCollect(String moneyCollect) {
        this.moneyCollect = moneyCollect;
    }

    public String getProjectUnite() {
        return projectUnite;
    }

    public void setProjectUnite(String projectUnite) {
        this.projectUnite = projectUnite;
    }

    public Statistics() {
    }

    public Statistics(String projectCount, String moneyCount, String projectCollect, String moneyCollect, String projectUnite) {
        this.projectCount = projectCount;
        this.moneyCount = moneyCount;
        this.projectCollect = projectCollect;
        this.moneyCollect = moneyCollect;
        this.projectUnite = projectUnite;
    }
}
