package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.Group;
import com.transo.hualiantou.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author PC20
 * @create 2019/8/13
 */

public interface GroupRepository extends JpaRepository<Group,Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "DELETE FROM  `group` WHERE  pm_code= ?1")
    void deleteByPmCode(String pmCode);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "DELETE FROM  `group` WHERE  mm_code= ?1")
    void deleteByMmCode(String mmCode);

    List<Group> findByMmCode(String mmCode);

    List<Group> findByPmCode(String pmCode);
}
