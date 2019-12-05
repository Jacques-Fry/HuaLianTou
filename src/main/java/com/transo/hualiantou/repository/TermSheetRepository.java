package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.TermSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TermSheetRepository extends JpaRepository<TermSheet, Long> {


    Page<TermSheet> findAllByMmUserIdAndStatusLikeAndPmNameLikeOrderByCreateTimeDesc(long mmUserId,String status, String s, Pageable pageable);

    long countByPmCodeAndMmUserIdAndStatus(String projectCode, long id, String s);


    Page<TermSheet> findAllByStatusLikeAndPmCodeAndMmNameLikeOrderByCreateTimeDesc(String status, String code, String s, Pageable pageable);



    Page<TermSheet> findByStatusIsLike(String s, Pageable pageable);

    @Query(nativeQuery = true,value = "select distinct  `mm_code` from `term_sheet`  where `ts_status`=1 order by `ts_create_time` DESC")
    List<String> findMoneySuccess();

    @Query(nativeQuery = true,value = "SELECT SUM(pm_now_money) FROM `term_sheet` WHERE ts_status='1'")
    String moneyTrading();

    @Query(nativeQuery = true,value = "SELECT SUM(pm_now_money) FROM `term_sheet` WHERE ts_status='1' AND pm_code=?1")
    String sumMoney(String pmCode);

    String countByStatus(String status);

    String countByStatusAndMmUserId(String s,long id);

    Integer countByStatusAndMmCodeAndPmCode(String s,String mmCode,String pmCode);

    int countByMmCodeAndPmCodeAndStatusAndCreateTimeLike(String mmCode, String pmCode, String s, String s1);

    //TermSheet findByStatusAndMmCodeAndPmCode(String s, String mmCode, String pmCode);

    Integer countByPmUserIdAndCreateTimeLike(long pmUserId,String time);



    //资金方的合作项目
    @Query(nativeQuery = true,value="SELECT t.* FROM term_sheet t JOIN (SELECT `pm_code` FROM `project_manage` WHERE pm_type LIKE ?1 AND pm_status LIKE ?2  AND pm_name LIKE ?3 ) p ON p.pm_code=t.pm_code WHERE  t.mm_user_id =?4 GROUP BY t.pm_code LIMIT ?5,?6 ")
    List<TermSheet> cooperationByMoney(String type,String status,String pmName,long userId,int pageNo,int pageSize);

    @Query(nativeQuery = true,value="select count(1) from ( SELECT t.ts_id FROM term_sheet t JOIN (SELECT `pm_code` FROM `project_manage` WHERE pm_type LIKE ?1 AND pm_status LIKE ?2  AND pm_name LIKE ?3 ) p ON p.pm_code=t.pm_code WHERE  t.mm_user_id =?4 GROUP BY t.pm_code) n")
    Integer countCooperationByMoney(String type,String status,String pmName,long userId);
}
