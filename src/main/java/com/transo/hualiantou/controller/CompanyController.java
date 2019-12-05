package com.transo.hualiantou.controller;

import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.pojo.AuditingCompany;
import com.transo.hualiantou.pojo.User;
import com.transo.hualiantou.repository.AuditingCompanyRepository;
import com.transo.hualiantou.repository.UserRepository;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@Api(tags = "企业认证模块")
public class CompanyController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务
    @Autowired
    private AuditingCompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("company")
    @ApiOperation("企业认证")
    public JSONResult save(@RequestHeader String token, HttpServletResponse rp, @RequestBody @Valid AuditingCompany company, BindingResult bindingResult, HttpServletRequest rq) {
    //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }

        JSONResult jsonResult = null;
        User user = userRepository.findOne(JWTUtils.getUserSubject(token).getId());

        if(companyRepository.findByAcStatusAndCode("0",user.getCode())!=0){
            return new JSONResult("已有审核中的企业认证,无法重复提交!",400);
        }

        if (bindingResult.hasErrors()) {
            return new JSONResult(bindingResult);
        } else if ("1".equals(user.getAuditingType()) || "2".equals(user.getAuditingType())) {

            //用户已验证个人 或 企业
            int i = companyRepository.findByAcStatusAndCode("0",user.getCode());

            if (i != 0) {
                jsonResult = new JSONResult("已有审核中的企业认证,无法重复提交!！",400);
            } else {
                company.setCode(user.getCode());
                company.setAcStatus("0");
                companyRepository.save(company);
                jsonResult = new JSONResult(company);
            }
        } else {
            jsonResult = new JSONResult("请先个人认证");
        }
        return jsonResult;
    }

    @DeleteMapping("/company/{id}")
    @ApiOperation("单个认证注销")
    @ApiImplicitParam(name = "id", value = "企业认证id")
    public JSONResult delete(@RequestHeader String token, HttpServletResponse rp, @PathVariable(value = "id",required = true) Integer id,HttpServletRequest rq) {

        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }
        //  String code = JWTUtils.getUserSubject(token).getCode();
        AuditingCompany company = new AuditingCompany();
        company.setCode(JWTUtils.getUserSubject(token).getCode());
        company.setId(id);
        company.setAcAuditor("3");
        companyRepository.saveAndFlush(company);
        return new JSONResult();
    }


    @DeleteMapping("/company")
    @ApiOperation("所有认证注销")
    public JSONResult delete(@RequestHeader String token, HttpServletResponse rp,HttpServletRequest rq) {
        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }
        companyRepository.updataByCode(JWTUtils.getUserSubject(token).getCode());
        return new JSONResult();
    }

    @GetMapping("/company/{id}")
    @ApiOperation("获取单个企业认证")
    public JSONResult getOne(@RequestHeader String token,HttpServletResponse rp,HttpServletRequest rq,@PathVariable Long id){
        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }
        return new JSONResult(companyRepository.findOne(id));
    }

    @GetMapping("/company")
    @ApiOperation("获取多个认证通过的企业认证")
    public JSONResult getAll(@RequestHeader String token, HttpServletResponse rp,HttpServletRequest rq){
        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }
        return new JSONResult(companyRepository.findAllByAcStatusAndCode("1",JWTUtils.getUserSubject(token).getCode()));
    }


}
