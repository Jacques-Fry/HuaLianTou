package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.ProjectManage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.print.DocFlavor;
import java.util.List;

public interface ProjectManageRepository extends JpaRepository<ProjectManage, Long> ,JpaSpecificationExecutor<ProjectManage> {


    /*@Query("select (1) from ProjectManage where user=?1 and type=?2 and status<>?3")
    public int findByUserAndTypeAndStatus(long user, String type, String status);
*/
    //根据用户查所有项目
    public List<ProjectManage> findAllByUserId(long userId);

    //根据项目状态查所有项目
    public List<ProjectManage> findAllByStatus(String status);

    //根据项目状态，是否优质 查所有
    public List<ProjectManage> findAllByQualityLikeAndStatus(String quality, String status);

    //根据项目状态并排序查询所有
    @Query("select new ProjectManage (p.id,p.name,p.introduce,p.logo,p.address,p.territory,p.label) from ProjectManage p where p.status=?1 order by p.qualityDate desc")
    public Page<ProjectManage> findAllByStatusOrderByCreateTimeDesc(String status,Pageable pageable);

    //筛选功能（状态已通过、 所属领域 territory、所在地区（省暂定）、融资方式 way、是否优质 quality、融资金额targetMoney）
    @Query(nativeQuery = true, value = "SELECT * FROM `project_manage` WHERE  `pm_status` ='1' AND `pm_territory` LIKE ? AND pm_way LIKE ? AND pm_quality LIKE ? ")
    public List<ProjectManage> findAllByterritoryAndwayAndquality(String territory, String way, String quality);

    @Query(nativeQuery = true, value = "SELECT * FROM `project_manage` WHERE `pm_status` ='1' AND `pm_territory` LIKE ? AND pm_way LIKE ? AND pm_quality LIKE ? AND pm_target_money >= ?")
    public List<ProjectManage> findAllByterritoryAndwayAndqualityANDstart(String territory, String way, String quality, double start);

    @Query(nativeQuery = true, value = "SELECT * FROM `project_manage` WHERE `pm_status` ='1' AND `pm_territory` LIKE ?1 AND pm_way LIKE ?2 AND pm_quality LIKE ?3 AND pm_target_money >= ?4 AND pm_target_money <= ?5")
    public List<ProjectManage> findAllByterritoryAndwayAndqualityANDend(String territory, String way, String quality, int start, int end);

    @Query(nativeQuery = true, value = "SELECT * FROM `project_manage`  WHERE  `pm_status` = 1 ORDER BY  RAND() LIMIT ?")
    public List<ProjectManage> weekLsit(Integer size);

    public ProjectManage findByCode(String code);

    public Page<ProjectManage> findByUserIdAndNameLikeAndStatusLikeAndTypeLikeOrderByCreateTimeDesc(long userId, String name, String status, String type, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update ProjectManage set status = ?2 where id=?1")
    public void updStatus(long id , String status);


    public int countById(long id);

    public ProjectManage findByUserId(long id);

    int countByCodeAndStatusAndUserId(String code, String status, long id);

    public ProjectManage findByIdAndStatus(long id , String status);


    public ProjectManage findByCodeAndStatusLike(String code, String status);


    Page<ProjectManage> findAllByStatusAndTerritoryLikeAndAddressLikeAndWayLikeAndQualityLikeAndNowMoneyBetween(String status ,String territory, String address, String way, String quality, int start, int end, Pageable pageable);

    ProjectManage findByNameLike(String s);


    Page<ProjectManage> findByCodeInAndNameLikeAndTypeLikeAndStatusLike(List<String> codes,String name,String type,String status,Pageable pageable);

    List<ProjectManage> findAllByCodeIn(List list);

    Page<ProjectManage> findByStatusOrderByCreateTimeDesc(String s, Pageable pageable);


    @Query(nativeQuery = true, value = "SELECT COUNT( DISTINCT pm_user) FROM project_manage")
    String countByUser();

    @Query(nativeQuery = true, value = "SELECT SUM( pm_now_money) FROM project_manage")
    String moneySum();

    String countByUserId(long userId);


    @Query("select new ProjectManage (p.id,p.name,p.introduce,p.logo,p.address,p.territory,p.label) from ProjectManage p where p.quality=?1 and p.status=?2 order by p.qualityDate desc")
    Page<ProjectManage> findAllByQualityLikeAndStatusOrderByQualityDateDesc(String y, String s, Pageable pageable);

    ProjectManage findByCodeAndStatusIn(String code,String [] status);

    Integer countByUserIdAndStatusInAndType(long userId,String[] status,String type);

    Integer countByUserIdAndStatusIn(long userId,String[] status);


    @Query(" select DISTINCT new ProjectManage (p.id,p.name,p.logo,p.introduce,p.territory,p.label,p.quality,p.code) from ProjectManage p  where p.status in (1,5) and p.name like ?1 ")
    Page<ProjectManage> searchByName(String str, Pageable pageable);

    @Query("SELECT new ProjectManage (p.id, p.code,p.name,p.type,p.territory,p.nowRound,p.nowMoney,p.status,p.contactName,p.auditingName,p.companyId) FROM ProjectManage p WHERE p.code = ?1")
    ProjectManage findCooperationProject(String pmCode);


    List<ProjectManage> findByUserIdAndStatus(long userId,String status);

    Integer countByUserIdAndStatus(long userId,String status);
}
