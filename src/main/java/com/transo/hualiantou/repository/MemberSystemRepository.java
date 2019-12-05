package com.transo.hualiantou.repository;

import com.transo.hualiantou.pojo.MemberSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/10/16 17:59
 */
public interface MemberSystemRepository extends JpaRepository<MemberSystem,Long> {
    List<MemberSystem> findByBelongMember(long belongMember);

    MemberSystem findTop1ByBelongMemberOrderByCreateTimeDesc(long belongMember);
}
