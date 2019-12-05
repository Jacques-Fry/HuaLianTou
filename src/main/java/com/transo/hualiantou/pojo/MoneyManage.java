package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jack_YD
 * @create 2019/8/14
 */
@Entity
@Table(name = "money_manage")
@ApiModel(value = "资金模型")
@Getter
@Setter
public class MoneyManage implements Serializable {
    @Id
    @Column(name = "mm_id", length = 11, columnDefinition = "bigint COMMENT'资金ID'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "资金ID")
    private long id;

    @Column(name = "mm_code", columnDefinition = "varchar(64) COMMENT'资金编码'")
    @ApiModelProperty(value = "资金编码")
    private String code;

    @Column(name = "mm_institutions", columnDefinition = "varchar(255) COMMENT'机构名称 （公司名称）'")
    @ApiModelProperty(value = "机构名称 （公司名称）")
    private String institutions;

    @NotBlank(message = "资金详情介绍不可为空!")
    @Column(name = "mm_dyf", columnDefinition = "TEXT COMMENT'资金详情介绍'")
    @ApiModelProperty(value = "资金详情介绍")
    private String dyf;

    //@Pattern(regexp = "^[1-9]\\d*$",message = "资金资产不合法 请输入正整数!")
    @Column(name = "mm_money", columnDefinition = "bigint(64) COMMENT'资金资产'")
    @ApiModelProperty(value = "资金资产")
    private long money;

    @NotBlank(message = "投资领域不可为空!")
    @Column(name = "mm_territory", columnDefinition = "varchar(250) COMMENT'投资领域'")
    @ApiModelProperty(value = "投资领域")
    private String territory;

    //@Pattern(regexp = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})",message = "url格式不匹配!")
    @Column(name = "mm_url", columnDefinition = "varchar(255) COMMENT'公司官方地址'")
    @ApiModelProperty(value = "公司官方地址")
    private String url;

    @Column(name = "mm_auditing_name", columnDefinition = "varchar(250) COMMENT'个人认证名'")
    @ApiModelProperty(value = "个人认证名")
    private String auditingName;

    /*@Column(name = "mm_addr_province")
    @ApiModelProperty(value = "省")
    private String addrProvince;

    @Column(name = "mm_addr_city")
    @ApiModelProperty(value = "市")
    private String addrCity;

    @Column(name = "mm_addr_area")
    @ApiModelProperty(value = "区")
    private String addrArea;*/

    @NotBlank(message = "资金地区不可为空!")
    @Column(name = "mm_address", columnDefinition = "varchar(255) COMMENT'资金地区'")
    @ApiModelProperty(value = "资金地区")
    private String address;

    //@NotBlank(message = "详细地址不可为空!")
    @Column(name = "mm_detailed_address", columnDefinition = "varchar(255) COMMENT'详细地址'")
    @ApiModelProperty(value = "详细地址")
    private String detailedAddress;

    @Column(name = "mm_logo", columnDefinition = "varchar(255) COMMENT'公司logo（图片）'")
    @ApiModelProperty(value = "公司logo（图片）")
    private String logo;

    @NotBlank(message = "投资方式不可为空!")
    @Column(name = "mm_way", columnDefinition = "varchar(250) COMMENT'投资方式'")
    @ApiModelProperty(value = "投资方式")
    private String way;

    @NotBlank(message = "偏好投资轮次不可为空!")
    @Column(name = "mm_love_round", columnDefinition = "varchar(250) COMMENT'偏好投资轮次'")
    @ApiModelProperty(value = "偏好投资轮次")
    private String loveRound;

    @Column(name = "mm_team", columnDefinition = "varchar(264) COMMENT'机构团队（团队编码）'")
    @ApiModelProperty(value = "机构团队（团队编码）")
    private String team;

    @Column(name = "mm_investment", columnDefinition = "varchar(255) COMMENT'投资案例（投资编号）'")
    @ApiModelProperty(value = "投资案例（投资编号）")
    private String investments;

    @Column(name = "mm_quality", columnDefinition = "varchar(2) COMMENT'是否优质(Y为优质,N为非优质)'")
    @ApiModelProperty(value = "是否优质(Y为优质,N为非优质)")
    private String quality;

    @Column(name = "mm_user", columnDefinition = "bigint(64) COMMENT'创建用户id'")
    @ApiModelProperty(value = "创建用户id")
    private long userId;

    @NotBlank(message = "联系人名字不能为空!")
    @Column(name = "mm_contact_name", columnDefinition = "varchar(250) COMMENT'联系人名字'")
    @ApiModelProperty(value = "联系人名字")
    private String contactName;

    @Pattern(regexp = "^(\\d{11})$", message = "联系人手机号不合法!")
    @Column(name = "mm_contact_phone", columnDefinition = "varchar(11) COMMENT'联系人手机号'")
    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;

    //@Pattern(regexp = "^[a-z0-9A-Z]+[-|a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$",message = "联系人邮箱格式不匹配!")
    @Column(name = "mm_contact_email", columnDefinition = "varchar(255) COMMENT'联系人邮箱'")
    @ApiModelProperty(value = "联系人邮箱")
    private String contactEmail;

    @Column(name = "mm_status", columnDefinition = "varchar(2) COMMENT'资金状态:0.待审核中 1.审核通过 2.审核失败 3.系统冻结 4. 5. 6. 7. 8.'")
    @ApiModelProperty(value = "资金状态:0.待审核中 1.审核通过 2.审核失败 3.系统冻结 4. 5. 6. 7. 8.")
    private String status;

    @Column(name = "mm_type", columnDefinition = "varchar(2) COMMENT'资金类型:1、个人资金；2、企业资金；3、政府资金'")
    @ApiModelProperty(value = "资金类型:1、个人资金；2、企业资金；3、政府资金")
    private String type;

    @Column(name = "mm_create_time", columnDefinition = "TIMESTAMP comment'资金发布时间'", nullable = false, updatable = false)
    @ApiModelProperty(value = "资金发布时间")
    private Date createTime;

    @Column(name = "mm_company_id", columnDefinition = "bigint(64) COMMENT'该资金所属企业ID，若非企业项目，则为空'")
    @ApiModelProperty(value = "该资金所属企业ID，若非企业项目，则为空")
    private long companyId;




    @Column(name = "mm_fail", columnDefinition = "text Comment '失败原因'")
    @ApiModelProperty(value = "审核失败原因..")
    private String fail;

    @Column(name = "mm_auditing_person", columnDefinition = "bigint (64) Comment '审核人'")
    private Integer auditingPerson;

    @Column(name = "mm_auditing_time", columnDefinition = "DATE Comment '审核时间'")
    private Date auditingTime;


    @Column(name = "mm_quality_date", columnDefinition = "datetime Comment '设置优质资金的确切时间'")
    private Date qualityDate;


    //@JsonBackReference(value = "group")
    /*@OneToMany(cascade = CascadeType.ALL*//*, fetch = FetchType.EAGER*//*, targetEntity = Group.class)
    @JoinColumn(name = "mm_code", referencedColumnName = "mm_team")*/
    @Transient
    private List<Group> group;


    //@JsonBackReference(value = "investment")
    /*@OneToMany(cascade = CascadeType.ALL*//*, fetch = FetchType.EAGER*//*, targetEntity = Investment.class)
    @JoinColumn(name = "it_code", referencedColumnName = "mm_investment")*/
    @Transient
    private List<Investment> investment ;

    @Transient
    private AuditingCompany company;

    @Transient
    private AuditingGovernment government;

    @Column(name = "mm_iscoller", columnDefinition = "varchar(20) Comment '是否被收藏'")
    private String isColler;


    @ApiModelProperty(value = "图表统计")
    @Transient
    private Map<String,Object> statistical;

    public MoneyManage() {
    }

    public MoneyManage(long id, String logo, String institutions, String dyf, String address) {
        this.id = id;
        this.dyf = dyf;
        this.logo = logo;
        this.institutions = institutions;
        this.address = address;
    }

    public MoneyManage(long id, String logo, String institutions, String dyf) {
        this.id = id;
        this.dyf = dyf;
        this.logo = logo;
        this.institutions = institutions;
    }

    public MoneyManage(long id, String logo, String institutions, String dyf,String territory,String quality) {
        this.id = id;
        this.dyf = dyf;
        this.logo = logo;
        this.institutions = institutions;
        this.territory = territory;
        this.quality = quality;
    }
}
