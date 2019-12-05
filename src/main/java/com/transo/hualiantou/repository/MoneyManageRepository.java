package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.MoneyManage;
import com.transo.hualiantou.pojo.ProjectManage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/8/21 15:46
 */
public interface MoneyManageRepository extends JpaRepository<MoneyManage,Long> ,JpaSpecificationExecutor<ProjectManage> {


    public MoneyManage findByCode(String code);

    public Page<MoneyManage> findByUserIdAndInstitutionsLikeAndStatusLikeAndTypeLikeOrderByCreateTimeDesc(long userId, String name, String status, String type, Pageable pageable);

    public MoneyManage findByInstitutions(String institutions);


    public MoneyManage findByIdAndStatus(long id,String status);

    public MoneyManage findByCodeAndStatusLike(String code, String status);

    public MoneyManage findByInvestments(String investments);
//状态 领域 地址 方式 优质 类型 金额
    Page<MoneyManage> findAllByStatusAndTerritoryLikeAndAddressLikeAndWayLikeAndQualityLikeAndTypeLikeAndMoneyBetween(String status,String territory, String address, String way, String quality, String type, long start, long end, Pageable pageable);

    public List<MoneyManage> findByQualityAndStatusIn(String quality,String[] status);

    List<MoneyManage> findByInstitutionsLikeAndStatus(String institutions,String status);

    @Query(value = "SELECT new MoneyManage (m.id,m.logo ,m.institutions,m.dyf,m.address) FROM MoneyManage as m WHERE m.status in ?1  ORDER BY m.qualityDate DESC ")
    Page<MoneyManage> findByStatusOrderByCreateTimeDesc (String s,Pageable pageable);

    Page<MoneyManage> findByCodeInAndStatus(List<String> code,String s,Pageable pageable);


    @Query(nativeQuery = true,value="SELECT COUNT(DISTINCT  `mm_user`) FROM `money_manage` WHERE mm_status='1'")
    String findMoneySide();

    @Query(nativeQuery = true,value="SELECT SUM(mm_money) FROM `money_manage` WHERE mm_status='1'")
    String sumMoney();

    String countByStatus(String status);

    String countByUserId(long userId);


    Integer countByUserIdAndStatusInAndType(long userId,String[] status,String type);

    Integer countByUserIdAndStatusIn(long userId,String[] status);


    //精选资金
    @Query(value = "SELECT new MoneyManage (m.id,m.logo ,m.institutions,m.dyf,m.address) FROM MoneyManage as m WHERE m.status in ?2 AND m.quality=?1 ORDER BY m.qualityDate DESC ")
    Page<MoneyManage> findByQualityAndStatusInOrderByAuditingTimeDesc(String quality, String[] status,Pageable pageable);

    @Query("select distinct new MoneyManage(m.id,m.logo ,m.institutions,m.dyf,m.territory,m.quality) from MoneyManage m where m.status in (1) and m.institutions like ?1")
    Page<MoneyManage> searchByName(String str, Pageable pageable);

    List<MoneyManage> findByUserIdAndStatus(long userId,String status);

    Integer countByUserIdAndStatus(long userId,String status);
}
