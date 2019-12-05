package com.transo.hualiantou.controller;

import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.ReturnsStyle.PageResult;
import com.transo.hualiantou.pojo.*;
import com.transo.hualiantou.service.RemindService;
import com.transo.hualiantou.service.UserMemberService;
import com.transo.hualiantou.utils.DateTime;
import com.transo.hualiantou.repository.*;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

import static org.aspectj.bridge.Version.getTime;

/**
 * @author Jack_YD
 * @create 2019/8/29 11:24
 */

@CrossOrigin(origins = "*")
@RestController
@Api(tags = "投递管理模块")
public class DeliveryController {

    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    @Autowired
    private RemindService remindService;

    @Autowired
    private MoneyManageRepository moneyManageRepository;

    @Autowired
    private ProjectManageRepository projectManageRepository;

    @Autowired
    private UserMemberService userMemberService;

    @PostMapping("/project/delivery")
    @ApiOperation(value = "投递项目", response = Delivery.class)
    public JSONResult deliverySave(@RequestHeader(required = false) String token, HttpServletResponse rp, @RequestBody @Valid Delivery
            delivery, HttpServletRequest rq, BindingResult bindingResult) {

        if(StringUtils.isBlank(token)){
            rp.setStatus(4001);
            return new JSONResult("未登录请先登录!", 4001);
        }
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }
        long id = JWTUtils.getUserSubject(token).getId();
        if (bindingResult.hasErrors()) {
            // 错误处理 （抛出异常交给全局处理或者在这里返回自定义的 JSON）
            return new JSONResult(new JSONResult(bindingResult));
        }
        String vip = "N";

        //不能对已入资的资金 再投递相同的项目
        //一天对相同的资金 只能
        // delivery.getMoneyCode(),delivery.getProjectCode(),时间Status;

        if (!"1".equals(moneyManageRepository.findByCode(delivery.getMoneyCode()).getStatus())) {
            return new JSONResult("资金状态不正确，不能投递！", 400);
        }

        if (id == moneyManageRepository.findByCode(delivery.getMoneyCode()).getUserId()) {
            return new JSONResult("不能投递自己的资金！", 5000);
        }


        if (deliveryRepository.countByMoneyCodeAndProjectCodeAndStatusLikeAndCreateTimeLike(delivery.getMoneyCode(), delivery.getProjectCode(), "1", "%") > 0) {
            return new JSONResult("该资金方已注资", 400);
        }
        if (deliveryRepository.countByMoneyCodeAndProjectCodeAndStatusLikeAndCreateTimeLike(delivery.getMoneyCode(), delivery.getProjectCode(), "%", DateTime.currentDateString() +
                "%") > 0) {
            return new JSONResult("一天同一项目只能对同一资金投递一次！", 400);
        }

        int  deliveryNumber= userMemberService.deliveryRow(id);

        if(deliveryNumber<=0){
            return new JSONResult("投递次数不足,无法投递", 6001);
        }


        Delivery deliveryLast = deliveryRepository.findByMoneyCodeAndProjectCodeAndShow(delivery.getMoneyCode(), delivery.getProjectCode(),"1");
        if(deliveryLast!=null){
            deliveryLast.setShow("2");
            deliveryRepository.save(deliveryLast);
        }
        MoneyManage moneyManage=moneyManageRepository.findByCode(delivery.getMoneyCode());
        ProjectManage projectManage=projectManageRepository.findByCode(delivery.getProjectCode());
        if(projectManage==null){
            return new JSONResult("为解析到项目数据！", 400);
        }
        if(!StringUtils.equals(projectManage.getStatus(),"1")){
            return new JSONResult("项目状态不正常！", 400);
        }



        if(projectManage.getUserId()!=id){
            rp.setStatus(5000);
            return new JSONResult("不能投递不属于自己的项目！", 5000);
        }
        delivery.setShow("1");
        delivery.setSendUserId(projectManage.getUserId());
        delivery.setSendUserId(id);
        delivery.setReceiveUserId(moneyManage.getUserId());
        delivery.setStatus("0");
        delivery.setCreateTime(DateTime.currentDateString3());
        delivery=deliveryRepository.save(delivery);

        Remind remind=new Remind("1","投递通知",projectManage.getAuditingName()+"给你投递了一个项目",projectManage.getId(),delivery.getReceiveUserId());
        remind.setDataName(projectManage.getName());
        remindService.addRemind(remind);

        //投递次数减一
        userMemberService.deductDeliveryNumber(id);


        return new JSONResult();
    }


    //用户所有投递项目记录
    @GetMapping("/project/deliverys")
    @ApiOperation(value = "投递项目记录", response = Delivery.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1.项目方 2.资金方", required = true),
            @ApiImplicitParam(name = "pageNo", value = "分页参数")
    })
    public JSONResult getDeliverys(@RequestHeader String token,HttpServletResponse rp, HttpServletRequest rq, @RequestParam(name = "type") String type, @RequestParam(name = "pageNo") int pageNo) {

        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }
        if (pageNo == 0) {
            pageNo = 1;
        }
        Pageable pageable = new PageRequest(pageNo - 1, 10);
        Page<Delivery> deliveries = null;
        if ("1".equals(type)) {
            deliveries = deliveryRepository.findAllBySendUserIdLikeAndShowOrderByCreateTimeDesc(JWTUtils.getUserSubject(token).getId(),"1", pageable);
        }
        if ("2".equals(type)) {
            deliveries = deliveryRepository.findAllByReceiveUserIdLikeAndShowOrderByCreateTimeDesc(JWTUtils.getUserSubject(token).getId(),"1", pageable);
        }
        if (deliveries == null) {
            return new JSONResult("啥都没有", 400);
        }
        for (int i = 0; i < deliveries.getContent().size(); i++) {

            deliveries.getContent().get(i).setProjectManage(projectManageRepository.findByCode(deliveries.getContent().get(i).getProjectCode()));
            deliveries.getContent().get(i).setMoneyManage(moneyManageRepository.findByCode(deliveries.getContent().get(i).getMoneyCode()));

            if (collectRepository.countByCodeAndUserIdAndType(deliveries.getContent().get(i).getProjectCode(), JWTUtils.getUserSubject(token).getId(), type)>0) {
                deliveries.getContent().get(i).setIsColler("已收藏");
            } else {
                deliveries.getContent().get(i).setIsColler("未收藏");
            }
        }
        PageResult<Delivery> pageResult = new PageResult<>(deliveries.getTotalPages(), deliveries.getContent());
        return new JSONResult(pageResult);
    }

    //一条投递详情记录信息
    @GetMapping("/project/delivery/{id}")
    @ApiOperation("一条投递详情记录信息")
    public JSONResult getDelivery(@RequestHeader String token,HttpServletResponse rp, HttpServletRequest rq,
                                  @PathVariable long id/*, BindingResult bindingResult*/) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }
        /*if (bindingResult.hasErrors()) {
            // 错误处理 （抛出异常交给全局处理或者在这里返回自定义的 JSON）
            return new JSONResult(new JSONResult(bindingResult));
        }*/
        return new JSONResult(deliveryRepository.findOne(id));
    }
}
