package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Map;

/**
 * @author Jack_YD
 * @create 2019/8/13
 */
@Entity
@Table(name = "user")
@ApiModel(value = "用户模型")
@Getter
@Setter
public class User {
  @Id
  @Column(name = "us_uid", length = 11, columnDefinition = "bigint COMMENT'用户id'")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "用户id")
  private Long id;

  @Pattern(regexp = "^(\\d{11})$", message = "手机号不合法!")
  @Column(name = "us_phone", length = 11, columnDefinition = "varchar(11) COMMENT'手机号'")
  @ApiModelProperty(value = "手机号", required = true)
  private String phone;

  @Column(name = "us_photo", columnDefinition = "varchar(255) COMMENT'头像'")
  @ApiModelProperty(value = "头像")
  private String photo;


  //    @NotBlank(message = "密码不可为空!")
  @Column(name = "us_password", length = 255, columnDefinition = "varchar(50) COMMENT'密码'")
  @ApiModelProperty(value = "密码"/*, required = true*/)
  private String password;

  @Column(name = "us_code", length = 255, columnDefinition = "varchar(255) COMMENT'认证编码'")
  @ApiModelProperty(value = "认证编码")
  private String code;

  @Column(name = "us_authority", columnDefinition = "varchar(5) COMMENT'权限'")
  @ApiModelProperty(value = "权限")
  private String authority;

  @Column(name = "us_create_time", columnDefinition = "TIMESTAMP comment'注册时间'", nullable = false, updatable = false)
  @ApiModelProperty(value = "注册时间")
  private Date createTime = new Date();

  @Column(name = "us_auditing_type", length = 1, columnDefinition = "varchar(5) COMMENT'认证角色（0.未认证,1个人,2企业,3政府'")
  @ApiModelProperty(value = "认证角色（0.未认证,1个人,2企业,3政府")
  private String auditingType = "0";

  @Column(name = "us_tag_code", columnDefinition = "varchar(255) COMMENT'第三方标记编码'")
  @ApiModelProperty(value = "第三方标记编码")
  private String tagCode;

  @Column(name = "us_isreal", length = 1, columnDefinition = "varchar(5) COMMENT'是否实名认证 （Y:是 N:否）'")
  @ApiModelProperty(value = "是否实名认证 （Y:是 N:否）")
  private String isReal = "N";

  @ApiModelProperty(value = "会员名称")
  @Transient
  private String memberName;

  @Column(name = "us_member_type")
  @ApiModelProperty(value = "会员类型")
  private String memberType;

  @ApiModelProperty(value = "会员图标")
  @Transient
  private String icon;

  @ApiModelProperty(value = "当前认证状态")
  @Transient
  private String auditingNowStatus;

  @ApiModelProperty(value = "项目信息")
  @Transient
  private Map<String, Object> project;

  @ApiModelProperty(value = "资金信息")
  @Transient
  private Map<String, Object> money;

  @ApiModelProperty(value = "消息信息")
  @Transient
  private Map<String, Object> remind;

  public User() {
  }

  public User(String phone, String photo, String password, String code, String authority, Date createTime, String auditingType, String isReal) {
    this.phone = phone;
    this.photo = photo;
    this.password = password;
    this.code = code;
    this.authority = authority;
    this.createTime = createTime;
    this.auditingType = auditingType;
    this.isReal = isReal;
  }

  @Override
  public String toString() {
    return "{\"User\":{"
        + "\"id\":"
        + id
        + ",\"phone\":\""
        + phone + '\"'
        + ",\"photo\":\""
        + photo + '\"'
        + ",\"password\":\""
        + password + '\"'
        + ",\"code\":\""
        + code + '\"'
        + ",\"authority\":\""
        + authority + '\"'
        + ",\"createTime\":\""
        + createTime + '\"'
        + ",\"auditingType\":\""
        + auditingType + '\"'
        + ",\"tagCode\":\""
        + tagCode + '\"'
        + ",\"isReal\":\""
        + isReal + '\"'
        + ",\"memberName\":\""
        + memberName + '\"'
        + ",\"memberType\":\""
        + memberType + '\"'
        + ",\"icon\":\""
        + icon + '\"'
        + ",\"auditingNowStatus\":\""
        + auditingNowStatus + '\"'
        + ",\"project\":"
        + project
        + ",\"remind\":"
        + remind
        + "}}";

  }
}
