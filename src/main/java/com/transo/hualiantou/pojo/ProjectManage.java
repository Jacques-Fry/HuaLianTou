package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "project_manage")
@ApiModel(value = "项目模型")
@Getter
@Setter
public class ProjectManage implements Serializable {

    @Id
    @Column(name = "pm_id", length = 11, columnDefinition = "bigint COMMENT'项目id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "项目id")
    private long id;

    @Column(name = "pm_code", columnDefinition = "varchar(50) COMMENT'项目编码'")
    @ApiModelProperty(value = "项目编码")
    private String code;


    @NotBlank(message = "项目名称不可为空!")
    @Column(name = "pm_name", columnDefinition = "varchar(250) COMMENT'项目名称'")
    @ApiModelProperty(value = "项目名称")
    private String name;

    @NotBlank(message = "项目简介不可为空!")
    @Column(name = "pm_introduce", columnDefinition = "TEXT COMMENT'项目简介'")
    @ApiModelProperty(value = "项目简介")
    private String introduce;

    @Column(name = "pm_logo", columnDefinition = "varchar(255) COMMENT'公司logo'")
    @ApiModelProperty(value = "公司logo")
    private String logo;

    /*@Column(name = "pm_province_code", length = 6)
    @ApiModelProperty(value = "省")
    private String provinceCode;

    @Column(name = "pm_city_code", length = 6)
    @ApiModelProperty(value = "市")
    private String cityCode;

    @Column(name = "pm_area_code", length = 6)
    @ApiModelProperty(value = "区")
    private String areaCode;*/

    @NotBlank(message = "项目地区不可为空!")
    @Column(name = "pm_address", columnDefinition = "varchar(255) COMMENT'项目地区'")
    @ApiModelProperty(value = "项目地区")
    private String address;

    //@NotBlank(message = "详细地址不可为空!")
    @Column(name = "pm_detailed_address", columnDefinition = "varchar(255) COMMENT'详细地址'")
    @ApiModelProperty(value = "详细地址")
    private String detailedAddress;

    @NotBlank(message = "所属领域不可为空!")
    @Column(name = "pm_territory", columnDefinition = "varchar(50) COMMENT'所属领域'")
    @ApiModelProperty(value = "所属领域")
    private String territory;

    @NotBlank(message = "所属标签不可为空!")
    @Column(name = "pm_label", columnDefinition = "varchar(50) COMMENT'所属标签'")
    @ApiModelProperty(value = "所属标签")
    private String label;

    //@Pattern(regexp = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})",message = "url格式不匹配!")
    @Column(name = "pm_url", columnDefinition = "varchar(255) COMMENT'官方地址'")
    @ApiModelProperty(value = "官方地址")
    private String url;

    //@Pattern(regexp = "^[a-z0-9A-Z]+[-|a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$",message = "联系人邮箱格式不匹配!")
    @Column(name = "pm_email", columnDefinition = "varchar(255) COMMENT'邮箱'")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank(message = "融资方式不可为空!")
    @Column(name = "pm_way", columnDefinition = "varchar(50) COMMENT'融资方式'")
    @ApiModelProperty(value = "融资方式")
    private String way;

    //@Pattern(regexp = "^[1-9]\\d*$",message = "当前轮次金额不合法 请输入正整数!")
    @Column(name = "pm_now_money", columnDefinition = "int(11) COMMENT'当前轮次金额'")
    @ApiModelProperty(value = "当前轮次金额")
    private Integer nowMoney;

    //@Pattern(regexp = "^[0-9]\\d*$",message = "去年营业额不合法 请输入非负整数!")
    @Column(name = "pm_turnover", columnDefinition = "int(11) COMMENT'去年营业额'")
    @ApiModelProperty(value = "去年营业额")
    private Integer turnover;

    //@Pattern(regexp = "^[0-9]\\d*$",message = "企业当前净利润不合法 请输入非负整数!")
    @Column(name = "pm_profit", columnDefinition = "int(11) COMMENT'企业当前净利润'")
    @ApiModelProperty(value = "企业当前净利润")
    private Integer profit;

    @Column(name = "pm_before_money", columnDefinition = "int(11) COMMENT'上次获投金额'")
    @ApiModelProperty(value = "上次获投金额")
    private Integer beforeMoney;

    @Column(name = "pm_before_round", columnDefinition = "varchar(55) COMMENT'上次获投轮次'")
    @ApiModelProperty(value = "上次获投轮次")
    private String beforeRound;

    //@Pattern(regexp = "^0\\.\\d[1-9]$|^0\\.[1-9]\\d$|^[1-9](\\.\\d{1,2})?$|^[1-9]\\d(\\.\\d{1,3})?$", message = "上轮投资方占比 请输入0.001-100的数字,小数点后最多三位!")
    @Column(name = "pm_proportion", columnDefinition = "int(3) COMMENT'上轮投资方占比'")
    @ApiModelProperty(value = "上次获投资金方占股比例")
    private Integer proportion;

    @Column(name = "pm_outyear", columnDefinition = "varchar(5) COMMENT'最短退出年限'")
    @ApiModelProperty(value = "最短退出年限")
    private Integer outyear;

    @NotBlank(message = "融资轮次不可为空!")
    @Column(name = "pm_now_round", columnDefinition = "varchar(50) COMMENT'当前融资轮次'")
    @ApiModelProperty(value = "当前融资轮次")
    private String nowRound;

    @Pattern(regexp = "^[1-9][0-9]{0,1}|(0|[1-9][0-9]?)(\\.[0-9]{0,2}[1-9])|100?$", message = "当前轮次投资方占比 请输入0.001-100的数字,小数点后最多三位!")
    @Column(name = "pm_now_proportion", columnDefinition = "varchar(5) COMMENT'当前轮次投资方占比'")
    @ApiModelProperty(value = "当前融资资金方占股比例")
    private String nowProportion;

    @NotBlank(message = "商业计划书bp不可为空!")
    @Column(name = "pm_bp", columnDefinition = "TEXT COMMENT'商业计划书bp'")
    @ApiModelProperty(value = "商业计划书bp")
    private String bp;

    @Column(name = "pm_status", columnDefinition = "varchar (2) Comment '项目状态:0.待审核中 1.融资中 2.审核失败 3.系统冻结 4.终止融资 5.完成融资 6. 7. 8.'")
    @ApiModelProperty(value = "项目状态:0.待审核中 1.融资中 2.审核失败 3.系统冻结 4.终止融资 5.完成融资 6. 7. 8.")
    private String status;

    @Column(name = "pm_fail", columnDefinition = "TEXT COMMENT'审核失败原因'")
    @ApiModelProperty(value = "审核失败原因..")
    private String fail;

    @Column(name = "pm_team", columnDefinition = "varchar(80) COMMENT'项目团队（团队编码)'")
    @ApiModelProperty(value = "项目团队（团队编码）")
    private String team;

    @Column(name = "pm_user", columnDefinition = "bigint COMMENT'创建用户id'")
    @ApiModelProperty(value = "创建用户id")
    private long userId;

    @NotBlank(message = "联系人名字不能为空!")
    @Column(name = "pm_contact_name", columnDefinition = "varchar(50) COMMENT'联系人名字'")
    @ApiModelProperty(value = "联系人名字")
    private String contactName;

    @Pattern(regexp = "^(\\d{11})$", message = "联系人手机号不合法!")
    @Column(name = "pm_contact_phone", length = 11, columnDefinition = "varchar(11) COMMENT'联系人手机号'")
    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;

    //@Pattern(regexp = "^[a-z0-9A-Z]+[-|a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$",message = "联系人邮箱格式不匹配!")
    @Column(name = "pm_contact_email", columnDefinition = "varchar(255) COMMENT'联系人邮箱'")
    @ApiModelProperty(value = "联系人邮箱")
    private String contactEmail;

    @Column(name = "pm_create_time", columnDefinition = "TIMESTAMP comment'项目发布时间'", nullable = false, updatable = false)
    @ApiModelProperty(value = "项目发布时间")
    private Date createTime;

    @Column(name = "pm_quality", columnDefinition = "varchar(5) COMMENT'是否优质(Y:优质,N:为非优质)'")
    @ApiModelProperty(value = "是否优质(Y:优质,N:为非优质)")
    private String quality;

    @Column(name = "pm_docid", columnDefinition = "varchar(255) COMMENT'关联项目审核ID'")
    @ApiModelProperty(value = "关联项目审核ID")
    private String docId;

    @Column(name = "pm_type", columnDefinition = "varchar(5) COMMENT' 项目类型：1、个人项目；2、企业项目；3、政府项目'")
    @ApiModelProperty(value = " 项目类型：1、个人项目；2、企业项目；3、政府项目")
    private String type;

    @Column(name = "pm_company_id", columnDefinition = " bigint COMMENT'该项目所属企业ID，若非企业项目，则为空'")
    @ApiModelProperty(value = "该项目所属企业ID，若非企业项目，则为空")
    private long companyId;



    @Column(name = "pm_auditing_name", columnDefinition = "varchar (255) Comment '个人认证名称'")
    @ApiModelProperty(value = "个人认证名称")
    private String auditingName;


    @Column(name = "pm_auditing_person", columnDefinition = "bigint (64) Comment '审核人'")
    @ApiModelProperty(value = "个人认证名称")
    private Integer auditingPerson;

    @Column(name = "pm_auditing_time", columnDefinition = "DATE Comment '审核时间'")
    @ApiModelProperty(value = "个人认证名称")
    private Date auditingTime;

    @Column(name = "pm_quality_date", columnDefinition = "datetime Comment '设置优质的确切时间'")
    @ApiModelProperty(value = "个人认证名称")
    private Date qualityDate;

    @Transient
    @ApiModelProperty(value = "当前融资金额")
    private long actualMoney;


    @Transient
    private List<Group> group ;

    @Transient
    private AuditingCompany company;

    @Transient
    private AuditingGovernment government;

    @Transient
    private List<ProjectRate> rates;//项目进度

    @Column(name = "pm_iscoller", columnDefinition = "varchar(20) Comment '是否被收藏'")
    private String isColler;


    @ApiModelProperty(value = "图表统计")
    @Transient
    private Map<String, Object> statistical;

    public ProjectManage() {
    }



    public ProjectManage(long id, String name, String introduce, String logo, String address, String territory, String label) {
        this.id = id;
        this.name = name;
        this.introduce = introduce;
        this.logo = logo;
        this.address = address;
        this.territory = territory;
        this.label = label;
    }

    public ProjectManage(long id, String name, String logo, String introduce, String territory, String label, String quality, String code) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.introduce = introduce;
        this.territory = territory;
        this.label = label;
        this.quality = quality;
        this.code = code;
    }

    public ProjectManage(long id, String code, String name, String type, String territory, String nowRound, Integer nowMoney, String status, String contactName, String auditingName, long companyId) {

        this.id = id;
        this.name = name;
        this.territory = territory;
        this.code = code;
        this.type = type;
        this.nowRound = nowRound;
        this.nowMoney = nowMoney;
        this.status = status;
        this.contactName = contactName;
        this.auditingName = auditingName;
        this.companyId = companyId;


    }
}
