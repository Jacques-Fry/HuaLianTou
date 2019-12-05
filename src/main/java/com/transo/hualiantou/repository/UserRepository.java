package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.User;
import com.transo.hualiantou.pojo.UserMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author PC20
 * @create 2019/8/13
 */

public interface UserRepository extends JpaRepository<User,Long> {

    //验证手机号
    @Query(value = "select count(1) from User where phone = ?1")
    public int findphone(String phone);

    //登录
    @Query(value = "from User where phone = ?1 and password=?2")
    public User findphoneAndpassword(String phone,String password);

    //手机号登录
    public User findByphone(String phone);

    public User findByCode(String code);


    boolean existsByPhone(String phone);

    @Transactional
    @Modifying
    @Query(value="update User set password=?2 where phone =?1")
    void updatePassword(String phone, String password);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE user SET us_member_type=?1 WHERE us_phone =?2")
    void updateMember(String type, String phone);

}
