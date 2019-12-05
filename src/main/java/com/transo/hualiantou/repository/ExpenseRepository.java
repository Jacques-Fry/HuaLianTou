package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chen
 * @create 2019/10/14 15:00
 */
public interface ExpenseRepository extends JpaRepository<Expense, String> {

    Page<Expense> findByUserIdAndTypeAndOrderCodeLikeOrderByCreateTimeDesc(long id,Integer type,String orderCode, Pageable pageable);

    Page<Expense> findByUserIdAndOrderCodeLikeOrderByCreateTimeDesc(long id,String orderCode,Pageable pageable);
}
