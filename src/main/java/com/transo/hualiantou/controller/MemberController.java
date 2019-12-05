package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSONObject;
import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.ReturnsStyle.PageResult;
import com.transo.hualiantou.pojo.Expense;
import com.transo.hualiantou.pojo.Member;
import com.transo.hualiantou.pojo.UserMember;
import com.transo.hualiantou.repository.MemberRepository;
import com.transo.hualiantou.repository.MemberSystemRepository;
import com.transo.hualiantou.repository.UserMemberRepository;
import com.transo.hualiantou.service.ExpenseService;
import com.transo.hualiantou.service.MemberService;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author chen
 * @create 2019/10/14 15:03
 */
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "会员模块")
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MemberSystemRepository memberSystemRepository;

    @Autowired
    private ExpenseService expenseService;


    @Autowired
    private UserMemberRepository userMemberRepository;

    //生成会员订单
    @PostMapping("/addExpense")
    @ApiOperation("生成订单")
    @ApiImplicitParam(value = "商品编号", name = "goodsCode")
    public JSONResult save(@RequestHeader String token, HttpServletRequest rq,HttpServletResponse rp, @RequestBody JSONObject obj) {
        Integer goodsCode = obj.getInteger("goodsCode");

        if (goodsCode == 0) {
            return new JSONResult("商品编号不可为空!", 400);
        }
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        Long id = JWTUtils.getUserSubject(token).getId();
        return new JSONResult(expenseService.addExpense(id, goodsCode, 1));
    }


    //支付完成修改消费记录状态 添加中间表数据UserMember {xxxxxx}

    //模拟支付成功
    @PutMapping("/settlementExpense")
    @ApiOperation("模拟支付成功")
    @ApiImplicitParam(value = "订单号", name = "orderCode")
    public JSONResult expenseSettlement(@RequestBody JSONObject obj) {
        String orderCode = obj.getString("orderCode");
        if (StringUtils.isBlank(orderCode)) {
            return new JSONResult("订单号不可为空!", 400);
        }
        expenseService.settlementExpense(orderCode, 1);
        return new JSONResult();
    }


    //展示会员列表
    @GetMapping("list")
    @ApiOperation(value = "会员列表", response = Member.class)
    public JSONResult findAll() {
        List<Member> members = memberService.findAll();
        for (Member member : members) {
            member.setMemberSystems(memberSystemRepository.findByBelongMember(member.getCode()));
        }

        return new JSONResult(members);
    }

    //根据会员code查询套餐
    @GetMapping("searchByCode")
    @ApiOperation(value = "根据会员code查询套餐", response = Member.class)
    public JSONResult searchByCode(int code) {
        Member member = memberRepository.findByCode(code);
        member.setMemberSystems(memberSystemRepository.findByBelongMember(member.getCode()));

        return new JSONResult(member);
    }

    //查询用户会员信息
    @GetMapping("searchUser")
    @ApiOperation(value = "查询用户会员信息", response = UserMember.class)
    public JSONResult searchUser(@RequestHeader String token, HttpServletRequest rq, HttpServletResponse rp) {
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        UserMember userMember = userMemberRepository.findTop1ByUserId(JWTUtils.getUserSubject(token).getId());

        userMember.setMember(memberRepository.findByCode(userMember.getMemberCode()));

        userMember.setMemberSystem(memberSystemRepository.findOne(userMember.getMemberSystemId()));


        return new JSONResult(userMember);
    }

}
