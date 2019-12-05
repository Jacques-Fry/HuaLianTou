package com.transo.hualiantou.mapper;

import com.transo.hualiantou.pojo.DataStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/11/12 15:15
 */
@Mapper
public interface DataStatisticsMapper {

    /**
     * 项目: 月份成功融资金额与获投次数
     */
    @Select("SELECT YEAR(ts_create_time) AS `year`, " +
            "MONTH(ts_create_time) AS `month`, " +
            "SUM(pm_now_money) AS money , " +
            "COUNT(1) AS num " +
            "FROM `term_sheet` " +
            "WHERE YEAR(ts_create_time)=#{year} " +
            "AND pm_code=#{code} " +
            "AND ts_status='1' " +
            "GROUP BY `month`")
    List<DataStatistics> statisticalProject(@Param("year") Integer year ,@Param("code") String pmCode);

    /**
     * 资金: 成功投资金额与投资协议通过或拒绝次数
     */
    @Select("SELECT YEAR(ts_create_time) AS year, " +
            "MONTH(ts_create_time) AS month, " +
            "SUM(pm_now_money) AS money " +
            "FROM `term_sheet` " +
            "WHERE YEAR(ts_create_time)=#{year} " +
            "AND mm_code=#{code} " +
            "AND ts_status=#{status} " +
            "GROUP BY `month`")
    List<DataStatistics> statisticalMoney(@Param("year") Integer year ,@Param("code") String mmCode , @Param("status")String status);


    /**
     * 统计投资协议失败/成功次数
     */
    @Select("SELECT YEAR(ts_create_time) AS `year`, " +
            "MONTH(ts_create_time) AS `month`, " +
            "COUNT(1) AS num " +
            "FROM `term_sheet` " +
            "WHERE YEAR(ts_create_time)=#{year} " +
            "AND mm_code=#{code} " +
            "AND ts_status=#{status} " +
            "GROUP BY `month` ")
    List<DataStatistics> termSheetNumTotal(@Param("year") Integer year ,@Param("code")String mmCode,@Param("status")String status);

    /**
     * 获得投递次数
     */
    @Select("SELECT YEAR(dy_create_time) AS `year`, " +
            "MONTH(dy_create_time) AS `month`, " +
            "COUNT(1) AS num " +
            "FROM `delivery` " +
            "WHERE YEAR(dy_create_time)=#{year} " +
            "AND dy_money_code =#{code} " +
            "GROUP BY `month` ")
    List<DataStatistics> deliveryNumTotal(@Param("year") Integer year ,@Param("code")String mmCode);
}
