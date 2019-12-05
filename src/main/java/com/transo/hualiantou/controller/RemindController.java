package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSONObject;
import com.transo.hualiantou.ReturnsStyle.*;
import com.transo.hualiantou.pojo.Delivery;
import com.transo.hualiantou.pojo.ProjectManage;
import com.transo.hualiantou.pojo.Remind;
import com.transo.hualiantou.pojo.TermSheet;
import com.transo.hualiantou.repository.*;
import com.transo.hualiantou.service.RemindService;
import com.transo.hualiantou.token.JWTUtils;
import com.transo.hualiantou.utils.DateTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jack_YD
 * @create 2019/9/5 16:22
 */
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "消息提醒模块")
public class RemindController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务


    @Autowired
    private ProjectManageRepository projectManageRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditingPersonalRepository personalRepository;
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
    private ProjectRateRepository rateRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RemindService remindService;

    @Autowired
    private MoneyManageRepository moneyManageRepository;

    @Autowired
    private CollectRepository collectRepository;

    @Autowired
    private TermSheetRepository termSheetRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @GetMapping("getRemind")
    @ApiOperation(value = "获取用户消息提醒", response = Remind.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "页数"),@ApiImplicitParam(name = "pageSize", value = "条数"),@ApiImplicitParam(name = "type", value = "类型(0.系统通知,1.收到投递,2.收到协议)"),@ApiImplicitParam(name = "status", value = "状态:是否已看(0.未读,1.已读)")})
    public JSONResult getRemind(@RequestHeader(required = false) String token, HttpServletResponse rp, String type,String status,  int pageNo,  int pageSize, HttpServletRequest rq) {
        if (StringUtils.isBlank(token)) {
            rp.setStatus(4001);
            return new JSONResult("未登录!", 4001);
        }
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        long id = JWTUtils.getUserSubject(token).getId();


        RemindResult remindResult = new RemindResult();

        //统计
        Statistics statistics = new Statistics(projectManageRepository.countByUserId(id), moneyManageRepository.countByUserId(id),
                collectRepository.countByTypeAndUserId("1", id), collectRepository.countByTypeAndUserId("2", id), termSheetRepository.countCooperationByMoney("%", "1","%",id)+"");
        remindResult.setStatistics(statistics);



        RemindResultStyle<Remind> reminds = remindService.pageQuery(id,status,type, pageNo, pageSize);

        remindResult.setMessageReminds(reminds);


        return new JSONResult(remindResult);
    }


   /* @PutMapping("lookedByType")
    @ApiOperation(value = "通过类型设置为已看")
    @ApiImplicitParam(name = "type", value = "通知类型(1.认证,2.投递,3.投资协议,4.合作协议,5.项目,6.资金)")
    public JSONResult lookedByType(@RequestHeader(required = false) String token, HttpServletResponse rp,  String type, HttpServletRequest rq) {
        if (StringUtils.isBlank(token)) {
            rp.setStatus(4001);
            return new JSONResult("未登录请先登录!", 4001);
        }
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        remindService.updIsLook(JWTUtils.getUserSubject(token).getId(), type);
        return new JSONResult();
    }*/

    @PutMapping("lookedById")
    @ApiOperation(value = "通过id设置为已看")
    @ApiImplicitParam(name = "id", value = "消息通知id")
    public JSONResult lookedById(@RequestHeader(required = false) String token, HttpServletResponse rp, @RequestBody JSONObject obj, HttpServletRequest rq) {
        if (StringUtils.isBlank(token)) {
            rp.setStatus(4001);
            return new JSONResult("未登录请先登录!", 4001);
        }
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        Long id = obj.getLong("id");
        if(id==0){
            return new JSONResult("ID不可为空!",400);
        }
        remindService.isLook(id);
        return new JSONResult();
    }

    @DeleteMapping("delRemind")
    @ApiOperation(value = "通过id删除")
    @ApiImplicitParam(name = "id", value = "消息通知id")
    public JSONResult delRemind(@RequestHeader(required = false) String token, HttpServletResponse rp, @RequestBody JSONObject obj, HttpServletRequest rq) {
        if (StringUtils.isBlank(token)) {
            rp.setStatus(4001);
            return new JSONResult("未登录请先登录!", 4001);
        }
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        Long id = obj.getLong("id");
        if(id==0){
            return new JSONResult("ID不可为空!",400);
        }
        remindService.deleteRemind(id);
        return new JSONResult();
    }
}
