package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSONObject;
import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.ReturnsStyle.PageResult;
import com.transo.hualiantou.pojo.ProjectRate;
import com.transo.hualiantou.repository.ProjectManageRepository;
import com.transo.hualiantou.repository.ProjectRateRepository;
import com.transo.hualiantou.repository.TermSheetRepository;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Jack_YD
 * @create 2019/8/27 14:18
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "项目进度+投资历程模块")
public class ProjectRateController {

    @Autowired
    private ProjectManageRepository projectManageRepository;
    @Autowired
    private ProjectRateRepository rateRepository;
    @Autowired
    private TermSheetRepository termSheetRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    //项目进度（项目方 投资方）
    @PostMapping("/project/rate")
    @ApiOperation(value = "添加项目进度", response = ProjectRate.class)
    public JSONResult projectRateSave(@RequestBody @Valid ProjectRate rate, @RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq, BindingResult bindingResult) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        long id = JWTUtils.getUserSubject(token).getId();
        if (bindingResult.hasErrors()) {
            // 错误处理 （抛出异常交给全局处理或者在这里返回自定义的 JSON）
            return new JSONResult(bindingResult);
        }
        //项目code 不能为空
        if (StringUtils.isBlank(rate.getProjectCode())) {
            return new JSONResult("项目code，不能为空！！", 400);
        }

        if ("1".equals(rate.getType())) {
            rate.setProjectUserId(id);
        } else if ("2".equals(rate.getType())) {
            if (termSheetRepository.countByPmCodeAndMmUserIdAndStatus(rate.getProjectCode(), id, "1") == 0) {
                return new JSONResult("你没有操作该项目资格！", 5000);
            }
            rate.setMoneyUserId(id);
        }

/*
        //判断发布者==>项目方
        if (projectManageRepository.countByCodeAndStatusAndUserId(rate.getProjectCode(), "1", id) == 1) {
            rate.setProjectUserId(id);
            rate.setType("1");
        } else {//投资方==>资格
            //根据项目code 和 合作项目中的状态 判断
            //从投资协议中查询 是否成功投资
            if (termSheetRepository.countByMmCodeAndMmUserIdAndStatus(rate.getProjectCode(), id, "1")==0) {
                return new JSONResult("你没有操作该项目资格！",5000);
            }
            rate.setMoneyUserId(id);
            rate.setType("2");
        }*/
        //状态 默认为可见
        rate.setPrStatus("1");
        rateRepository.save(rate);
        return new JSONResult();
    }

    @DeleteMapping("/project/rate")
    @ApiOperation(value = "设置项目进度不可见")
    @ApiImplicitParam(name = "id", value = "项目进度ID", dataType = "URL参数")
    public JSONResult del(@RequestBody JSONObject obj, @RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        long id = obj.getLong("id");
        if (StringUtils.isBlank(id + "")) {
            return new JSONResult("未解析到进度ID", 400);
        }
        rateRepository.updStatus(id, "2");
        return new JSONResult();
    }

    //查看项目进度
    @GetMapping("/project/rates")
    @ApiOperation("查看项目进度")
    @ApiImplicitParams({@ApiImplicitParam(name = "code", value = "项目code", dataType = "url参数"),
            @ApiImplicitParam(name = "pageNo", value = "分页参数", dataType = "url参数"),
            @ApiImplicitParam(name = "type", value = "进度类型(1.项目方2.投资方)", dataType = "url参数")})
    public JSONResult getRrojectRate(@RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq, String code, Integer pageNo, String type) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        if (StringUtils.isBlank(code)) {
            return new JSONResult("未解析到项目code!", 400);
        }

        if (pageNo == null || pageNo == 0) {
            pageNo = 1;
        }
        if (StringUtils.isBlank(type)) {
            type = "%";
        }

        Pageable pageable = new PageRequest(pageNo - 1, 10);
        Page<ProjectRate> projectRate = rateRepository.findAllByProjectCodeAndPrStatusAndType(code, "1", type, pageable);

        PageResult pageResult = new PageResult(projectRate.getTotalElements(), projectRate.getContent());
        return new JSONResult(pageResult);
    }
}
