package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSONObject;
import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.pojo.DeliveryPackage;
import com.transo.hualiantou.repository.DeliveryPackageRepository;
import com.transo.hualiantou.service.ExpenseService;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jack_YD
 * @create 2019/10/17 15:44
 */
@CrossOrigin
@RestController
@RequestMapping("delivery")
@Api(tags = "投递套餐模块")
public class DeliveryPackageController {

    @Autowired
    private DeliveryPackageRepository deliveryPackageRepository;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //查询套餐列表
    @GetMapping("search")
    @ApiOperation(value = "查询投递套餐列表",response = DeliveryPackage.class)
    public JSONResult search(){
        return new JSONResult(deliveryPackageRepository.findAll());
    }


    //生成投递套餐订单
    @PostMapping("/addExpense")
    @ApiOperation(value ="生成投递套餐订单")
    @ApiImplicitParam(value = "商品编号",name="goodsCode")
    public JSONResult save(@RequestHeader String token, HttpServletRequest rq, HttpServletResponse rp, @RequestBody JSONObject obj) {


        Integer goodsCode=obj.getInteger("goodsCode");

        if(goodsCode==0){
            return new JSONResult("商品编号不可为空!",400);
        }

        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        long id = JWTUtils.getUserSubject(token).getId();
        return new JSONResult(expenseService.addExpense(id,goodsCode,2));
    }

    //模拟支付成功
    @PutMapping("/settlementExpense")
    @ApiOperation("模拟支付成功")
    @ApiImplicitParam(value = "订单号",name="orderCode")
    public JSONResult expenseSettlement(@RequestBody JSONObject obj) {
        String orderCode = obj.getString("orderCode");
        if(StringUtils.isBlank(orderCode)){
            return new JSONResult("订单号不可为空!",400);
        }
        expenseService.settlementExpense(orderCode,2);
        return new JSONResult();
    }
}
