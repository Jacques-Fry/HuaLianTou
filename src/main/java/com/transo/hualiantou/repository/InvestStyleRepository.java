package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.InvestStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvestStyleRepository extends JpaRepository<InvestStyle, Long> {

    @Query(nativeQuery = true,value = "SELECT `is_name` FROM `invest_style` WHERE `is_unid` = ?1")
    String findOneNameById(String id);
}
