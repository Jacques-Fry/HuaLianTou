package com.transo.hualiantou.controller;

import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.utils.Transform;
import com.transo.hualiantou.pojo.*;
import com.transo.hualiantou.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "通用接口")
public class generaldutyController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CollectRepository collectRepository;

    @Autowired
    private ClassifyRealmRepository realmRepository;

    @Autowired
    private ClassifyRotationRepository rotationRepository;

    @Autowired
    private ClassifyTagRepository tagRepository;

    @Autowired
    private ProjectRateRepository rateRepository;

    @Autowired
    private ProjectManageRepository projectRepository;

    @Autowired
    private InvestStyleRepository styleRepository;

    @Autowired
    private MoneyManageRepository moneyRepository;


    @GetMapping("getRealmTag")
    @ApiOperation("获取所有的领域和标签")
    public JSONResult getRealmTag() {
        Map<String, Object> map = new HashMap<>();
        List<ClassifyRealm> realms = realmRepository.findAll();
        //融资方式
        List<InvestStyle> styles = styleRepository.findAll();
        //轮次
        List<ClassifyRotation> rotations = rotationRepository.findAll();

        map.put("realms", Transform.objectsToMaps(realms));
        map.put("styles", styles);
        map.put("rotations", rotations);

        return new JSONResult(map);
    }




}
