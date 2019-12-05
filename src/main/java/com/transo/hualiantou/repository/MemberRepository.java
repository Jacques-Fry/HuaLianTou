package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.Member;
import com.transo.hualiantou.pojo.UserMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author chen
 * @create 2019/10/14 15:01
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(nativeQuery = true,value = "SELECT name FROM member WHERE code =?")
    String findMemberNameByCode(long code);

    @Query(nativeQuery = true,value = "SELECT price FROM member WHERE code =?")
    Integer findPriceByCode(long code);

    Member findByCode(long code);

    Member findByType(String type);
}
