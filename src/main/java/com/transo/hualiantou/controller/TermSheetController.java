package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSONObject;
import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.ReturnsStyle.PageResult;
import com.transo.hualiantou.service.RemindService;
import com.transo.hualiantou.utils.DateTime;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/8/27 13:47
 */

@CrossOrigin(origins = "*")
@RestController
@Api(tags = "投资协议模块")
public class TermSheetController {

    @Autowired
    private TermSheetRepository termSheetRepository;
    @Autowired
    private MoneyManageRepository moneyManageRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务
    @Autowired
    private InvestmentRepository investmentRepository;
    @Autowired
    private ProjectManageRepository projectManageRepository;

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
    private RemindService remindService;
    @Autowired
    private GroupRepository groupRepository;


    @Autowired
    private CollectRepository collectRepository;

    //发起投资协议
    @PostMapping("/project/termSheet")
    @ApiOperation("发起投资协议")
    //@Transactional
    public JSONResult termSheetSave(@RequestBody @Valid TermSheet termSheet, BindingResult bindingResult, @RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq) {

        long id = JWTUtils.getUserSubject(token).getId();
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        if (bindingResult.hasErrors()) {
            // 错误处理 （抛出异常交给全局处理或者在这里返回自定义的 JSON）
            return new JSONResult(bindingResult);
        }

        User user = userRepository.getOne(id);
        //todo 投资协议次数限制
        if (termSheetRepository.countByPmUserIdAndCreateTimeLike(id, "%" + DateTime.currentDateString() + "%") >= 2) {
            return new JSONResult("一天只能发起投资协议两次！", 5000);
        }


        if (id == moneyManageRepository.findByCode(termSheet.getMmCode()).getUserId()) {
            return new JSONResult("不能给自己的资金发起投资协议！！", 5000);
        }

        //查询 Institutions 获取资金编码 并插入
        MoneyManage money = moneyManageRepository.findByCodeAndStatusLike(termSheet.getMmCode(), "1");

        if (money == null) {
            return new JSONResult("资金机构可能不存在，请核实！！", 400);
        }

        if (termSheetRepository.countByMmCodeAndPmCodeAndStatusAndCreateTimeLike(termSheet.getMmCode(), termSheet.getPmCode(), "%", DateTime.currentDateString() +
                "%") > 0) {
            return new JSONResult("一天同一项目只能对同一资金发协议一次！", 400);
        }

        //是否存在未确认的投资协议
        if(termSheetRepository.countByStatusAndMmCodeAndPmCode("0", termSheet.getMmCode(),termSheet.getPmCode())>0){
            return new JSONResult("您有协议未被对方确认,无法再次对此资金发起协议！", 400);
        }


        ProjectManage project = projectManageRepository.findByCode(termSheet.getPmCode());

        if (project == null) {
            return new JSONResult("项目数据不存在!", 400);
        } else if (!StringUtils.equals(project.getStatus(), "1")) {
            return new JSONResult("项目状态异常!", 5000);
        }

        //项目名
        termSheet.setPmName(project.getName());
        //资金方Id
        termSheet.setMmUserId(money.getUserId());
        termSheet.setPmUserId(id);
        termSheet.setMmName(money.getInstitutions());
        termSheet.setStatus("0");
        termSheet.setCreateTime(DateTime.currentDateString3());
        termSheetRepository.save(termSheet);

        Remind remind = new Remind("2", "协议通知", project.getAuditingName()+"让您确认一份协议", project.getId(), money.getUserId());
        remindService.addRemind(remind);


        return new JSONResult();
    }

    @GetMapping("rates")
    @ApiOperation("合作协议列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "项目方名称", required = true),
            @ApiImplicitParam(name = "status", value = "状态 可以为空"),
            @ApiImplicitParam(name = "pageNo", value = "分页参数")})
    public JSONResult getTermSheet(@RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq, @RequestParam String name, @RequestParam(name = "status", required = false) String status, @RequestParam(name = "pageNo", required = false) int pageNo) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        long id = JWTUtils.getUserSubject(token).getId();

        if (StringUtils.isBlank(status) || "-1".equals(status)) {
            status = "%";
        }
        if (StringUtils.isBlank(name)) {
            name = "%";
        }
       /* String pmId = "%", mmId = "%";
        if ("1".equals(type)) {
            //项目方
            pmId = id + "";
        } else if ("2".equals(type)) {
            //资金方
            mmId = id + "";
        } else {
            return new JSONResult("传入类型错误！！");
        }*/


        if (StringUtils.isBlank(pageNo + "") || pageNo == 0) {
            pageNo = 1;
        }
        Pageable pageable = new PageRequest(pageNo - 1, 10);

        Page<TermSheet> sheets = termSheetRepository.findAllByMmUserIdAndStatusLikeAndPmNameLikeOrderByCreateTimeDesc(id, status, "%" + name + "%", pageable);

        PageResult<TermSheet> page = new PageResult<>(sheets.getTotalPages(), sheets.getContent());
        return new JSONResult(page);
    }

    @PutMapping("/rate")
    @ApiOperation("更改协议状态")
    @ApiImplicitParam(name = "o", value = "状态：id表主键（必填）status（1.确定协议 ，2.拒绝协议 ） 如果失败填写message ", dataType = "application/json", required = true)
    @Transactional
    public JSONResult pudTermSheetStatus(@RequestHeader String token, @RequestBody JSONObject o, BindingResult bindingResult, HttpServletRequest rq) {
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            return new JSONResult("token过期或无效!", 4000);
        }
        if (bindingResult.hasErrors()) {
            // 错误处理 （抛出异常交给全局处理或者在这里返回自定义的 JSON）
            return new JSONResult(bindingResult);
        }
        long id = o.getLong("id");
        if (id == 0) {
            return new JSONResult("未获取到ID值", 400);
        }

        TermSheet sheet = termSheetRepository.findOne(id);

        if (sheet == null) {
            return new JSONResult("未解析到数据,数据可能不存在", 400);
        }

        ProjectManage projectManage = projectManageRepository.findByCodeAndStatusLike(sheet.getPmCode(), "%");
        if (projectManage == null) {
            return new JSONResult("项目数据已丢失!", 500);
        }
        if (!"0".equals(sheet.getStatus())) {
            return new JSONResult("该协议已被处理,无法重复处理", 5000);
        }

        if ("2".equals(o.getString("status"))) {
            sheet.setStatus(o.getString("status"));
            sheet.setMessage(o.getString("message"));

        }

        if ("1".equals(o.getString("status"))) {
            MoneyManage money = moneyManageRepository.findByCode(sheet.getMmCode());
            sheet.setStatus(o.getString("status"));
            //生成投资案例
            Investment investment = new Investment();
            investment.setCode(money.getInvestments());
            investment.setStatus("1");
            investment.setProjectName(projectManage.getName());//项目名称
            investment.setMoney(sheet.getMoney());//投资金额
            investment.setTime(DateTime.currentDateString());//投资时间
            investment.setProjectCode(sheet.getPmCode());//被投资项目编码

            investmentRepository.save(investment);

            Integer nowMoney = new Integer(termSheetRepository.sumMoney(projectManage.getCode()));

            //已融资成功
            if (nowMoney >= projectManage.getNowMoney()) {
                projectManageRepository.updStatus(projectManage.getId(), "5");
                Remind remind1 = new Remind("0", "项目通知", "你有项目已融资成功: "+projectManage.getName(), projectManage.getId(), projectManage.getUserId());
                remindService.addRemind(remind1);

                
            }
            Remind remind2 = new Remind("2", "协议通知", projectManage.getAuditingName()+"发起的的协议被您通过,合作项目已更新" , projectManage.getId(), money.getUserId());
            remind2.setDataName(projectManage.getName());
            remindService.addRemind(remind2);

        }

        sheet = termSheetRepository.save(sheet);

        Remind remind = null;
        if ("1".equals(sheet.getStatus())) {
            remind = new Remind("2", "协议通知", "该项目发起的协议被对方确认通过", projectManage.getId(), projectManage.getUserId());
            remind.setDataName(projectManage.getName());
        }
        if ("2".equals(sheet.getStatus())) {
            remind = new Remind("2", "协议通知", "该项目发起的协议被对方拒绝", projectManage.getId(), projectManage.getUserId());
            remind.setDataName(projectManage.getName());
        }
        if (remind != null) {
            remindService.addRemind(remind);
        }


        return new JSONResult();
    }


    @GetMapping("/rate")
    @ApiOperation("获取单个项目的所有协议")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "项目编码", required = true),
            @ApiImplicitParam(name = "name", value = "资金名称", required = false),
            @ApiImplicitParam(name = "status", value = "协议状态", required = false),
            @ApiImplicitParam(name = "pageNo", value = "分页参数", required = false)
    })
    public JSONResult getRateOne(@RequestHeader String token, @RequestParam(name = "code") String code, @RequestParam(name = "name") String name, @RequestParam(name = "status") String status, @RequestParam(name = "pageNo") Integer pageNo, HttpServletRequest rq) {
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            return new JSONResult("token过期或无效!", 4000);
        }

        if (pageNo == null || pageNo == 0) {
            pageNo = 1;
        }
        if (StringUtils.isBlank(status) || "-1".equals(status)) {
            status = "%";
        }
        if (StringUtils.isEmpty(name)) {
            name = "%";
        }
        Pageable pageable = new PageRequest(pageNo - 1, 12);
        Page<TermSheet> sheetPage = termSheetRepository.findAllByStatusLikeAndPmCodeAndMmNameLikeOrderByCreateTimeDesc(status, code, "%" + name + "%", pageable);
        PageResult<TermSheet> pageResult = new PageResult<>(sheetPage.getTotalPages(), sheetPage.getContent());
        return new JSONResult(pageResult);
    }

    @GetMapping("termSheetByMoney")
    @ApiOperation(value = "资金方获取确认协议的项目列表", response = ProjectManage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pmName", value = "项目名称", required = false),
            /*@ApiImplicitParam(name = "mmName", value = "资金名称", required = false),*/
            @ApiImplicitParam(name = "status", value = "项目状态", required = false),
            @ApiImplicitParam(name = "type", value = "项目类型", required = false),
            @ApiImplicitParam(name = "pageSize", value = "每页个数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNo", value = "分页参数", required = true)
    })
    public JSONResult termSheetByMoney(@RequestParam(name = "type") String type, @RequestParam(name = "pmName", required = false) String pmName, @RequestParam(name = "status", required = false) String status, @RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestHeader String token, HttpServletRequest rq) {
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            return new JSONResult("token过期或无效!", 4000);
        }

        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        if (StringUtils.isBlank(status) || "-1".equals(status)) {
            status = "%";
        }
        if (StringUtils.isBlank(type) || "-1".equals(type)) {
            type = "%";
        }


        if (StringUtils.isEmpty(pmName)) {
            pmName = "%";
        }else{
            pmName="%"+pmName+"%";
        }

        long userId = JWTUtils.getUserSubject(token).getId();

        List<TermSheet> TermSheets = termSheetRepository.cooperationByMoney( type, status, pmName,userId,pageNo-1,pageSize);

        Integer total=termSheetRepository.countCooperationByMoney( type, status, pmName,userId);

        ProjectManage project = null;
        for (TermSheet termSheet : TermSheets) {

            if (termSheet!=null&&StringUtils.isNotBlank(termSheet.getPmCode())) {

                project = projectManageRepository.findByCode(termSheet.getPmCode());
                ProjectManageController.changeProject(userId, null, project, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);
                termSheet.setProjectManage(project);

            }else{
                total=0;
            }
        }

        return new JSONResult(new PageResult<>(total,TermSheets));
    }


}
