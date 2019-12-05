package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.mapper.AuditingMapper;
import com.transo.hualiantou.pojo.*;
import com.transo.hualiantou.repository.*;
import com.transo.hualiantou.service.RemindService;
import com.transo.hualiantou.service.UserMemberService;
import com.transo.hualiantou.utils.CuccSMS;
import com.transo.hualiantou.utils.DateTime;
import com.transo.hualiantou.utils.Encrypt;
import com.transo.hualiantou.utils.IPaddress;
import com.transo.hualiantou.token.JWTUtils;
import com.transo.hualiantou.token.UserSubject;
import io.swagger.annotations.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Jack_YD
 * @create 2019/8/19 11:06
 */

@CrossOrigin(origins = "*")
@RestController
@Api(tags = "用户模块")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;//redis服务

  @Value("${HmacSHA1.key}")
  private String PasswordKey;

  @Autowired
  private UserMemberService userMemberService;

  @Autowired
  private RemindRepository remindRepository;

  @Autowired
  private AuditingPersonalRepository personalRepository;
  @Autowired
  private AuditingCompanyRepository companyRepository;
  @Autowired
  private AuditingGovernmentRepository governmentRepository;

  @Autowired
  private UserMemberRepository userMemberRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ProjectManageRepository  projectManageRepository;

  @Autowired
  private MoneyManageRepository moneyManageRepository;


  @Autowired
  private AuditingMapper auditingMapper;

  //@CrossOrigin(origins = "*")
  @PostMapping("register")
  @ApiOperation(value = "注册")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "11位字符串", paramType = "application/json"),
      @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "字符串", paramType = "application/json"),
      @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "字符串", paramType = "application/json")
  })
  public JSONResult register(@RequestBody JSONObject object, HttpServletRequest rq, HttpServletResponse rp) throws Exception {
    String phone = object.getString("phone");
    String password = object.getString("password");
    String code = object.getString("code");
    if (StringUtils.isBlank(phone)) {
      return new JSONResult("手机号不能为空!", 400);
    }
    if (!pwsVerify(password)) {
      return new JSONResult("密码格式至少一个字母,一个数字，在6~16个字符之间!", 400);
    }
    if (StringUtils.isBlank(code)) {
      return new JSONResult("验证码不能为空!", 400);
    }
    String oldCode = redisTemplate.boundValueOps(phone + "register").get() + "";
    if (!oldCode.equals(code)) {
      return new JSONResult("验证码不存在或失效!", 400);
    }
    if (userRepository.findphone(phone) != 0) {
      return new JSONResult("号码已被注册!", 400);
    }

    User user = new User();
    user.setCreateTime(new Date());
    user.setPhone(phone);
    user.setPhoto("/base/userhead.jpg");
    user.setTagCode("000000");
    user.setCode(UUID.randomUUID().toString());
    user.setPassword(Encrypt.getInstance().SHA1(password, PasswordKey));
    user.setMemberType("0");//注册会员
    user = userRepository.save(user);

    //生成用户会员
    userMemberService.save(user.getId());

    UserSubject userSubject = new UserSubject(user.getId(), user.getPhone(), user.getCode());

    String token = JWTUtils.createJWT(IPaddress.getIpAddress(rq), "login", JWTUtils.generalSubject(userSubject), 60 * 60 * 1000 * 24 * 14);
    redisTemplate.boundValueOps(token).set("1", 2, TimeUnit.HOURS);
    token = "{\"token\":\"" + token + "\"}";

    //清除redis验证码缓存
    redisTemplate.boundValueOps(phone + "login").set("", 1, TimeUnit.MICROSECONDS);

    return new JSONResult(JSON.parse(token));
  }

  private boolean pwsVerify(String password) {
    //通过返回 true
    String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
    if (password.matches(regex)) {
      return true;
    }

    return false;
  }


  //@CrossOrigin(origins = "*")
  @PostMapping("loginCode")
  @ApiOperation(value = "手机号+验证码登录")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "11位字符串", paramType = "application/json"),
      @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "字符串", paramType = "application/json")
  })
  public JSONResult loginCode(@RequestBody JSONObject object, HttpServletRequest rq, HttpServletResponse rp) throws Exception {
    JSONResult jsonResult = null;
    String phone = object.getString("phone");
    String oldCode = redisTemplate.boundValueOps(phone + "login").get() + "";
    //匹配验证码
    if (oldCode.equals(object.getString("code"))) {
      User user = userRepository.findByphone(phone);
      if (user == null) {
        //没注册登录
        //rp.setStatus(5001);
        return new JSONResult("该手机号未注册!", 5001);
      }
      UserSubject userSubject = new UserSubject(user.getId(), user.getPhone(), user.getCode());
      String token = JWTUtils.createJWT(IPaddress.getIpAddress(rq), "login", JWTUtils.generalSubject(userSubject), 60 * 60 * 1000 * 24 * 14);
      redisTemplate.boundValueOps(token).set("1", 2, TimeUnit.HOURS);
      token = "{\"token\":\"" + token + "\"}";
      jsonResult = new JSONResult(JSON.parse(token));
      //清除redis验证码缓存
      redisTemplate.boundValueOps(phone + "login").set("", 1, TimeUnit.MICROSECONDS);
    } else {
      jsonResult = new JSONResult("验证码不存在或者过期!", 400);
    }


    return jsonResult;
  }

  @PostMapping("loginPwd")
  @ApiOperation(value = "手机号+密码登录")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "11位字符串", paramType = "application/json"),
      @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "字符串", paramType = "application/json"),
      @ApiImplicitParam(name = "object", value = "不要理会我", required = true, dataType = "不要理会我", paramType = "不要理会我")
  })
  public JSONResult loginPwd(@RequestBody JSONObject object, HttpServletRequest rq) throws Exception {
    JSONResult jsonResult = null;
    String password = object.getString("password");
    if (StringUtils.isEmpty(password)) {
      return new JSONResult("密码不可为空!", 400);
    }
    password = Encrypt.getInstance().SHA1(password, PasswordKey);
    User user = userRepository.findphoneAndpassword(object.getString("phone"), password);
    if (user != null) {
      UserSubject userSubject = new UserSubject(user.getId(), user.getPhone(), user.getCode());

      String token = JWTUtils.createJWT(IPaddress.getIpAddress(rq), "login", JWTUtils.generalSubject(userSubject), 60 * 60 * 1000 * 24 * 14);
      redisTemplate.boundValueOps(token).set("1", 2, TimeUnit.HOURS);
      token = "{\"token\":\"" + token + "\"}";
      jsonResult = new JSONResult(JSON.parse(token));
    } else {
      jsonResult = new JSONResult("手机号和密码不匹配!", 400);
    }

    return jsonResult;
  }


  @GetMapping("getUserInfo")
  @ApiOperation(value = "获取用户数据", tags = "", response = User.class)
  public JSONResult getUser(@RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq) throws Exception {
    if (StringUtils.isBlank(token)) {
      rp.setStatus(4001);
      return new JSONResult("未登录请先登录!", 4001);
    }
    if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
      rp.setStatus(4000);
      return new JSONResult("token过期或无效!", 4000);
    }
    Map<String, Object> map = new HashMap<>();
    long id = JWTUtils.getUserSubject(token).getId();
    User user = userRepository.findOne(JWTUtils.getUserSubject(token).getId());
    Integer isReal = personalRepository.countByApStatusAndCode("1", user.getCode());
    if (isReal != 0) {
      user.setIsReal("Y");
    } else {
      user.setIsReal("N");
    }

    List<Auditing> auditings = auditingMapper.getAuditing(user.getCode());

    if (auditings.size() != 0) {
      user.setAuditingNowStatus(auditings.get(0).getStatus());
    } else {
      user.setAuditingNowStatus("-1");
    }

    //会员信息
    UserMember userMember = userMemberRepository.findTop1ByUserId(id);

    if (userMember != null) {

      Member memberNow = memberRepository.findByCode(userMember.getMemberCode());

      user.setIcon(memberNow.getIcon());

      user.setMemberName(memberNow.getName());

      if (!StringUtils.equals(user.getMemberType(), "0")) {

        //是否到期
        if (userMemberRepository.Expires(new Date(), id) == 0) {
          //TODO 会员到期信息变更
          //用户
          user.setMemberType("0");
          //用户会员
          Member member = memberRepository.findByType("0");
          if (member == null) {
            throw new RuntimeException("注册会员信息不存在!");
          }

          if (StringUtils.equals(user.getAuditingType(), "1")) {
            userMember.setMoneyNumber(member.getMoneyNumberPersonal());
            userMember.setProjectNumber(member.getProjectNumberPersonal());

          } else if (StringUtils.equals(user.getAuditingType(), "2")) {
            userMember.setMoneyNumber(member.getMoneyNumberCompany());
            userMember.setProjectNumber(member.getProjectNumberCompany());

          } else if (StringUtils.equals(user.getAuditingType(), "3")) {
            userMember.setMoneyNumber(member.getMoneyNumberGovernment());
            userMember.setProjectNumber(member.getProjectNumberGovernment());

          }

          userRepository.save(user);

          userMember=userMemberRepository.save(userMember);

        }
      }

    } else {
      //生成用户会员
      userMemberService.save(user.getId());

      userMember = userMemberRepository.findTop1ByUserId(user.getId());
    }

    //消息
    Map<String,Object> remind=new HashMap<>();
    remind.put("count",remindRepository.countTotal(user.getId(), "0"));
    remind.put("systemRemind",remindRepository.findByUserIdAndStatus(user.getId(), "0","0"));
    remind.put("deliveryRemind",remindRepository.findByUserIdAndStatus(user.getId(), "0","1"));
    remind.put("termSheetRemind",remindRepository.findByUserIdAndStatus(user.getId(), "0","2"));
    user.setRemind(remind);

    //项目信息
    String[] projectStatus={"0","1","3"};
    Integer projectCount = projectManageRepository.countByUserIdAndStatusIn(user.getId(), projectStatus);
    Map<String,Object> project=new HashMap<>();
    project.put("nowCount",projectCount);
    project.put("maxCount",userMember.getProjectNumber());
    user.setProject(project);

    //资金信息
    String[] moneyStatus={"0","1","3"};
    Integer moneyCount = moneyManageRepository.countByUserIdAndStatusIn(user.getId(), moneyStatus);
    Map<String,Object> money=new HashMap<>();
    money.put("nowCount",moneyCount);
    money.put("maxCount",userMember.getMoneyNumber());
    user.setMoney(money);

    if (StringUtils.isNotBlank(user.getPassword())) {
      user.setPassword("Y");
    }

    map.put("user", user);

    return new JSONResult(map);
  }


  /**
   * 修改密码
   */
  @PutMapping("changePassword")
  @ApiOperation(value = "修改密码")
  @ApiImplicitParams({@ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "application/json"),
      @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "application/json")
  })
  public JSONResult changePassword(@RequestHeader(required = false) String token, HttpServletResponse rp, HttpServletRequest rq, @RequestBody JSONObject obj) {

    if (StringUtils.isBlank(token)) {
      rp.setStatus(4001);
      return new JSONResult("未登录请先登录!", 4001);
    }
    if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
      rp.setStatus(4000);
      return new JSONResult("token过期或无效!", 4000);
    }
    String code = obj.getString("code");
    String password = obj.getString("password");

    if (!pwsVerify(password)) {
      return new JSONResult("密码格式至少一个字母,一个数字，在6~16个字符之间!", 400);
    }

    String phone = JWTUtils.getUserSubject(token).getPhone();
    String realCode = redisTemplate.boundValueOps(phone + "changePassword").get() + "";
    if (!realCode.equals(code)) {
      return new JSONResult("验证码不存在或者过期!", 400);
    }

    userRepository.updatePassword(phone, Encrypt.getInstance().SHA1(password, PasswordKey));
    //清除redis验证码缓存
    redisTemplate.boundValueOps(phone + "changePassword").set("", 1, TimeUnit.MICROSECONDS);

    return new JSONResult();
  }


  @PostMapping("getVerifyCode")
  @ApiOperation(value = "获取登录验证码")
  @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "11位字符串", paramType = "application/json")
  })
  public JSONResult getVerifyCode(@RequestBody JSONObject object, HttpServletResponse rp) {
    String phone = object.getString("phone");
    String regex = "^(\\d{11})$";
    if (!phone.matches(regex)) {
      return new JSONResult("手机号码不合法!", 400);
    } else if (userRepository.findphone(phone) == 0) {
      //rp.setStatus(5001);
      return new JSONResult("该手机号未注册!", 5001);
    } else {
      return getMessage(phone, "login");
    }
  }


  @PostMapping("getRegisterCode")
  @ApiOperation(value = "获取注册验证码")
  @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "11位字符串", paramType = "application/json")
  })
  public JSONResult getRegisterCode(@RequestBody JSONObject object, HttpServletResponse rp) {
    String phone = object.getString("phone");
    String regex = "^(\\d{11})$";
    if (!phone.matches(regex)) {
      return new JSONResult("手机号码不合法!", 400);
    } else if (userRepository.findphone(phone) != 0) {
      return new JSONResult("号码已被注册!", 400);
    } else {
      return getMessage(phone, "register");
    }
  }


  @PostMapping("getModifyCode")
  @ApiOperation(value = "获取修改密码验证码")
  public JSONResult getModifyCode(@RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq) {

    if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
      rp.setStatus(4000);
      return new JSONResult("token过期或无效!", 4000);
    }
    return getMessage(JWTUtils.getUserSubject(token).getPhone(), "changePassword");

  }

  @PostMapping("changePhoto")
  @ApiOperation(value = "修改头像")
  @ApiImplicitParams({@ApiImplicitParam(name = "photo", value = "头像", required = true, paramType = "application/json")})
  public JSONResult changePhoto(@RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq, @RequestBody JSONObject object) {

    if (!JWTUtils.verifyToken(token, rq, redisTemplate)) {
      rp.setStatus(4000);
      return new JSONResult("token过期或无效!", 4000);
    }

    String photo = object.getString("photo");
    User user = userRepository.getOne(JWTUtils.getUserSubject(token).getId());
    user.setPhoto(photo);
    userRepository.save(user);
    return new JSONResult();

  }

  public JSONResult getMessage(String phone, String type) {
    String code = redisTemplate.boundValueOps(phone + type).get();

    Long expire = redisTemplate.boundValueOps(phone + type).getExpire();
    if (expire >= 240) {
      return new JSONResult("发送过于频繁，请稍后再试!", 400);
    } else {
      if (StringUtils.isBlank(code)) {
        //验证码
        code = (int) ((Math.random() * 9 + 1) * 100000) + "";
      }
      CuccSMS.sendSMS("505002", phone, "wzcy002", "【华联投】您的验证注册码为：" + code + "，该验证码在10分钟内均有效。如非本人操作，请勿理会。", 0);
      //存入redis
      redisTemplate.boundValueOps(phone + type).set("" + code, 10, TimeUnit.MINUTES);
      return new JSONResult();
    }
  }


}
