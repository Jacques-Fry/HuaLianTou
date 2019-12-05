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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*")
@RestController
@Api(tags = "项目管理模块")
public class ProjectManageController {
    @Autowired
    private ProjectManageRepository projectManageRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

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
    private UserMemberService userMemberService;

    @PostMapping("/project")
    @ApiOperation(value = "添加项目", response = ProjectManage.class)
    public JSONResult save(@RequestHeader String token, @RequestBody JSONObject obj, HttpServletRequest rq) {
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            return new JSONResult("token过期或无效!", 4000);
        }
        try {
            //获取标签
            JSONArray labelJSON = obj.getJSONArray("label");

            if (labelJSON == null) {
                return new JSONResult("标签不可为空!", 400);
            }
            obj.remove("label");

            ProjectManage project = JsonUtil.jsonToPojo(obj.toString(), ProjectManage.class);
            project.setLabel(labelJSON.toString());
            long id = JWTUtils.getUserSubject(token).getId();
            //获取用户
            User user = userRepository.findOne(id);

            /*---会员权限新增---*/
            String status[] = {"0", "1","3"};
            Integer num = projectManageRepository.countByUserIdAndStatusIn(id, status);
            Integer projectRow = userMemberService.projectRow(user.getId());
            if (projectRow <= num) {
                return new JSONResult("创建项目数量已达上限，无法创建！更多权益请查看会员权益。", 6001);
            }
            /*---会员权限新增---*/

            if ("0".equals(user.getAuditingType())) {
                return new JSONResult("抱歉,个人认证未通过,无法发布任何信息!", 400);
            }

            if (StringUtils.isEmpty(user.getAuditingType()) && !"1".equals(project.getType())) {
                return new JSONResult("政府或企业认证才能发布个人以外的项目!", 400);
            }


            if ("1".equals(project.getType())) {
                if ("3".equals(user.getAuditingType())) {
                    return new JSONResult("政府不可发布个人项目!", 400);
                }
                AuditingPersonal personal = personalRepository.findByCodeAndApStatus(user.getCode(), "1");
                project.setAuditingName(personal.getRealName());
            } else if ("2".equals(project.getType())) {
                if ("3".equals(user.getAuditingType())) {
                    return new JSONResult("政府不可发布企业项目!", 400);
                } else if (!"2".equals(user.getAuditingType())) {
                    return new JSONResult("您没有认证通过的企业认证,不可发布企业项目!", 400);
                }
                AuditingCompany company = companyRepository.findByIdAndAcStatus(project.getCompanyId(), "1");
                project.setAuditingName(company.getCompanyName());
            } else if ("3".equals(project.getType())) {
                if (!"3".equals(user.getAuditingType())) {
                    return new JSONResult("您政府认证未通过,不可发布政府项目!", 400);
                }
                AuditingGovernment government = governmentRepository.findByCodeAndAgStatus(user.getCode(), "1");
                project.setAuditingName(government.getUnitsName());
            } else {
                return new JSONResult("没有选择项目类型!", 400);
            }

            if (StringUtils.isBlank(project.getLogo())) {
                project.setLogo("/base/ProjectAndMoneyDefault.png");
            }

            if(StringUtils.equals(project.getBeforeRound(),"-1")){
                project.setBeforeRound("");
            }

            project.setCode(FileUtils.getUUID());
            project.setStatus("0");
            project.setQuality("N");
            project.setUserId(user.getId());
            //团队多存少存无所谓,保证项目只存一次
            String groupCode = FileUtils.getUUID();
            if(project.getGroup()!=null){
                for (Group group : project.getGroup()) {
                    group.setPmCode(groupCode);
                    groupRepository.save(group);
                }
            }

            project.setTeam(groupCode);
            projectManageRepository.save(project);

        } catch (ConstraintViolationException e) {
            JSONResult jsonResult = null;
            for (ConstraintViolation<?> c : e.getConstraintViolations()) {
                jsonResult = new JSONResult(c.getMessage(), 400);
            }
            return jsonResult;
        }

        return new JSONResult();
    }


    @Autowired
    private GroupRepository groupRepository;

    @PutMapping("/project")
    @ApiOperation(value = "修改项目", response = ProjectManage.class)
    public JSONResult put(@RequestHeader String token, @RequestBody JSONObject obj, HttpServletRequest rq, HttpServletResponse rp) {

        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        try {
            //获取标签
            JSONArray labelJSON = obj.getJSONArray("label");

            if (labelJSON == null) {
                return new JSONResult("标签不可为空!", 400);
            }
            if (StringUtils.isBlank("" + obj.getLong("id"))) {
                return new JSONResult("未解析到项目ID!", 400);
            }

            long id = JWTUtils.getUserSubject(token).getId();

            ProjectManage project = projectManageRepository.findOne(obj.getLong("id"));

            if (project == null) {
                return new JSONResult("未获取到资金信息,数据可能已不存在!", 400);
            }

            if (id != project.getUserId()) {
                rp.setStatus(5000);
                return new JSONResult("您没有权限修改此项目信息!", 5000);
            }

            //删除之前所有的小组成员
            groupRepository.deleteByPmCode(project.getTeam());

            //记录
            Record record = new Record();
            record.setUserId(id);
            record.setNewContent(JSON.toJSONString(project));
            record.setCreaterTime(new Date());
            record.setContentType("1");
            record.setCode(project.getCode());
            record.setOperationType("0");


            project.setStatus("0");
            project.setLogo(obj.getString("logo"));
            project.setBp(obj.getString("bp"));
            project.setUrl(obj.getString("url"));
            project.setContactEmail(obj.getString("contactEmail"));
            project.setContactName(obj.getString("contactName"));
            project.setContactPhone(obj.getString("contactPhone"));
            project.setCreateTime(new Date());

            //todo 小组成员优化
            List<Group> groups = JsonUtil.jsonToList(obj.getJSONArray("group").toJSONString(), Group.class);
            if(groups!=null){
                for (Group group : groups) {
                    group.setPmCode(project.getTeam());
                    groupRepository.save(group);
                }
            }



            /*List<Group> groups2 =new ArrayList<>();
            if(groups!=null){
                for (Group group1 : groups2) {
                    JSONObject o1=(JSONObject) JSONObject.toJSON(group1);
                    o1.remove("id");
                    groups2.add(JsonUtil.jsonToPojo(o1.toJSONString(),Group.class));
                }
            }*/
            //project.setGroup(groups);
            project = projectManageRepository.save(project);

            //记录
            record.setOldContent(JSON.toJSONString(project));
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


    @GetMapping("/projectUser/{id}")
    @ApiOperation("获取用户的单个未转换的项目")
    public JSONResult getOneUser(@RequestHeader(required = false) String token, HttpServletResponse rp, @PathVariable long id, HttpServletRequest rq) {
        if (StringUtils.isBlank(token)) {
            rp.setStatus(4001);
            return new JSONResult("未登录,请先登录!", 4001);
        }

        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }

        ProjectManage project = projectManageRepository.findOne(id);
        if (project == null) {
            rp.setStatus(5001);
            return new JSONResult("未解析到项目数据,数据可能不存在!", 5001);
        }
        //todo 小组成员
        if(StringUtils.isNotBlank(project.getTeam())){
            project.setGroup(groupRepository.findByPmCode(project.getTeam()));
        }

        return new JSONResult(project);
    }

    @Autowired
    private CollectRepository collectRepository;

    @Autowired
    private DataStatisticsService dataStatisticsService;

    @GetMapping("/projectOne/{id}")
    @ApiOperation("获取单个转换后的项目")
    public JSONResult getOne(@RequestHeader(required = false) String token, @PathVariable long id, HttpServletRequest rq, HttpServletResponse rp) {

        if (StringUtils.isBlank(token)) {
            rp.setStatus(4001);
            return new JSONResult("未登录,请先登录!", 4001);
        }

        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!", 4000);
        }
        ProjectManage project = projectManageRepository.findOne(id);


        if (project == null) {
            rp.setStatus(5001);
            return new JSONResult("未解析到项目数据,数据可能不存在!", 5001);
        }

        if (project.getUserId() != JWTUtils.getUserSubject(token).getId() && !"1".equals(project.getStatus()) && !"5".equals(project.getStatus())) {

            rp.setStatus(5000);
            return new JSONResult("该项目可能正在审核中,无法获取详情信息!", 5000);
        }
        if (collectRepository.countByCodeAndUserIdAndType(project.getCode(), JWTUtils.getUserSubject(token).getId(), "1") != 0) {
            project.setIsColler("已收藏");
        } else {
            project.setIsColler("未收藏");
        }

        changeProject(JWTUtils.getUserSubject(token).getId(), null, project, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);
        List<ProjectRate> rates = rateRepository.findAllByProjectCodeAndPrStatusAndTypeLike(project.getCode(), "1", "%");
        project.setRates(rates);

        //图表统计
        project.setStatistical(dataStatisticsService.getStatisticalProject(Calendar.getInstance().get(Calendar.YEAR), project.getCode()));

        return new JSONResult(project);
    }


    @GetMapping("/projects/{status}")
    @ApiOperation("获取所有不同状态的项目")
    public JSONResult getAll(@PathVariable String status) {
        List<ProjectManage> projects = projectManageRepository.findAllByStatus(status);
        changeProject(0, projects, null, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);
        return new JSONResult(projects);
    }


    //名字 类型 审核
    @GetMapping("/projects")
    @ApiOperation("获取个人所有项目并筛选")
    @ApiImplicitParams({@ApiImplicitParam(name = "name", value = "项目名", dataType = "url参数"),
            @ApiImplicitParam(name = "status", value = "项目状态:0.待审核中 1.融资中 2.审核失败 3.系统冻结 4.终止融资 5.完成融资 6. 7. 8.", dataType = "url参数"),
            @ApiImplicitParam(name = "type", value = "项目类型：1、个人项目；2、企业项目；3、政府项目", dataType = "url参数"),
            @ApiImplicitParam(name = "pageNo", value = "分页参数", dataType = "url参数")})
    public JSONResult getAllByUser(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "status", required = false) String status, @RequestParam(name = "type", required = false) String type, @RequestParam(name = "pageNo", required = false) Integer pageNo, @RequestHeader String token, HttpServletRequest rq) {

        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            return new JSONResult("token过期或无效!", 4000);
        }

        long id = JWTUtils.getUserSubject(token).getId();
        String code = JWTUtils.getUserSubject(token).getCode();

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


        Pageable pageable = new PageRequest(pageNo - 1, 10);

        Page<ProjectManage> projects = projectManageRepository.findByUserIdAndNameLikeAndStatusLikeAndTypeLikeOrderByCreateTimeDesc(id, "%" + name + "%", status, type, pageable);

        changeProject(id, projects.getContent(), null, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);

        PageResult<ProjectManage> page = new PageResult<ProjectManage>(projects.getTotalElements(), projects.getContent());

        return new JSONResult(page);
    }





    /*//项目筛选功能（所属领域、所在地区、融资方式、融资金额、是否优质）
    @GetMapping("/project/filter")
    @ApiOperation("项目筛选功能")
    @ApiImplicitParam(name = "object", value = "筛选条件（所属领域territory、所在地区、融资方式way、融资金额amount、是否优质quality）", required = true, dataType = "application/json")
    public JSONResult filter(@RequestBody JSONObject object) {
        List<ProjectManage> projects = null;

        String territory = object.getString("territory");
        if ("不限".equals(territory)) {
            territory = "%";
        }
        String way = object.getString("way");
        if ("不限".equals(way)) {
            way = "%";
        }
        String quality = object.getString("quality");
        if ("不限".equals(quality)) {
            quality = "%";
        }

        String amount = object.getString("amount");
        int start = 0, end = 0;
        if ("不限".equals(amount)) {
            projects = projectManageRepository.findAllByterritoryAndwayAndquality(territory, way, quality);

        } else {
            String[] amounts = StringUtils.split(amount, "~");
            if (amounts.length == 1) {
                start = Integer.parseInt(amounts[0]);
                projects = projectManageRepository.findAllByterritoryAndwayAndqualityANDstart(territory, way, quality, start);
            } else if (amounts.length == 2) {
                start = Integer.parseInt(amounts[0]);
                end = Integer.parseInt(amounts[1]);
                projects = projectManageRepository.findAllByterritoryAndwayAndqualityANDend(territory, way, quality, start, end);
            }
        }
        return new JSONResult(projects);
    }*/


    @PostMapping("projectEnd")
    @ApiOperation(value = "项目终止")
    @ApiImplicitParam(name = "id", value = "项目ID")
    public JSONResult end(@RequestHeader String token, @RequestBody JSONObject obj, HttpServletRequest rq) {

        long id = obj.getLong("id");
        //验证token
        if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
            return new JSONResult("token过期或无效!", 4000);
        }
        int count = projectManageRepository.countById(id);
        if (count < 1) {
            return new JSONResult("未获取到项目信息!", 400);
        }
        projectManageRepository.updStatus(id, "4");

        return new JSONResult();
    }


    public static void changeProject(long userCode, List<ProjectManage> projects, ProjectManage p, CollectRepository collectRepository, TermSheetRepository termSheetRepository, AuditingCompanyRepository companyRepository,
                                     AuditingGovernmentRepository governmentRepository, UserRepository userRepository,
                                     ClassifyTagRepository classifyTagRepository, ClassifyRealmRepository classifyRealmRepository,
                                     ClassifyRotationRepository classifyRotationRepository, InvestStyleRepository investStyleRepository,GroupRepository groupRepository) {

        if (projects == null) {
            projects = new ArrayList<>();
            if (p != null) {
                projects.add(p);
            }
        }

        for (ProjectManage project : projects) {

            //todo 小组成员
            if(StringUtils.isNotBlank(project.getTeam())){
                project.setGroup(groupRepository.findByPmCode(project.getTeam()));
            }

            //收藏转换
            /*if (collectRepository.countByCodeAndUserIdAndType(project.getCode(), userCode,"1")!=0) {
                project.setIsColler(z'z'z'z'z'z'd"已收藏");
            } else {
                project.setIsColler("未收藏");
            }*/

            //计算融资总金额

            String money = termSheetRepository.sumMoney(project.getCode());
            if (StringUtils.isNotBlank(money)) {
                project.setActualMoney(new Integer(money));
            }
            if (StringUtils.isNotBlank(project.getType())) {
                if ("2".equals(project.getType())) {
                    //转换公司认证
                    AuditingCompany company = companyRepository.findOne(project.getCompanyId());
                    project.setCompany(company);
                }

                if ("3".equals(project.getType())) {
                    //转换政府认证
                    AuditingGovernment government = governmentRepository.findByCodeAndAgStatus(userRepository.findOne(project.getUserId()).getCode(), "1");
                    project.setGovernment(government);
                }
            }


            //转换标签
            if (StringUtils.isNotBlank(project.getLabel())) {

                String[] labels = project.getLabel().replace("[", "").replace("]", "").split(",");
                labels = classifyTagRepository.findNameByIdIn(labels);
                project.setLabel(Arrays.toString(labels).replace("[", "").replace("]", ""));
            }

            //转换领域
            if (StringUtils.isNotBlank(project.getTerritory())) {
                project.setTerritory(classifyRealmRepository.findOneNameById(project.getTerritory()));
            }


            //转换轮次
            if (StringUtils.isNotBlank(project.getNowRound())) {
                project.setNowRound(classifyRotationRepository.findNameById(project.getNowRound()));


            }
            if (StringUtils.isNotBlank(project.getBeforeRound())) {
                project.setBeforeRound(classifyRotationRepository.findNameById(project.getBeforeRound()));
            }

            //转换投资方式
            if (StringUtils.isNotBlank(project.getWay())) {
                project.setWay(investStyleRepository.findOneNameById(project.getWay()));
            }
        }
    }


    List<ProjectManage> proList(String quality, String status, int size) {
        List<ProjectManage> project = projectManageRepository.findAllByQualityLikeAndStatus(quality, status);
        List<ProjectManage> loveList = new ArrayList<>();
        if (project.size() > size) {
            for (int i = 0; i < size; i++) {
                int num = (int) (Math.random() * project.size());
                if (loveList.contains(project.get(num))) {
                    i--;
                    continue;
                }
                loveList.add(project.get(num));
            }
        } else {
            loveList = project;
        }
        changeProject(0, loveList, null, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);

        return loveList;
    }

    //项目猜你喜欢
    @GetMapping("/project")
    @ApiOperation("猜你喜欢")
    public JSONResult getLove(@RequestParam(name = "size", required = true) Integer size) {
        return new JSONResult(proList("%", "1", size));
    }

    //优质项目推荐
    @GetMapping("/project/superior")
    @ApiOperation("优质项目推荐")
    public JSONResult getSuperior(@RequestParam(name = "size", required = true) Integer size) {

        Pageable pageable = new PageRequest(0, size);
        Page<ProjectManage> project = projectManageRepository.findAllByQualityLikeAndStatusOrderByQualityDateDesc("Y", "1", pageable);
        changeProject(0, project.getContent(), null, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);
        return new JSONResult(project.getContent());
    }


    //最新项目（排序根据时间）
    @GetMapping("/project/newest")
    @ApiOperation("最新项目")
    public JSONResult getNewest(@RequestParam(name = "size", required = true) Integer size) {
        Pageable pageable = new PageRequest(0, size);
        Page<ProjectManage> list = projectManageRepository.findAllByStatusOrderByCreateTimeDesc("1", pageable);
        List<ProjectManage> projectList = list.getContent();
        changeProject(0, projectList, null, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);
        return new JSONResult(new PageResult(list.getTotalElements(), projectList));
    }

    @Autowired
    private TermSheetRepository termSheetRepository;

    //成功案例
    @GetMapping("/project/case")
    @ApiOperation("成功案例")
    public JSONResult succeed(@RequestParam(name = "size", required = true) Integer size) {
        Pageable pageable = new PageRequest(0, size);
        Page<ProjectManage> projects = projectManageRepository.findByStatusOrderByCreateTimeDesc("5", pageable);
        List<ProjectManage> projectList = projects.getContent();
        changeProject(0, projectList, null, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);

        return new JSONResult(new PageResult(projects.getTotalElements(), projectList));
    }

    //每周推荐
    @GetMapping("/project/week")
    @ApiOperation("每周推荐")
    public JSONResult weekList(@RequestParam(name = "size", required = true) Integer size) {
        String json = redisTemplate.boundValueOps("weeklist").get();
        if (StringUtils.isBlank(json)) {
            System.out.println("没有缓存");
            List<ProjectManage> projectManageList = projectManageRepository.weekLsit(size);
            changeProject(0, projectManageList, null, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);
            List<ProjectManage> list =new ArrayList();
            for (ProjectManage projectManage : projectManageList) {

                ProjectManage p=new ProjectManage();
                p.setId(projectManage.getId());
                p.setName(projectManage.getName());
                p.setLogo(projectManage.getLogo());
                p.setAddress(projectManage.getAddress());
                p.setTerritory(projectManage.getTerritory());
                p.setLabel(projectManage.getLabel());
                list.add(p);
            }
            json = JSONObject.toJSONString(list);
            redisTemplate.boundValueOps("weeklist").set(json, 7, TimeUnit.DAYS);
        }
        return new JSONResult(JSON.parse(json));
    }



}
