package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.ClassifyRealm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassifyRealmRepository extends JpaRepository<ClassifyRealm, Long> {

    @Query(nativeQuery = true,value = "SELECT `cr_name` FROM `classify_realm` WHERE `cr_unid` IN ?1")
    String[] findNameById(String[] id);

    @Query(nativeQuery = true,value = "SELECT `cr_name` FROM `classify_realm` WHERE `cr_unid` = ?1")
    String findOneNameById(String id);

}
