package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.AuditingPersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Jack_YD
 * @create 2019/8/19 10:40
 */
public interface AuditingPersonalRepository extends JpaRepository<AuditingPersonal,Long> {


    int countByApStatusAndCode( String status,String code);

    //查询已被使用过的身份证
    @Query("select count(1) from AuditingPersonal where apStatus=1 and idcard=?1")
    public int findIDcard(String idcard);

    @Query(nativeQuery=true,value = "select * from auditing_personal where ap_code=?1 and ap_status LIKE ?2 order by ap_create_time desc LIMIT 1")
    public AuditingPersonal findByCodeAndApStatus(String code,String apStatus);
}
