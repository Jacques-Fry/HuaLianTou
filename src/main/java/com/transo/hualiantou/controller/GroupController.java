package com.transo.hualiantou.controller;

import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.pojo.Group;
import com.transo.hualiantou.pojo.MoneyManage;
import com.transo.hualiantou.pojo.ProjectManage;
import com.transo.hualiantou.repository.GroupRepository;
import com.transo.hualiantou.repository.MoneyManageRepository;
import com.transo.hualiantou.repository.ProjectManageRepository;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/8/26 15:10
 */
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "团队管理模块")
public class GroupController {

    @Autowired
    private ProjectManageRepository projectManageRepository;
    @Autowired
    private MoneyManageRepository manageRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    @Autowired
    private GroupRepository groupRepository;


    @PostMapping("group/{type}/{id}")
    @ApiOperation(value = "一次添加多个项目或者多个资金的团队", response = Group.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "团队类型(1.项目团队,2.资金团队)", required = true, paramType = "URL参数"),
            @ApiImplicitParam(name = "id", value = "资金或项目的id", required = true, paramType = "URL参数"),
            @ApiImplicitParam(name="group",value = "团队成员",required = true,dataType = "数组格式")})
    public JSONResult addMm(@RequestHeader String token, HttpServletResponse rp, @RequestBody @Valid List<Group> group, HttpServletRequest rq, @PathVariable(value = "type") String type, @PathVariable(value = "id") long id) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        try {

            if(StringUtils.isBlank(id+"")){
                return new JSONResult("未获取到ID!",400);
            }

            if ("1".equals(type)) {
                ProjectManage project = projectManageRepository.findOne(id);
                for (Group g : group) {
                    g.setPmCode(project.getTeam());
                }
                groupRepository.save(group);
            } else if ("2".equals(type)) {
                MoneyManage money = manageRepository.findOne(id);
                for (Group g : group) {
                    g.setMmCode(money.getTeam());
                }
                groupRepository.save(group);
            } else {
                return new JSONResult("未获取到指定类型!", 400);
            }
        } catch (ConstraintViolationException e) {
            JSONResult jsonResult = null;
            for (ConstraintViolation<?> c : e.getConstraintViolations()) {
                jsonResult = new JSONResult(c.getMessage(), 400);
                break;
            }
            return jsonResult;
        }


        return new JSONResult();
    }


    @PutMapping("group")
    @ApiOperation(value = "一次修改多个项目或者多个资金的团队", response = Group.class)
    @ApiImplicitParam(name="group",value = "团队成员",required = true,dataType = "数组格式")
    public JSONResult upd(@RequestHeader String token,HttpServletResponse rp, @RequestBody @Valid List<Group> group, HttpServletRequest rq){
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        groupRepository.save(group);

        return new JSONResult();
    }


    @DeleteMapping("group/{id}")
    @ApiOperation(value = "删除单个团队成员")
    @ApiImplicitParam(name = "id", value = "团队成员ID", required = true, paramType = "URL参数")
    public JSONResult del(@RequestHeader String token,HttpServletResponse rp,@PathVariable("id") long id, HttpServletRequest rq){
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        groupRepository.delete(id);

        return new JSONResult();
    }

}
