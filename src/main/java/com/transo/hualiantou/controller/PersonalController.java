package com.transo.hualiantou.controller;

import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.pojo.AuditingPersonal;
import com.transo.hualiantou.repository.AuditingPersonalRepository;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "个人认证模块")
public class PersonalController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    @Autowired
    private AuditingPersonalRepository personalRepository;

    /*进行认证*/
    @PostMapping("personal")
    @ApiOperation("个人认证")
    public JSONResult save(@RequestBody @Valid AuditingPersonal personal, BindingResult bindingResult, @RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq) {
        JSONResult jsonResult = null;

        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }

        if (bindingResult.hasErrors())
            // 错误处理 （抛出异常交给全局处理或者在这里返回自定义的 JSON）
            return new JSONResult(bindingResult);
        else {

            if(personalRepository.countByApStatusAndCode("0",JWTUtils.getUserSubject(token).getCode())!=0){
                return new JSONResult("有正在审核中的个人认证,无法重复提交!",400);
            }

            if(personalRepository.findIDcard(personal.getIdcard())!=0){
                return new JSONResult("身份证已被使用!",400);
            }
            personal.setCreateTime(new Date());
            personal.setCode(JWTUtils.getUserSubject(token).getCode());
            personal.setApStatus("0");
            personalRepository.save(personal);
            jsonResult = new JSONResult(personal);
        }
        return jsonResult;
    }

    @GetMapping("personal")
    @ApiOperation("获取认证通过的个人认证")
    public JSONResult get(@RequestHeader String token, HttpServletRequest rq){
        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            return new JSONResult("token过期或无效!",4000);
        }

        return new JSONResult(personalRepository.findByCodeAndApStatus(JWTUtils.getUserSubject(token).getCode(),"1"));
    }
}
