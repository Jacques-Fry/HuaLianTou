package com.transo.hualiantou.controller;

import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.pojo.AuditingGovernment;
import com.transo.hualiantou.repository.AuditingCompanyRepository;
import com.transo.hualiantou.repository.AuditingGovernmentRepository;
import com.transo.hualiantou.repository.AuditingPersonalRepository;
import com.transo.hualiantou.token.JWTUtils;
import com.transo.hualiantou.token.UserSubject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Jack_YD
 * @create 2019/8/20 19:45
 */
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "政府认证模块")
public class GovernmentController {

    @Autowired
    private AuditingPersonalRepository personalRepository;
    @Autowired
    private AuditingCompanyRepository companyRepository;
    @Autowired
    private AuditingGovernmentRepository governmentRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    @PostMapping("government")
    @ApiOperation(value = "添加政府认证")
    public JSONResult Government(@RequestBody  @Valid AuditingGovernment government, BindingResult bindingResult, @RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq){
        JSONResult jsonResult = null;

        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }

        if (bindingResult.hasErrors()) {
            // 错误处理 （抛出异常交给全局处理或者在这里返回自定义的 JSON）
            return new JSONResult(bindingResult);
        }else {
            UserSubject userSubject=JWTUtils.getUserSubject(token);
            if(personalRepository.countByApStatusAndCode("1",userSubject.getCode())==0){
                return new JSONResult("个人认证未通过无法认证企业!",400);
            }
            /*if(companyRepository.findByAcStatusAndCode("1",userSubject.getCode())!=0){
                return new JSONResult("有未注销的企业认证!",400);
            }*/
            if(governmentRepository.findByAgStatusAndCode("1",userSubject.getCode())!=0){
                return new JSONResult("有正在认证中的政府认证!",400);
            }
            government.setCode(userSubject.getCode());
            government.setAgStatus("0");
            government=governmentRepository.save(government);
            jsonResult =new JSONResult(government);
        }
        return jsonResult;
    }


    @GetMapping("government")
    @ApiOperation("获取认证通过的政府认证")
    public JSONResult get(@RequestHeader String token,HttpServletResponse rp, HttpServletRequest rq){
        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }

        return new JSONResult(governmentRepository.findByCodeAndAgStatus(JWTUtils.getUserSubject(token).getCode(),"1"));
    }
}
