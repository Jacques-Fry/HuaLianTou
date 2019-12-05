package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.ClassifyTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassifyTagRepository extends JpaRepository<ClassifyTag, Long> {

    @Query(nativeQuery = true,value = "SELECT `ct_name` FROM `classify_tag` WHERE `ct_unid` IN ?1 ")
    String[] findNameByIdIn(String [] id);
}
