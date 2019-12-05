package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.AuditingGovernment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Jack_YD
 * @create 2019/8/19 10:48
 */
public interface AuditingGovernmentRepository extends JpaRepository<AuditingGovernment,Long> {

    @Query("select count(1) from AuditingGovernment where agStatus=?1 and code=?2")
    public int findByAgStatusAndCode(String agStatus,String code);

    public AuditingGovernment findByCodeAndAgStatus(String code,String agStatus);

}
