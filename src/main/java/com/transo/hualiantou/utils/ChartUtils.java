package com.transo.hualiantou.utils;

import com.transo.hualiantou.pojo.DataStatistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/11/13 10:53
 */
public class ChartUtils {

    private static final String[] month = {"2019年1月", "2019年2月", "2019年3月", "2019年4月", "2019年5月", "2019年6月", "2019年7月", "2019年8月", "2019年9月", "2019年10月", "2019年11月", "2019年12月"};

    /**
     * 提取月份
     */
    public List<Integer> getMonth(List<DataStatistics> dataStatistics) {
        List<Integer> months = new ArrayList<>();
        for (int i = 0; i < dataStatistics.size(); i++) {
            months.add(dataStatistics.get(i).getMonth());
        }

        return months;
    }


    /**
     * 提取num 提取money
     *
     */
    public List<Double> getMoneys(List<DataStatistics> dataStatistics, int year){

        Integer newYear = Calendar.getInstance().get(Calendar.YEAR);
        Integer nuwMonth = Calendar.getInstance().get(Calendar.MONTH);

        //获取月份
        List<Integer> months =getMonth(dataStatistics);

        List<Double> moneys=new ArrayList<>();

        int count1=0;

        if (year == newYear) {
            for (int i = 0; i <= nuwMonth; i++) {
                if (!months.contains(i + 1)) {
                    moneys.add(0d);
                    continue;
                }
                moneys.add(dataStatistics.get(count1).getMoney());
                count1++;
            }
        } else if (year < newYear) {
            for (int i = 1; i <= 12; i++) {
                if (!months.contains(i + 1)) {
                    moneys.add(0d);
                } else {
                    moneys.add(dataStatistics.get(count1).getMoney());
                    count1++;
                }
            }
        }


        return moneys;
    }

    public  List<Integer> getNums(List<DataStatistics> dataStatistics, int year){

        Integer newYear = Calendar.getInstance().get(Calendar.YEAR);
        Integer nuwMonth = Calendar.getInstance().get(Calendar.MONTH);

        //获取月份
        List<Integer> months =getMonth(dataStatistics);

        List<Integer> nums=new ArrayList<>();

        int count1=0;

        if (year == newYear) {
            for (int i = 0; i <= nuwMonth; i++) {
                if (!months.contains(i + 1)) {
                    nums.add(0);
                    continue;
                }
                nums.add(dataStatistics.get(count1).getNum());
                count1++;
            }
        } else if (year < newYear) {
            for (int i = 1; i <= 12; i++) {
                if (!months.contains(i + 1)) {
                    nums.add(0);
                } else {
                    nums.add(dataStatistics.get(count1).getNum());
                    count1++;
                }
            }
        }

        return nums;
    }


    /**
     * x轴属性标识样式1
     * @return
     */
    public String[] getXAxis1(){
        return month;
    }
}
