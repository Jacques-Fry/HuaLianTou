package com.transo.hualiantou.service;

import com.transo.hualiantou.pojo.*;
import com.transo.hualiantou.repository.*;
import com.transo.hualiantou.utils.DateTime;
import com.transo.hualiantou.utils.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/10/17 16:52
 */
@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserMemberRepository userMemberRepository;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MemberSystemRepository memberSystemRepository;

    @Autowired
    private DeliveryPackageRepository deliveryPackageRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 生成订单
     *
     * @param userId
     * @param goodsCode
     * @param type
     * @return
     */
    public Expense addExpense(long userId, long goodsCode, Integer type) {

        Expense expense = new Expense();
        expense.setUserId(userId);//用户id
        expense.setGoodsCode(goodsCode);//商品编码
        expense.setType(type);//类型
        expense.setTags(1);//付费

        expense.setOrderCode(new SimpleDateFormat("yyyyMMdd").format(new Date())+idWorker.nextId() + "");//订单编号
        expense.setCreateTime(new Date());//创建时间
        expense.setStatus(0);//未完成支付

        if (type == 1) {
            //会员订单
            MemberSystem memberSystem = memberSystemRepository.findOne(goodsCode);

            if (memberSystem == null) {
                throw new RuntimeException("商品不存在!");
            }

            Member member = memberRepository.findByCode(memberSystem.getBelongMember());
            if(member==null){
                throw new RuntimeException("会员不存在!");
            }

            Integer price = memberRepository.findPriceByCode(memberSystem.getBelongMember())*memberSystem.getDuration() ;
            expense.setGoodsName(member.getName()+"-"+memberSystem.getName());//名称
            expense.setOriginalPrice(price); //原价
            expense.setActualPrice(price* memberSystem.getDiscount());//实际价格
            expense.setDuration(memberSystem.getDuration());//时长

        } else if (type == 2) {
            //投递套餐订单
            DeliveryPackage deliveryPackage = deliveryPackageRepository.findOne(goodsCode);
            if (deliveryPackage == null) {
                throw new RuntimeException("商品不存在!");
            }

            expense.setGoodsName(deliveryPackage.getDpName());//名称
            expense.setOriginalPrice(deliveryPackage.getDpPrice()); //原价
            expense.setActualPrice(deliveryPackage.getDpPrice() * deliveryPackage.getDpDiscount());//实际价格
            expense.setDeliveryNumber(deliveryPackage.getDpNumber());//投递次数
        } else {
            throw new RuntimeException("订单类型错误!");
        }

        return expenseRepository.save(expense);
    }

    /**
     * 结算订单
     */
    public void settlementExpense(String orderCode, Integer type) {
        Expense expense = expenseRepository.findOne(orderCode);
        if (expense == null) {
            throw new RuntimeException("订单不存在!");
        }
        if(expense.getStatus()!=0){
            throw new RuntimeException("订单已支付或支付失败!");
        }

        expense.setStatus(1);

        UserMember userMember = userMemberRepository.findTop1ByUserId(expense.getUserId());

        if (type == 1) {
            //会员订单
            MemberSystem memberSystem = memberSystemRepository.findOne(expense.getGoodsCode());
            if(memberSystem==null){
                throw new RuntimeException("会员套餐不存在!");
            }
            Member member = memberRepository.findOne(memberSystem.getBelongMember());

            if(member==null){
                throw new RuntimeException("此会员商品已不存在!");
            }


            User user = userRepository.findOne(expense.getUserId());

            if (user == null) {
                throw new RuntimeException("订单所属用户数据找不到!");
            }

            //成为会员
            if (userMember == null) {
                userMember = new UserMember();
            }
            //创建时间
            userMember.setCreateTime(new Date());
            //套餐
            userMember.setMemberSystemId(memberSystem.getId());
            //用户id
            userMember.setUserId(user.getId());
            //会员编号
            userMember.setMemberCode(member.getCode());
            //投递次数
            userMember.setDeliveryNumber(userMember.getDeliveryNumber()+memberSystem.getDeliveryNumber());
            //发布项目
            //发布资金
            if (StringUtils.equals(user.getAuditingType(), "1")) {
                userMember.setMoneyNumber(member.getMoneyNumberPersonal());
                userMember.setProjectNumber(member.getProjectNumberPersonal());

            } else if (StringUtils.equals(user.getAuditingType(), "2")) {
                userMember.setMoneyNumber(member.getMoneyNumberCompany());
                userMember.setProjectNumber(member.getProjectNumberCompany());

            } else if (StringUtils.equals(user.getAuditingType(), "3")) {
                userMember.setMoneyNumber(member.getMoneyNumberGovernment());
                userMember.setProjectNumber(member.getProjectNumberGovernment());

            }

            if(userMember.getClosedTime()==null){
                userMember.setClosedTime(new Date());
            }

            if (userMember.getClosedTime().getTime() >= DateTime.currentDate().getTime()) {
                //未到期续费
                //延长到期时间
                Date closeTime = userMember.getClosedTime();
                closeTime.setDate(closeTime.getDate() + expense.getDuration()*365);
                userMember.setClosedTime(closeTime);
            } else {
                //到期续费
                //重置到期时间
                Date now = DateTime.currentDate();
                now.setDate(now.getDate() + expense.getDuration()*365);
                userMember.setClosedTime(now);
            }

            //生成赠送的投递订单
            createExpense(user.getId(),memberSystem.getDeliveryNumber(),"开通 "+member.getName()+"-"+memberSystem.getName()+" 赠送");


        } else if (type == 2) {

            if (userMember == null) {
                userMember = new UserMember();
                userMember.setUserId(expense.getUserId());
            }
            //投递套餐订单
            userMember.setDeliveryNumber(userMember.getDeliveryNumber() + expense.getDeliveryNumber());

        } else {
            throw new RuntimeException("订单类型错误!");
        }

        try {
            expenseRepository.save(expense);

            userMemberRepository.save(userMember);
        } catch (Exception e) {
            throw new RuntimeException("程序出错!");
        }

    }


    /**
     * 生成赠送的投递次数订单
     */
    public void createExpense(long userId,int number,String goodsName){

        Expense expense=new Expense();
        expense.setGoodsName(goodsName);
        expense.setTags(2);//官方赠送
        expense.setOrderCode(new SimpleDateFormat("yyyyMMdd").format(new Date())+idWorker.nextId() + "");//订单编号
        expense.setType(2);//类型:投递套餐
        expense.setDeliveryNumber(number);//投递次数
        expense.setActualPrice(0);//价格
        expense.setStatus(1);//已结算
        expense.setUserId(userId);//用户id
        expense.setCreateTime(new Date());//创建时间
        expenseRepository.save(expense);
    }


    /**
     * 查询订单
     */

    public Page<Expense> search(long userId, int pageNo, int pageSize,Integer type,String orderCode) {
        if (pageSize < 1) {
            pageSize = 1;
        }

        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
        if(type!=1&&type!=2){
            return expenseRepository.findByUserIdAndOrderCodeLikeOrderByCreateTimeDesc(userId,"%"+orderCode+"%", pageable);
        }

        return expenseRepository.findByUserIdAndTypeAndOrderCodeLikeOrderByCreateTimeDesc(userId,type,"%"+orderCode+"%", pageable);
    }




}
