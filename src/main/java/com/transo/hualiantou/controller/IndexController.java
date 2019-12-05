package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSONObject;
import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.ReturnsStyle.PageResult;
import com.transo.hualiantou.pojo.ClassifyRealm;
import com.transo.hualiantou.pojo.InvestStyle;
import com.transo.hualiantou.pojo.MoneyManage;
import com.transo.hualiantou.pojo.ProjectManage;
import com.transo.hualiantou.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.transo.hualiantou.controller.MoneyController.changeMoney;
import static com.transo.hualiantou.controller.ProjectManageController.changeProject;

/**
 * @author Jack_YD
 * @create 2019/8/29 11:53
 */

@CrossOrigin(origins = "*")
@RestController
@Api(tags = "主页信息模块")
public class IndexController {
    @Autowired
    private ProjectManageRepository projectManageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvestmentRepository investmentRepository;
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
    private CollectRepository collectRepository;
    @Autowired
    private GroupRepository groupRepository;

    @GetMapping("filter")
    @ApiOperation("筛选条件")
    public JSONResult filter() {
        List<ClassifyRealm> territory = classifyRealmRepository.findAll();
        List<InvestStyle> way = investStyleRepository.findAll();
        Map<String, Object> map = new HashMap();
        map.put("territory", territory);
        map.put("way", way);
        return new JSONResult(map);
    }

    @Autowired
    private MoneyManageRepository moneyRepository;
    @Autowired
    ProjectManageRepository projectRepository;

    //按发布时间、发布的投资/融资金额(排序功能)
    @PostMapping("/sort")
    @ApiOperation(value = "筛选排序功能")
    @ApiImplicitParam(name = "jsonObject", value = "类型(必填)：1.项目 2.资金,所属领域 territory、所在地区 address、融资方式 way、金额最小值min, 金额最大值max 、是否优质 quality" +
            "时间排序 timeSort(1.降序 2.升序)、 金额排序 moneySort(1.降序 2.升序)、综合排序synthesizeSort（1.降序 2.升序）pageNo页码  size每页个数 status 项目状态", dataType = "application/json", required = true)

    public JSONResult sort(@RequestBody JSONObject jsonObject) {
        JSONResult jsonResult = null;
        //类型
        String auType = jsonObject.getString("auType");
        String type = jsonObject.getString("type");
        Integer pageNo = jsonObject.getInteger("pageNo");
        Integer size = jsonObject.getInteger("size");
        Integer min = jsonObject.getInteger("min");
        Integer max = jsonObject.getInteger("max");

        if (StringUtils.isBlank(size + "") || size <= 0) {
            size = 10;
        }
        if (StringUtils.isBlank(pageNo + "") || pageNo <= 0) {
            pageNo = 1;
        }



        //todo 排序问题
        List<Sort.Order> orders = new ArrayList<>();
        String money = jsonObject.getString("moneySort");
        String time = jsonObject.getString("timeSort");
        String synthesize = jsonObject.getString("synthesizeSort");
        Sort.Order synthesizeSort = null;
        Sort.Order moneySort = null;
        Sort.Order timeSort = "1".equals(time)?new Sort.Order(Sort.Direction.DESC, "createTime"):"2".equals(time)?new Sort.Order(Sort.Direction.ASC, "createTime"):null;
        if (StringUtils.isNoneBlank(money) || StringUtils.isNoneBlank(synthesize)) {
            //项目
            if ("1".equals(type)) {
                if ("2".equals(money)) {
                    moneySort = new Sort.Order(Sort.Direction.ASC, "nowMoney");
                } else if ("1".equals(money)) {
                    moneySort = new Sort.Order(Sort.Direction.DESC, "nowMoney");
                }
                if ("2".equals(synthesize)) {
                    synthesizeSort = new Sort.Order(Sort.Direction.ASC, "name");
                } else if ("1".equals(synthesize)) {
                    synthesizeSort = new Sort.Order(Sort.Direction.DESC, "name");
                }
            }
            //资金
            if ("2".equals(type)) {
                if ("2".equals(money)) {
                    moneySort = new Sort.Order(Sort.Direction.ASC, "money");
                } else if ("1".equals(money)) {
                    moneySort = new Sort.Order(Sort.Direction.DESC, "money");
                }
                if ("2".equals(synthesize)) {
                    synthesizeSort = new Sort.Order(Sort.Direction.ASC, "institutions");
                } else if ("1".equals(synthesize)) {
                    synthesizeSort = new Sort.Order(Sort.Direction.DESC, "institutions");
                }
            }
        }
        if (timeSort != null) {
            orders.add(timeSort);
        }
        if (moneySort != null) {
            orders.add(moneySort);
        }
        if (synthesizeSort != null) {
            orders.add(synthesizeSort);
        }
        //排序
        Pageable pageable = new PageRequest(pageNo - 1, size);
        if (orders.size() > 0) {
            Sort sort = new Sort(orders);
            pageable = new PageRequest(pageNo - 1, size, sort);
        }

        //领域
        String territory = jsonObject.getString("territory");
        if (StringUtils.isBlank(territory)) {
            territory = "%";
        }
        //地址
        String address = "%" + jsonObject.getString("address") + "%";
        //方式
        String way = jsonObject.getString("way");
        if (StringUtils.isBlank(way)) {
            way = "%";
        }
        //是否优质
        String quality = jsonObject.getString("quality");
        if (StringUtils.isBlank(quality)) {
            quality = "%";
        }

        //认证类型
        if (StringUtils.isBlank(auType)) {
            auType = "%";
        }

        if ("1".equals(type)) {
            String status=jsonObject.getString("status");
            if(!StringUtils.isBlank(status)&&!StringUtils.equals(status,"1")&&!StringUtils.equals(status,"5")){
                return new JSONResult("参数错误!",400);
            }
            if(StringUtils.isBlank(status)){
                status="1";
            }

            Page<ProjectManage> pro = projectRepository.findAllByStatusAndTerritoryLikeAndAddressLikeAndWayLikeAndQualityLikeAndNowMoneyBetween(status, territory, address, way, quality, min, max, pageable);

            changeProject(0, pro.getContent(), null, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);
            jsonResult = new JSONResult(new PageResult(pro.getTotalElements(), pro.getContent()));
        }

        if ("2".equals(type)) {
            //状态 领域 地址 方式 优质auType 类型 金额
            Page<MoneyManage> moneyManage = moneyRepository.findAllByStatusAndTerritoryLikeAndAddressLikeAndWayLikeAndQualityLikeAndTypeLikeAndMoneyBetween("1", "%\"" + territory + "\"%", address, way, quality, auType, min, max, pageable);

            changeMoney(0, moneyManage.getContent(), null, collectRepository, companyRepository, governmentRepository, userRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository,investmentRepository);
            jsonResult = new JSONResult(new PageResult(moneyManage.getTotalElements(), moneyManage.getContent()));
        }
        return jsonResult;
    }

    @Autowired
    private TermSheetRepository termSheetRepository;

    /**
     * 获取项目主页信息
     */
    @GetMapping("project/summary")
    @ApiOperation(value = "获取项目主页信息")
    public JSONResult projectSummary() {

        Map<String, String> map = new HashMap<>();

        //项目方总数量
        map.put("projectUser", projectManageRepository.countByUser());
        //需求资金总额
        map.put("projectMoneySum", projectManageRepository.moneySum());
        //成功融资数量
        map.put("termSheetCount", termSheetRepository.countByStatus("1"));
        //完成融资金额
        map.put("projectTrading", termSheetRepository.moneyTrading());

        return new JSONResult(map);
    }

    /**
     * 获取资金主页信息
     */
    @GetMapping("monge/summary")
    @ApiOperation(value = "获取资金主页信息")
    public JSONResult summary() {
        Map<String, Object> map = new HashMap<>();
        map.put("moneySide", moneyRepository.findMoneySide());//资金方数
        map.put("moneySum", moneyRepository.sumMoney());//资金总数
        map.put("moneyTrading", termSheetRepository.moneyTrading());//完成融资资金
        map.put("moneyDocking", moneyRepository.countByStatus("1"));//资金数量
        return new JSONResult(map);
    }


    @GetMapping("search")
    @ApiOperation(value = "搜索功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "搜索的字符串"),
            @ApiImplicitParam(name = "pageSize", value = "个数"),
            @ApiImplicitParam(name = "pageNo", value = "页码")
    })
    public JSONResult search(String s, int pageSize, int pageNo) {
        Map<String, Object> date = new HashMap<>();
        String str = "%" + s + "%";
        if (StringUtils.isBlank(s)) {
            str = "";
        }
        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
        //项目名称
        Page<ProjectManage> pros = projectManageRepository.searchByName(str, pageable);
        changeProject(0, pros.getContent(), null, collectRepository, termSheetRepository, companyRepository, governmentRepository, userRepository, classifyTagRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository);
        date.put("projects", new PageResult<>(pros.getTotalElements(), pros.getContent()));
        //资金名称
        Page<MoneyManage> money = moneyRepository.searchByName(str, pageable);
        changeMoney(0, money.getContent(), null, collectRepository, companyRepository, governmentRepository, userRepository, classifyRealmRepository, classifyRotationRepository, investStyleRepository,groupRepository,investmentRepository);
        date.put("moneys", new PageResult<>(money.getTotalElements(), money.getContent()));
        return new JSONResult(date);
    }

}
