package com.transo.hualiantou.service;

import com.transo.hualiantou.mapper.DataStatisticsMapper;
import com.transo.hualiantou.pojo.DataStatistics;
import com.transo.hualiantou.utils.ChartUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Jack_YD
 * @create 2019/11/12 16:53
 */
@Service
public class DataStatisticsService {
    @Autowired
    private DataStatisticsMapper dataStatisticsMapper;


    /**
     * 获取项目图表数据
     */
    public Map<String, Object> getStatisticalProject(int year, String pmCode) {
        List<DataStatistics> dataStatistics = dataStatisticsMapper.statisticalProject(year, pmCode);

        ChartUtils chartUtils = new ChartUtils();

        Map<String, Object> map = new HashMap<>();

        //x轴标识
        map.put("xAxis", chartUtils.getXAxis1());

        Map<String, Object> serice = new HashMap<>();
        serice.put("moneys",chartUtils.getMoneys(dataStatistics,year));
        serice.put("nums",chartUtils.getNums(dataStatistics,year));

        map.put("series", serice);

        return map;


    }

    /**
     * 获取资金图表数据
     */
    public Map<String ,Object> getStatisticalMoney(int year, String mmCode){

        ChartUtils chartUtils = new ChartUtils();

        Map<String, Object> option = new HashMap<>();
        //x轴标识
        option.put("xAxis", chartUtils.getXAxis1());

        Map<String, Object> series = new HashMap<>();

        //投资资金
        List<DataStatistics> investment = dataStatisticsMapper.statisticalMoney(year, mmCode,"1");
        series.put("investment",chartUtils.getMoneys(investment, year));

        //获得投递次数
        List<DataStatistics>  deliveryNumTotal= dataStatisticsMapper.deliveryNumTotal(year, mmCode);
        series.put("deliveryNumTotal",chartUtils.getNums(deliveryNumTotal, year));

        //投资协议通过次数
        List<DataStatistics> through = dataStatisticsMapper.termSheetNumTotal(year, mmCode,"1");
        series.put("through", chartUtils.getNums(through, year));

        //投资协议拒绝次数
        List<DataStatistics> refused = dataStatisticsMapper.termSheetNumTotal(year, mmCode,"2");
        series.put("refused",chartUtils.getNums(refused, year));

        option.put("series",series);

        return option;
    }


}
