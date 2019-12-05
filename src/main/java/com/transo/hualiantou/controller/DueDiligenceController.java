package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSONObject;
import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.ReturnsStyle.PageResult;
import com.transo.hualiantou.pojo.DueDiligence;
import com.transo.hualiantou.pojo.ProjectManage;
import com.transo.hualiantou.repository.DueDiligenceRepository;
import com.transo.hualiantou.repository.ProjectManageRepository;
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
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/10/16 10:17
 */
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "尽职调查模块")
@RequestMapping("/survey")
public class DueDiligenceController {

    @Autowired
    private ProjectManageRepository projectManageRepository;

    @Autowired
    private DueDiligenceRepository dueDiligenceRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    @PostMapping("/upload")
    @ApiOperation(value = "上传尽职调查")
    @ApiImplicitParams({@ApiImplicitParam(value = "code",name = "项目code"),
    @ApiImplicitParam(value = "investPaper",name = "调查表url")})
    public JSONResult uploadSurvey(@RequestHeader String token, HttpServletRequest rq, @RequestBody JSONObject obj){
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            return new JSONResult("token过期或无效!", 4000);
        }
        String code=obj.getString("code");

        if(StringUtils.isBlank(code)){
            return new JSONResult("项目未指定!", 400);
        }

        String investPaper= obj.getString("investPaper");
        if(StringUtils.isBlank(investPaper)){
            return new JSONResult("调查表不可为空!", 400);
        }

        ProjectManage project = projectManageRepository.findByCode(code);

        if(project==null){
            return new JSONResult("项目数据不存在!", 400);
        }

        if(StringUtils.equals(project.getStatus(),"0")||StringUtils.equals(project.getStatus(),"2")||StringUtils.equals(project.getStatus(),"3")){
            return new JSONResult("项目状态异常,无法上传!", 400);
        }

        DueDiligence dueDiligence=new DueDiligence();

        //初始化数据及赋值
        dueDiligence.setUserId(JWTUtils.getUserSubject(token).getId());
        dueDiligence.setProjectName(project.getName());
        dueDiligence.setProjectCode(project.getCode());
        dueDiligence.setInvestStatus("1");
        dueDiligence.setInvestPaper(investPaper);
        dueDiligence.setInvestDate(new Date());

        dueDiligenceRepository.save(dueDiligence);

        return new JSONResult();

    }

    @GetMapping("/search")
    @ApiOperation(value = "搜索尽职调查记录",response = DueDiligence.class)
    @ApiImplicitParams({@ApiImplicitParam(value = "项目名称",name="name"),@ApiImplicitParam(value = "页数",name="pageNo"),@ApiImplicitParam(value = "个数",name="pageSize")})
    public JSONResult querySurvey(@RequestHeader String token, HttpServletRequest rq,String name,int pageNo,int pageSize){

        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            return new JSONResult("token过期或无效!", 4000);
        }

        if(pageSize<0){
            pageSize=1;
        }

        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
        Page<DueDiligence> dueDiligences = dueDiligenceRepository.findByProjectNameLikeAndUserIdOrderByInvestDateDesc("%" + name + "%",JWTUtils.getUserSubject(token).getId(),pageable);
        return new JSONResult(new PageResult<>(dueDiligences.getTotalElements(),dueDiligences.getContent()));
    }

}
