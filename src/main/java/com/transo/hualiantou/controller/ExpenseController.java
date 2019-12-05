package com.transo.hualiantou.controller;

import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.ReturnsStyle.PageResult;
import com.transo.hualiantou.pojo.Expense;
import com.transo.hualiantou.service.ExpenseService;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jack_YD
 * @create 2019/10/17 17:16
 */

@CrossOrigin
@RestController
@RequestMapping("/expense")
@Api(tags = "用户订单模块")
public class ExpenseController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("search")
    @ApiOperation(value = "查询用户订单",response = Expense.class)
    @ApiImplicitParams({@ApiImplicitParam(value = "订单号",name="orderCode"),@ApiImplicitParam(value = "类型(0.全部,1.会员套餐订单,2.投递套餐订单)",name="type"),@ApiImplicitParam(value = "页数",name="pageNo"),@ApiImplicitParam(value = "个数",name="pageSize")})
    public JSONResult querySurvey(@RequestHeader String token, HttpServletRequest rq,  int pageNo, int pageSize,Integer type,String orderCode){
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            return new JSONResult("token过期或无效!", 4000);
        }
        Page<Expense> expenses = expenseService.search(JWTUtils.getUserSubject(token).getId(), pageNo, pageSize,type,orderCode);
        return new JSONResult(new PageResult<>(expenses.getTotalElements(),expenses.getContent()));

    }


}
