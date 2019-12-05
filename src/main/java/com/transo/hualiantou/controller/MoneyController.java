package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.ReturnsStyle.PageResult;
import com.transo.hualiantou.service.DataStatisticsService;
import com.transo.hualiantou.service.UserMemberService;
import com.transo.hualiantou.utils.FileUtils;
import com.transo.hualiantou.utils.JsonUtil;
import com.transo.hualiantou.pojo.*;
import com.transo.hualiantou.repository.*;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * @author Jack_YD
 * @create 2019/8/22 14:25
 */
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "资金管理模块")
public class MoneyController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MoneyManageRepository moneyRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ClassifyTagRepository classifyTagRepository;

    @Autowired
    private ClassifyRealmRepository classifyRealmRepository;

    @Autowired
    private ClassifyRotationRepository classifyRotationRepository;

    @Autowired
    private InvestStyleRepository investStyleRepository;

    @Autowired
    private AuditingPersonalRepository personalRepository;
    @Autowired
    private AuditingCompanyRepository companyRepository;
    @Autowired
    private AuditingGovernmentRepository governmentRepository;
    @Autowired
    private ProjectRateRepository rateRepository;
    @Autowired
    private CollectRepository collectRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UserMemberService userMemberService;


    @PostMapping("money")
    @ApiOperation(value = "创建资金", response = MoneyManage.class)
    public JSONResult save(@RequestHeader String token, HttpServletResponse rp, @RequestBody JSONObject obj, HttpServletRequest rq) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        try {
            //获取领域
            JSONArray territoryJSON = obj.getJSONArray("territory");
            if (territoryJSON == null) {
                return new JSONResult("领域至少一个", 400);
            }
            obj.remove("territory");
            MoneyManage money = JsonUtil.jsonToPojo(obj.toString(), MoneyManage.class);
            for (int i = 0; i < territoryJSON.size(); i++) {
                territoryJSON.set(i, "" + territoryJSON.get(i));
            }
            money.setTerritory(territoryJSON.toJSONString());
            User user = userRepository.getOne(JWTUtils.getUserSubject(token).getId());

            /*---会员权限新增---*/
            String status[] = {"1", "0","3"};
            Integer num = moneyRepository.countByUserIdAndStatusIn(user.getId(), status);
            Integer moneyRow = userMemberService.moneyRow(user.getId());
            if (moneyRow <= num) {
                return new JSONResult("创建资金数量已达上限，无法创建！更多权益请查看会员权益。", 6001);
            }
            /*---会员权限新增---*/

            if ("1".equals(money.getType())) {
                if ("3".equals(user.getAuditingType())) {
                    return new JSONResult("政府不可发布个人项目!", 400);
                }
                AuditingPersonal personal = personalRepository.findByCodeAndApStatus(user.getCode(), "1");
                if (personal == null) {
                    return new JSONResult("查无个人认证数据", 400);
                }
                money.setAuditingName(personal.getRealName());
            } else if ("2".equals(money.getType())) {
                if ("3".equals(user.getAuditingType())) {
                    return new JSONResult("政府不可发布企业项目!", 400);
                } else if (!"2".equals(user.getAuditingType())) {
                    return new JSONResult("您没有认证通过的企业认证,不可发布企业项目!", 400);
                }
                AuditingCompany company = companyRepository.findByIdAndAcStatus(money.getCompanyId(), "1");
                money.setAuditingName(company.getCompanyName());
            } else if ("3".equals(money.getType())) {
                if (!"3".equals(user.getAuditingType())) {
                    return new JSONResult("您政府认证未通过,不可发布政府项目!", 400);
                }
                AuditingGovernment government = governmentRepository.findByCodeAndAgStatus(user.getCode(), "1");
                money.setAuditingName(government.getUnitsName());
            } else {
                return new JSONResult("没有选择资金类型!", 400);
            }


            if (StringUtils.isBlank(money.getLogo())) {
                money.setLogo("/base/ProjectAndMoneyDefault.png");
            }

            //团队多存少存无所谓,保证项目只存一次
            String groupCode = FileUtils.getUUID();
            if (money.getGroup() != null) {
                for (Group group : money.getGroup()) {
                    group.setMmCode(groupCode);
                    groupRepository.save(group);
                }
            }
            //投资案例多存少存无所谓,保证项目只存一次
            String investmentCode = FileUtils.getUUID();
            if(money.getInvestment()!=null){
                for (Investment investment : money.getInvestment()) {
                    investment.setCode(investmentCode);
                    investmentRepository.save(investment);

                }
            }

            money.setQuality("N");
            money.setStatus("0");
            money.setUserId(user.getId());
            money.setCode(FileUtils.getUUID());
            money.setInvestments(investmentCode);
            money.setTeam(groupCode);
            moneyRepository.save(money);

        } catch (ConstraintViolationException e) {
            JSONResult jsonResult = null;
            for (ConstraintViolation<?> c : e.getConstraintViolations()) {
                jsonResult = new JSONResult(c.getMessage(), 400);
            }
            return jsonResult;
        }

        return new JSONResult();
    }


    @PutMapping("money")
    @ApiOperation(value = "修改资金", response = MoneyManage.class)
    public JSONResult update(@RequestHeader String token, HttpServletResponse rp, @RequestBody JSONObject obj, HttpServletRequest rq) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        try {


            String code = obj.getString("code");
            if (StringUtils.isBlank(code)) {
                return new JSONResult("未获取到资金ID或编码信息", 400);
            }
            long id = JWTUtils.getUserSubject(token).getId();

            MoneyManage money = moneyRepository.findByCode(code);


            if (money == null) {
                return new JSONResult("未获取到资金信息,数据可能已不存在!", 400);
            }

            if (id != money.getUserId()) {
                return new JSONResult("您没有权限修改此资金信息!", 5000);
            }

            //删除之前所有的小组成员
            groupRepository.deleteByMmCode(money.getTeam());
            //隐藏之前所有投资案例
            investmentRepository.updStatusByPmCode(money.getInvestments());


            //记录
            Record record = new Record();
            record.setUserId(id);
            record.setOldContent(JSON.toJSONString(money));
            record.setCode(money.getCode());
            record.setContentType("1");
            record.setOperationType("0");

            money.setLogo(obj.getString("logo"));
            money.setAddress(obj.getString("address"));
            money.setDetailedAddress(obj.getString("detailedAddress"));
            money.setUrl(obj.getString("url"));
            money.setDyf(obj.getString("dyf"));
            money.setContactEmail(obj.getString("contactEmail"));
            money.setContactName(obj.getString("contactName"));
            money.setContactPhone(obj.getString("contactPhone"));

            //todo 小组成员优化
            List<Group> groups = JsonUtil.jsonToList(obj.getJSONArray("group").toJSONString(), Group.class);
            /*List<Group> groups2 =new ArrayList<>();
            if(groups!=null){
                for (Group group1 : groups2) {
                    JSONObject o1=(JSONObject) JSONObject.toJSON(group1);
                    o1.remove("id");
                    groups2.add(JsonUtil.jsonToPojo(o1.toJSONString(),Group.class));
                }
            }*/
            //money.setGroup(groups);
            if (groups != null) {
                for (Group group : groups) {
                    group.setMmCode(money.getTeam());
                    groupRepository.save(group);
                }
            }


            //todo 投资案例
            List<Investment> investments = JsonUtil.jsonToList(obj.getJSONArray("investment").toJSONString(), Investment.class);
            /*List<Investment> investment2=new ArrayList<>();
            if(investment!=null){
                for (Investment investment1 : investment) {
                    JSONObject o1=(JSONObject) JSONObject.toJSON(investment1);
                    o1.remove("id");
                    investment2.add(JsonUtil.jsonToPojo(o1.toJSONString(),Investment.class));
                }
            }

            money.setInvestment(investment);
            */
            if(investments!=null){
                for (Investment investment : investments) {
                    investment.setTime(investment.getTime().substring(0,10));
                    investment.setCode(money.getInvestments());
                    investmentRepository.save(investment);

                }
            }

            money.setCreateTime(new Date());
            money.setStatus("0");
            money = moneyRepository.save(money);

            //记录
            record.setNewContent(JSON.toJSONString(money));
            recordRepository.save(record);


        } catch (ConstraintViolationException e) {
            JSONResult jsonResult = null;
            for (ConstraintViolation<?> c : e.getConstraintViolations()) {
                jsonResult = new JSONResult(c.getMessage(), 400);
            }
            return jsonResult;
        }


        return new JSONResult();
    }


    @GetMapping("moneyUser")
    @ApiOperation(value = "获取单个未转换的用户资金详细信息", response = MoneyManage.class)
    @ApiImplicitParam(name = "id", value = "资金ID", required = true, dataType = "URL参数")
    public JSONResult getOneUser(@RequestParam(value = "id") long id, @RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        if (StringUtils.isBlank("" + id)) {
            return new JSONResult("未获取到资金ID!", 400);
        }
        MoneyManage money = moneyRepository.findOne(id);
        if (money == null) {
            rp.setStatus(5001);
            return new JSONResult("未解析到资金数据,数据可能不存在!", 5001);
        }

        //todo 小组成员
        if(StringUtils.isNotBlank(money.getTeam())){
            money.setGroup(groupRepository.findByMmCode(money.getTeam()));
        }

        //todo 投资案例
        if(StringUtils.isNotBlank(money.getInvestments())) {
            money.setInvestment(investmentRepository.findByCode(money.getInvestments()));
        }


        return new JSONResult(money);
    }

    @Autowired
    private DataStatisticsService dataStatisticsService;

    @GetMapping("money")
    @ApiOperation(value = "获取单个资金转换后的详细信息", response = MoneyManage.class)
    @ApiImplicitParam(name = "id", value = "资金id", required = true, dataType = "URL参数")
    public JSONResult getOne(@RequestParam(value = "id") long id, @RequestHeader(required = false) String token, HttpServletResponse rp, HttpServletRequest rq) {
        if (StringUtils.isBlank(token)) {
            rp.setStatus(4001);
            return new JSONResult("未登录请先登录!", 4001);
        }

        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        if (StringUtils.isBlank("" + id)) {
            return new JSONResult("未获取到资金ID!", 400);
        }


        MoneyManage money = moneyRepository.findOne(id);
        if (money == null) {
            rp.setStatus(5001);
            return new JSONResult("未解析到资金数据,数据可能不存在!", 5001);
        }
        if (money.getUserId() != JWTUtils.getUserSubject(token).getId() && !"1".equals(money.getStatus()) && !"5".equals(money.getStatus())) {

            rp.setStatus(5000);
            return new JSONResult("该项目数据可能正在审核中,无法查看详情!", 5000);
        }

        if (collectRepository.countByCodeAndUserIdAndType(money.getCode(), JWTUtils.getUserSubject(token).getId(), "2") != 0) {
            money.setIsColler("已收藏");
        } else {
            money.setIsColler("未收藏");
        }

        //投资案例


        changeMoney(JWTUtils.getUserSubject(token).getId(), null, money, collectRepository, companyRepository, governmentRepository, userRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository,investmentRepository);
        money.setTerritory(money.getTerritory().replace("[", "").replace("]", ""));

        //图表统计
        money.setStatistical(dataStatisticsService.getStatisticalMoney(Calendar.getInstance().get(Calendar.YEAR), money.getCode()));
        return new JSONResult(money);
    }


    @GetMapping("moneys")
    @ApiOperation(value = "根据条件获取个人全部资金+分页", response = MoneyManage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "name", value = "资金名", dataType = "url参数"),
            @ApiImplicitParam(name = "status", value = "项目状态:0.待审核中 1.审核通过 2. 3. 4. 5. 6. 7. 8.", dataType = "url参数"),
            @ApiImplicitParam(name = "type", value = "项目类型：1、个人项目；2、企业项目；3、政府项目", dataType = "url参数"),
            @ApiImplicitParam(name = "pageNo", value = "页数", dataType = "url参数")})
    public JSONResult findAllByMoney(String status, String name, String type, Integer pageNo, @RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        if (StringUtils.isBlank(name)) {
            name = "%";
        }

        if ("-1".equals(status) || StringUtils.isBlank(status)) {
            status = "%";
        }

        if ("-1".equals(type) || StringUtils.isBlank(type)) {
            type = "%";
        }


        if (pageNo == null || pageNo == 0) {
            pageNo = 1;
        }

        String code = JWTUtils.getUserSubject(token).getCode();
        Pageable pageable = new PageRequest(pageNo - 1, 10);

        Page<MoneyManage> moneyPage = moneyRepository.findByUserIdAndInstitutionsLikeAndStatusLikeAndTypeLikeOrderByCreateTimeDesc(JWTUtils.getUserSubject(token).getId(), "%" + name + "%", status, type, pageable);
        List<MoneyManage> moneyList = moneyPage.getContent();
        changeMoney(JWTUtils.getUserSubject(token).getId(), moneyList, null, collectRepository, companyRepository, governmentRepository, userRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository,investmentRepository);

        return new JSONResult(new PageResult<>(moneyPage.getTotalElements(), moneyList));
    }

    @GetMapping("money/searchName")
    @ApiOperation("名称搜索资金")
    @ApiImplicitParam(name = "name", value = "资金机构名")
    public JSONResult isMoney(@RequestHeader(required = false) String token, HttpServletResponse rp, @RequestParam(name = "name") String name, HttpServletRequest rq) {

        if (StringUtils.isBlank(token)) {
            rp.setStatus(4001);
            return new JSONResult("未登录", 4001);
        }
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        if (StringUtils.isBlank(name)) {
            return new JSONResult("机构名称不可为空!", 400);
        }

        List<MoneyManage> search = moneyRepository.findByInstitutionsLikeAndStatus("%" + name + "%", "1");


        return new JSONResult(search);
    }


    public static void changeMoney(long userCode, List<MoneyManage> moneys, MoneyManage moneyOne, CollectRepository collectRepository, AuditingCompanyRepository companyRepository,
                                   AuditingGovernmentRepository governmentRepository, UserRepository userRepository,
                                   ClassifyRealmRepository classifyRealmRepository,
                                   ClassifyRotationRepository classifyRotationRepository, InvestStyleRepository investStyleRepository,GroupRepository groupRepository,InvestmentRepository investmentRepository) {

        if (moneys == null) {
            moneys = new ArrayList<>();
            if (moneyOne != null) {
                moneys.add(moneyOne);
            }
        }

        for (MoneyManage money : moneys) {
            
            //todo 小组成员
            if(StringUtils.isNotBlank(money.getTeam())){
                money.setGroup(groupRepository.findByMmCode(money.getTeam()));

            }

            //todo 投资案例
            if(StringUtils.isNotBlank(money.getInvestments())) {
                money.setInvestment(investmentRepository.findByCode(money.getInvestments()));
            }

            /*if (collectRepository.countByCodeAndUserIdAndType(money.getCode(), userCode, "2")!=0) {
                money.setIsColler("已收藏");
            } else {
                money.setIsColler("未收藏");
            }*/
            if (StringUtils.isNotBlank(money.getType())) {
                if ("2".equals(money.getType())) {
                    //转换公司认证
                    AuditingCompany company = companyRepository.findOne(money.getCompanyId());
                    money.setCompany(company);
                }


                if ("3".equals(money.getType())) {
                    //转换政府认证
                    AuditingGovernment government = governmentRepository.findByCodeAndAgStatus(userRepository.findOne(money.getUserId()).getCode(), "1");
                    money.setGovernment(government);
                }
            }

            //转换领域
            if (StringUtils.isNotBlank(money.getTerritory())) {
                String[] territorys = money.getTerritory().replace("\"", "").replace("[", "").replace("]", "").split(",");
                territorys = classifyRealmRepository.findNameById(territorys);
                money.setTerritory(Arrays.toString(territorys).replace("[", "").replace("]", ""));
            }
            //转换轮次
            if (StringUtils.isNotBlank(money.getLoveRound())) {
                money.setLoveRound(classifyRotationRepository.findNameById(money.getLoveRound()));
            }

            //投资方式
            if (StringUtils.isNotBlank(money.getWay())) {
                money.setWay(investStyleRepository.findOneNameById(money.getWay()));
            }
        }
    }


    /**
     * 精选资金
     */
    @GetMapping("money/quality")
    @ApiOperation(value = "获取精选资金", response = MoneyManage.class)
    @ApiImplicitParam(name = "size", value = "精选数量")
    public JSONResult quality(@RequestParam int size) {
        String[] status = {"1", "5"};
        Pageable pageable = new PageRequest(0, size);
        Page<MoneyManage> moneys = moneyRepository.findByQualityAndStatusInOrderByAuditingTimeDesc("Y", status, pageable);
        return new JSONResult(moneys.getContent());
    }

    /**
     * 最新资金
     */
    @GetMapping("money/newList")
    @ApiOperation(value = "获取最新资金", response = MoneyManage.class)
    @ApiImplicitParam(name = "size", value = "数量")
    public JSONResult newList(int size) {

        Pageable pageable = new PageRequest(0, size);
        List<MoneyManage> moneyList = moneyRepository.findByStatusOrderByCreateTimeDesc("1", pageable).getContent();
        changeMoney(0, moneyList, null, collectRepository, companyRepository, governmentRepository, userRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository,investmentRepository);
        return new JSONResult(moneyList);
    }

    /**
     * 猜你喜欢
     */
    @GetMapping("money/loveList")
    @ApiOperation(value = "获取猜你喜欢", response = MoneyManage.class)
    @ApiImplicitParam(name = "size", value = "数量")
    public JSONResult love(int size) {
        return quality(size);
    }


    /**
     * 行业精选
     */
    @GetMapping("money/industryList")
    @ApiOperation(value = "获取行业精选", response = MoneyManage.class)
    @ApiImplicitParam(name = "size", value = "数量")
    public JSONResult industryList(int size) {
        return quality(size);
    }


    @Autowired
    private TermSheetRepository termSheetRepository;


    //成功案列
    @GetMapping("/money/success")
    @ApiOperation("成功案列")
    public JSONResult succeed(@RequestParam(name = "size", required = true) Integer size) {
        Pageable pageable = new PageRequest(0, size);
        List<String> mmCodes = termSheetRepository.findMoneySuccess();
        Page<MoneyManage> moneys = moneyRepository.findByCodeInAndStatus(mmCodes, "1", pageable);
        List<MoneyManage> moneyList = moneys.getContent();
        changeMoney(0, moneyList, null, collectRepository, companyRepository, governmentRepository, userRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository,investmentRepository);
        return new JSONResult(moneyList);
    }


}

