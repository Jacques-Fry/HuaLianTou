package com.transo.hualiantou;

import com.transo.hualiantou.mapper.DataStatisticsMapper;
import com.transo.hualiantou.pojo.DataStatistics;
import com.transo.hualiantou.service.DataStatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest*/
public class HualiantouPojoApplicationTests {

   /* @Autowired
    DataStatisticsMapper dataStatisticsMapper;

    @Test
    public void test1(){
        List<DataStatistics> dataStatistics = dataStatisticsMapper.statisticalMoney(2019, "abe3fe36aea047739a186e9f4c5254a4", "1");
        System.err.println("项目: 月份成功融资金额与获投次数");
        for (DataStatistics dataStatistic : dataStatistics) {
            System.out.println(dataStatistic.toString());
        }
    }
    @Test
    public void test2(){
        List<DataStatistics> dataStatistics = dataStatisticsMapper.statisticalProject(2019,"b8f159bfa0c449739a24df9421ea9df0");
        System.err.println("资金: 成功投资金额与投资协议通过或拒绝次数");
        for (DataStatistics dataStatistic : dataStatistics) {
            System.out.println(dataStatistic.toString());
        }
    }

    @Test
    public void test3(){
        List<DataStatistics> dataStatistics = dataStatisticsMapper.termSheetNumTotal(2019,"abe3fe36aea047739a186e9f4c5254a4","0");
        System.err.println("统计投资协议失败/成功次数");
        for (DataStatistics dataStatistic : dataStatistics) {
            System.out.println(dataStatistic.toString());
        }
    }

    @Test
    public void test4(){
        List<DataStatistics> dataStatistics = dataStatisticsMapper.deliveryNumTotal(2019,"abe3fe36aea047739a186e9f4c5254a4");
        System.err.println("获得投递次数");
        for (DataStatistics dataStatistic : dataStatistics) {
            System.out.println(dataStatistic.toString());
        }
    }

    @Autowired
    private DataStatisticsService dataStatisticsService;

    @Test
    public void test5(){

        Map<String, Object> map = dataStatisticsService.getStatisticalProject(2019, "b8f159bfa0c449739a24df9421ea9df0");

        Map<String , Object> series =(Map<String , Object>) map.get("series");


        System.out.println(series.get("moneys"));
        System.out.println(series.get("nums"));
    }

    @Test
    public void test6(){

        Map<String, Object> map = dataStatisticsService.getStatisticalMoney(2019, "abe3fe36aea047739a186e9f4c5254a4");

        Map<String , Object> series =(Map<String , Object>) map.get("series");


        System.out.println(series.get("investment"));
        System.out.println(series.get("deliveryNumTotal"));
        System.out.println(series.get("refused"));
        System.out.println(series.get("through"));
    }*/


}
