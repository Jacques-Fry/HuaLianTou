package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.UserMember;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * @author chen
 * @create 2019/10/14 14:58
 */

public interface UserMemberRepository extends JpaRepository<UserMember, String> {

    UserMember findTop1ByUserId(long userId);


//    @Query(nativeQuery = true, value = "SELECT user_id,delivery_number,project_number,money_number FROM `user_member` WHERE user_id =?1")
    public UserMember findByUserId(long id);


    @Query(nativeQuery = true, value = "SELECT closed_time ,us_uid FROM `user_member` um JOIN `user` u ON u.`us_uid` =um.`user_id` WHERE u.`us_phone`=?")
    public UserMember selectTime(String phone);

    @Query(nativeQuery = true, value = "SELECT  DATE_FORMAT(closed_time,'%Y-%m-%d') >= DATE_FORMAT(?1,'%Y-%m-%d') FROM `user_member` WHERE user_id = ?2")
    public Integer Expires(Date now,long userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE user_member SET delivery_number=delivery_number-1 WHERE user_id=?1")
    public int deductDeliveryNumber(long userId);


}
