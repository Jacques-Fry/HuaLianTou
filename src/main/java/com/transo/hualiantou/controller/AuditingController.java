package com.transo.hualiantou.controller;

import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.mapper.AuditingMapper;
import com.transo.hualiantou.pojo.Auditing;
import com.transo.hualiantou.pojo.AuditingPersonal;
import com.transo.hualiantou.repository.AuditingCompanyRepository;
import com.transo.hualiantou.repository.AuditingGovernmentRepository;
import com.transo.hualiantou.repository.AuditingPersonalRepository;
import com.transo.hualiantou.repository.UserRepository;
import com.transo.hualiantou.token.JWTUtils;
import com.transo.hualiantou.token.UserSubject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jack_YD
 * @create 2019/8/21 14:02
 */
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "认证信息模块")
public class AuditingController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    @Autowired
    private AuditingPersonalRepository personalRepository;
    @Autowired
    private AuditingCompanyRepository companyRepository;
    @Autowired
    private AuditingGovernmentRepository governmentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditingMapper auditingMapper;


    @GetMapping("getAuditing")
    @ApiOperation(value = "获取用户所有认证简略信息记录",response = Auditing.class)
    public JSONResult getAuditing(@RequestHeader String token, HttpServletRequest rq, HttpServletResponse rp){
        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }

        List<Auditing> auditing= auditingMapper.getAuditing(JWTUtils.getUserSubject(token).getCode());
        return new JSONResult(auditing);
    }

    @GetMapping("getPassAuditing")
    @ApiOperation(value = "获取用户所有已通过的认证信息")
    public JSONResult get(@RequestHeader String token,HttpServletRequest rq,HttpServletResponse rp){
        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }

        Map<String,Object> map=new HashMap<>();

        String code=JWTUtils.getUserSubject(token).getCode();
        map.put("auditingType",userRepository.findByCode(code).getAuditingType());
        map.put("personal",personalRepository.findByCodeAndApStatus(code,"1"));
        map.put("company",companyRepository.findAllByAcStatusAndCode("1",code));
        map.put("government",governmentRepository.findByCodeAndAgStatus(code,"1"));

        return new JSONResult(map);

    }

    @GetMapping("getAuditingStatus")
    @ApiOperation(value = "获取用户当前认证状态")
    public JSONResult getStatus(@RequestHeader String token,HttpServletRequest rq,HttpServletResponse rp){
        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }
        
        Map<String,Object> map=new HashMap<>();

        UserSubject userSubject=JWTUtils.getUserSubject(token);

        int isReal=personalRepository.countByApStatusAndCode("1",userSubject.getCode());

        String personalStatus="-1";
        if(isReal!=0){
            AuditingPersonal personal=personalRepository.findByCodeAndApStatus(userSubject.getCode(),"1");
            personalStatus="1";
            map.put("personal",personal);

            List<Auditing> auditings =auditingMapper.getAuditingStatus(userSubject.getCode());

            if(auditings.size()!=0){
                //map.put("auditingNowStatus",auditings.get(0).getStatus());
                map.put("auditingNow",auditings.get(0));
                //map.put("auditingNowFail",auditings.get(0).getFail());
                int CisReal=companyRepository.findByAcStatusAndCode("1",userSubject.getCode());
                int GisReal=governmentRepository.findByAgStatusAndCode("1",userSubject.getCode());
                if(CisReal!=0&&GisReal==0){
                    map.put("company",companyRepository.findAllByAcStatusAndCode("1",userSubject.getCode()));
                }
                if(GisReal!=0){
                    map.put("government",governmentRepository.findByCodeAndAgStatus(userSubject.getCode(),"1"));
                }
            }else{
                map.put("auditingStatus","-1");
            }

        }else{

            AuditingPersonal personal=personalRepository.findByCodeAndApStatus(userSubject.getCode(),"%");
            if(personal!=null){
                if("2".equals(personal.getApStatus())){
                    map.put("personalFail",personal.getApFail());
                }
                personalStatus=personal.getApStatus();
                map.put("personal",personal);
            }

        }


        map.put("personalStatus",personalStatus);

        return new JSONResult(map);
    }


    @GetMapping("getAudit/{type}/{id}")
    @ApiOperation(value = "获取某个认证的详细信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "认证类型(1.个人认证,2.企业认证,3.政府认证)", required = true, dataType = "字符", paramType = "url参数"),
            @ApiImplicitParam(name = "id", value = "所属认证id", required = true, dataType = "数字", paramType = "url参数")})
    public JSONResult getAudit(@PathVariable(value = "type") String type,@PathVariable(value = "id") long id){
        if("1".equals(type)){
            return new JSONResult(personalRepository.findOne(id));
        }else if("2".equals(type)){
            return new JSONResult(companyRepository.findOne(id));
        }else if("3".equals(type)){
            return new JSONResult(governmentRepository.findOne(id));
        }
        return new JSONResult("请传输正确的参数,谢谢!",400);
    }
}
