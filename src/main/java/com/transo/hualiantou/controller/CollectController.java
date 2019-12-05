package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSONObject;
import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.ReturnsStyle.PageResult;
import com.transo.hualiantou.pojo.*;
import com.transo.hualiantou.repository.*;
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

/**
 * @author Jack_YD
 * @create 2019/8/29 11:49
 */

@CrossOrigin(origins = "*")
@RestController
@Api(tags = "收藏管理模块")
public class CollectController {



    @Autowired
    private MoneyManageRepository moneyRepository;
    @Autowired
    ProjectManageRepository projectRepository;

    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    @Autowired
    private AuditingCompanyRepository companyRepository;
    @Autowired
    private AuditingGovernmentRepository governmentRepository;
    @Autowired
    private ClassifyTagRepository classifyTagRepository;

    @Autowired
    private ClassifyRealmRepository classifyRealmRepository;

    @Autowired
    private ClassifyRotationRepository classifyRotationRepository;

    @Autowired
    private InvestStyleRepository investStyleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TermSheetRepository termSheetRepository;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private InvestmentRepository investmentRepository;

    @PostMapping("/collect")
    @ApiOperation(value = "收藏功能", response = Collect.class)
    public JSONResult collectSave(@RequestHeader(required = false) String token, HttpServletResponse rp, /*BindingResult bindingResult, */@RequestBody @Valid Collect collect, HttpServletRequest rq) {

        if(StringUtils.isBlank(token)){
            rp.setStatus(4001);
            return new JSONResult("未登录请先登录!", 4001);
        }

        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }


        long id = JWTUtils.getUserSubject(token).getId();
        //收藏人id
        collect.setUserId(id);

        if(collectRepository.countByCodeAndUserIdAndType(collect.getCode(),id,collect.getType())!=0){
            return new JSONResult("已收藏无法重复收藏!",400);
        }


        if ("1".equals(collect.getType())) {
            ProjectManage project = projectRepository.findByCode(collect.getCode());
            if (id == project.getUserId()) {
                return new JSONResult("不能收藏自己的项目", 5000);
            }

            collect.setAuType(project.getType());
            collect.setName(project.getName());

        } else if ("2".equals(collect.getType())) {
            MoneyManage money = moneyRepository.findByCode(collect.getCode());
            if (id == money.getUserId()) {
                return new JSONResult("不能收藏自己的资金", 5000);
            }

            collect.setAuType(money.getType());
            collect.setName(money.getInstitutions());
        } else {
            return new JSONResult("输入正确收藏类型！！");
        }
        collectRepository.save(collect);
        return new JSONResult();
    }

    @GetMapping("/collect")
    @ApiOperation(value = "查看收藏列表", response = Collect.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型：1.项目 2.资金(必填)", required = true),
            @ApiImplicitParam(name = "name", value = "名称"),
            @ApiImplicitParam(name = "auType", value = "认证类型"),
            @ApiImplicitParam(name = "pageNo", value = "分页参数")})
    public JSONResult getCollect(@RequestHeader String token, HttpServletRequest rq, HttpServletResponse rp, @RequestParam String type, @RequestParam String auType, @RequestParam(name = "name", required = false) String name, @RequestParam int pageNo) {



        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        long id = JWTUtils.getUserSubject(token).getId();
        if (StringUtils.isBlank(name)) {
            name = "%";
        }
        name = "%" + name + "%";
        if ("-1".equals(auType) || StringUtils.isBlank(auType)) {
            auType = "%";
        }
        if (pageNo == 0) {
            pageNo = 1;
        }

        Pageable pageable = new PageRequest(pageNo - 1, 10);
        Page<Collect> page = collectRepository.findAllByUserIdAndTypeAndAuTypeLikeAndNameLike(id, type, auType, name, pageable);
        for (Collect collect : page) {
            if ("1".equals(collect.getType())) {
                ProjectManage project= projectRepository.findByCode(collect.getCode());
                ProjectManageController.changeProject(id,null,project,collectRepository,termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);
                collect.setProjectManage(project);
            }

            if ("2".equals(collect.getType())) {
                MoneyManage moneyManage = moneyRepository.findByCode(collect.getCode());
                MoneyController.changeMoney(id,null,moneyManage,collectRepository, companyRepository, governmentRepository, userRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository,investmentRepository);
                moneyManage.setTerritory(moneyManage.getTerritory().replace("[", "").replace("]", ""));
                collect.setMoneyManage(moneyManage);
            }
        }


        if (page == null) {
            return new JSONResult(new PageResult<>());
        }
        return new JSONResult(new PageResult<>(page.getTotalElements(), page.getContent()));
    }


    @DeleteMapping("/collect")
    @ApiOperation("取消收藏")
    @ApiImplicitParam(name = "o", value = "type code", dataType = "application/json")
    public JSONResult delCollect(@RequestHeader String token, HttpServletResponse rp, @RequestBody JSONObject o, HttpServletRequest rq) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        long userId = JWTUtils.getUserSubject(token).getId();
        String code = o.getString("code");
        String type = o.getString("type");
        collectRepository.deleteByUserIdAndCodeAndType(userId, code, type);
        return new JSONResult();
    }


}
