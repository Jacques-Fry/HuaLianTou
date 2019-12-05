package com.transo.hualiantou.service;

import com.transo.hualiantou.pojo.*;
import com.transo.hualiantou.repository.MemberRepository;
import com.transo.hualiantou.repository.MemberSystemRepository;
import com.transo.hualiantou.repository.UserMemberRepository;
import com.transo.hualiantou.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/10/18 13:42
 */
@Service
public class UserMemberService {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserMemberRepository userMemberRepository;

    @Autowired
    private MemberSystemRepository memberSystemRepository;

    @Autowired
    private MemberRepository memberRepository;


    //查询投递可使用次数
    public Integer deliveryRow(Long userId) {

        UserMember byUserId = userMemberRepository.findByUserId(userId);

        return byUserId.getDeliveryNumber();
    }

    //查询項目可使用条数
    public Integer projectRow(Long userId) {
        return userMemberRepository.findByUserId(userId).getProjectNumber();
    }

    //查询资金可使用条数
    public Integer moneyRow(Long userId) {
        return userMemberRepository.findByUserId(userId).getMoneyNumber();
    }

    //投递次数减一
    public void deductDeliveryNumber(Long userId){
        userMemberRepository.deductDeliveryNumber(userId);
    }

    @Autowired
    private UserRepository userRepository;

    @Async
    public void save(Long userId) {
        Member member= memberRepository.findByType("0");

        MemberSystem memberSystem = memberSystemRepository.findTop1ByBelongMemberOrderByCreateTimeDesc(member.getCode());

        User user= userRepository.findOne(userId);

        if(member==null){
            throw new RuntimeException("注册会员信息不存在!");
        }

        //生产一条订单记录
        expenseService.createExpense(userId,memberSystem.getDeliveryNumber(),memberSystem.getContent());
        //TODO 订单记录

        //生产用户记录
        UserMember userMember = new UserMember();
        userMember.setMemberCode(member.getCode());
        userMember.setMemberSystemId(memberSystem.getId());
        userMember.setUserId(userId);
        userMember.setCreateTime(new Date());
        userMember.setDeliveryNumber(memberSystem.getDeliveryNumber());
        if (StringUtils.equals(user.getAuditingType(), "1")) {
            userMember.setMoneyNumber(member.getMoneyNumberPersonal());
            userMember.setProjectNumber(member.getProjectNumberPersonal());

        } else if (StringUtils.equals(user.getAuditingType(), "2")) {
            userMember.setMoneyNumber(member.getMoneyNumberCompany());
            userMember.setProjectNumber(member.getProjectNumberCompany());

        } else if (StringUtils.equals(user.getAuditingType(), "3")) {
            userMember.setMoneyNumber(member.getMoneyNumberGovernment());
            userMember.setProjectNumber(member.getProjectNumberGovernment());

        }else{
            userMember.setMoneyNumber(0);
            userMember.setProjectNumber(0);
        }


        //TODO 用户会员记录
        userMemberRepository.save(userMember);
    }

    public UserMember findByUserId(long id){
        return userMemberRepository.findByUserId(id);
    }

}
