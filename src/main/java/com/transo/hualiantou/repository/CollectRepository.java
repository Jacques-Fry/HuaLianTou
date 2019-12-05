package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.Collect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


public interface CollectRepository extends JpaRepository<Collect, Long> {


    Page<Collect> findAllByUserIdAndTypeAndAuTypeLikeAndNameLike(long id, String type, String auType, String name, Pageable pageable);
    @Transactional
    @Modifying
    void deleteByUserIdAndCodeAndType(long userId, String code, String type);


    long countByCodeAndUserIdAndType(String code,long userId,String type);


    String countByTypeAndUserId(String type,long UserId);
}
