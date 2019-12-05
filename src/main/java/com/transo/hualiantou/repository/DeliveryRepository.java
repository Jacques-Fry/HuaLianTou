package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Page<Delivery> findAllBySendUserIdLikeAndShowOrderByCreateTimeDesc(long sendUserId,String show, Pageable pageable);

    Page<Delivery> findAllByReceiveUserIdLikeAndShowOrderByCreateTimeDesc(long receiveUserId,String show, Pageable pageable);


    long countBySendUserId(long id);

    long countBySendUserIdAndCreateTimeLike(long id, String time);


    long countByMoneyCodeAndProjectCodeAndStatusLikeAndCreateTimeLike(String moneyCode, String projectCode, String s, String s1);

    Delivery findByMoneyCodeAndProjectCodeAndShow(String moneyCode,String projectCode,String show);
}
