package com.transo.hualiantou.repository;


import com.transo.hualiantou.pojo.ClassifyRotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClassifyRotationRepository extends JpaRepository<ClassifyRotation, Long> {

    @Query(nativeQuery = true,value = "SELECT `ro_name` FROM `classify_rotation` WHERE `ro_unid` = ?1 ")
    String findNameById(String id);

}
