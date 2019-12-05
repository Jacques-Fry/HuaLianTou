package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.ProjectRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectRateRepository extends JpaRepository<ProjectRate, Long> {


    @Modifying
    @Transactional
    @Query("update ProjectRate set prStatus=?2 where id=?1")
    void updStatus(long id, String s);

    Page<ProjectRate> findAllByProjectCodeAndPrStatusAndType(String code, String status, String type, Pageable pageable);

    List<ProjectRate> findAllByProjectCodeAndPrStatusAndTypeLike(String code, String status, String type);

}
