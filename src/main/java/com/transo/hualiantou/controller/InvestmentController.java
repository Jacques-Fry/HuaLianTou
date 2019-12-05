package com.transo.hualiantou.controller;

import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.ReturnsStyle.PageResult;
import com.transo.hualiantou.pojo.Investment;
import com.transo.hualiantou.pojo.MoneyManage;
import com.transo.hualiantou.repository.InvestmentRepository;
import com.transo.hualiantou.repository.MoneyManageRepository;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/8/26 19:30
 */
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "投资案例管理模块")
public class InvestmentController {

    @Autowired
    private MoneyManageRepository manageRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务
    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private MoneyManageRepository moneyManageRepository;

    @GetMapping("/investments")
    @ApiOperation(value = "获取单个资金的投资案例列表", response = Investment.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "code", value = "资金投资案例编码investments"), @ApiImplicitParam(name = "pageNo", value = "页数")})
    public JSONResult getInvestment(@RequestParam String code, @RequestParam Integer pageNo, @RequestHeader(required = false) String token, HttpServletResponse rp, HttpServletRequest rq) {

        if(StringUtils.isBlank(token)){
            rp.setStatus(4001);
            return new JSONResult("未登录请先登录!", 4001);
        }
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        if (pageNo == null||pageNo==0) {
            pageNo = 1;
        }
        Pageable pageable = new PageRequest(pageNo-1, 10);
        Page<Investment> page = investmentRepository.findByCodeOrderByCreateTime(code, pageable);
        return new JSONResult(new PageResult<>(page.getTotalElements(), page.getContent()));
    }

    @PostMapping("investment/{code}")
    @ApiOperation(value = "添加一个投资案例", response = Investment.class)
    @ApiImplicitParam(name = "investment", value = "投资案例", required = true)
    public JSONResult add(@PathVariable String code, @RequestHeader String token, HttpServletResponse rp, @RequestBody @Valid Investment investment, HttpServletRequest rq) {

        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        if (StringUtils.isBlank(code)) {
            return new JSONResult("未获取到code值!", 400);
        }
        investment.setCode(null);
        MoneyManage money = manageRepository.findByInvestments(code);

        if(money==null){
            return new JSONResult("未解析到资金!",400);
        }
        investment.setCode(money.getInvestments());
        investment.setStatus("1");
        investmentRepository.save(investment);
        return new JSONResult();
    }

    @PutMapping("investment")
    @ApiOperation(value = "修改一个或者多个投资案例", response = Investment.class)
    @ApiImplicitParam(name = "investment", value = "投资案例", required = true, dataType = "数组格式")
    public JSONResult upd(@RequestHeader String token, HttpServletResponse rp, @RequestBody @Valid List<Investment> investment, HttpServletRequest rq) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        for (Investment investment1 : investment) {
            investment1.setStatus("1");
        }
        investmentRepository.save(investment);

        return new JSONResult();
    }

    @DeleteMapping("investment/{id}")
    @ApiOperation(value = "根据ID删除某个投资案例")
    @ApiImplicitParam(name = "id", value = "投资案例id", required = true, paramType = "URL参数")
    public JSONResult del(@RequestHeader String token, HttpServletResponse rp, @PathVariable(value = "id", required = true) long id, HttpServletRequest rq) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        investmentRepository.updStatus(id);
        return new JSONResult();
    }

}
