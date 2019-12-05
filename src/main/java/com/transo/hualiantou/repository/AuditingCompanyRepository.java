package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.Auditing;
import com.transo.hualiantou.pojo.AuditingCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author PC20
 * @create 2019/8/13
 */
public interface AuditingCompanyRepository extends JpaRepository<AuditingCompany,Long> {
    /*@Query("select count(1) from AuditingCompany where acStatus='1' and code=?1")
    public int getPassCompany(String code);*/

    @Query("select count(1) from AuditingCompany where acStatus=?1 and code=?2")
    public int findByAcStatusAndCode(String acStatic,String code);

    @Query("update AuditingCompany set acStatus='3'where code=?1 ")
    public int updataByCode(String code);

    public List<AuditingCompany> findAllByAcStatusAndCode(String acStatus,String code);

    public AuditingCompany findByCodeAndAcStatus(String code,String acStatus);

    AuditingCompany findByIdAndAcStatus(long companyId, String s);
}
