package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.DueDiligence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jack_YD
 * @create 2019/10/16 16:15
 */
public interface DueDiligenceRepository extends JpaRepository<DueDiligence, Long> {

    Page<DueDiligence> findByProjectNameLikeAndUserIdOrderByInvestDateDesc(String projectName,long userId, Pageable pageable);
}
