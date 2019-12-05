package com.transo.hualiantou.service;

import com.transo.hualiantou.pojo.Expense;
import com.transo.hualiantou.pojo.Member;
import com.transo.hualiantou.pojo.MemberSystem;
import com.transo.hualiantou.pojo.UserMember;
import com.transo.hualiantou.repository.ExpenseRepository;
import com.transo.hualiantou.repository.MemberRepository;
import com.transo.hualiantou.repository.MemberSystemRepository;
import com.transo.hualiantou.repository.UserMemberRepository;
import com.transo.hualiantou.utils.DateTime;
import com.transo.hualiantou.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author chen
 * @create 2019/10/14 15:01
 */

@Service
public class MemberService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserMemberRepository userMemberRepository;


    @Autowired
    private MemberSystemRepository memberSystemRepository;

    @Autowired
    private IdWorker idWorker;


    @Transactional
    public void UserSave(UserMember userMember) {
        userMemberRepository.save(userMember);
    }


    public List<Member> findAll() {
        return memberRepository.findAll();
    }


}
