package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.Investment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/8/23 14:40
 */

public interface InvestmentRepository  extends JpaRepository<Investment,Long> {
    @Transactional
    @Modifying
    @Query("delete from Investment where id =?1")
     void updStatus(long id);



    Page<Investment> findByCodeOrderByCreateTime(String code, Pageable pageable);

    @Transactional
    @Modifying
    @Query(nativeQuery = true ,value="delete FROM investment  where it_code =?1")
    void updStatusByPmCode(String pmCode);

    List<Investment> findByCode(String code);
}
