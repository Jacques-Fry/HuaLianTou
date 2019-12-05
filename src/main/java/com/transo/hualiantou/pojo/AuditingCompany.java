package com.transo.hualiantou.pojo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/8/13
 */
@Entity
@Table(name = "auditing_company")
@ApiModel(value = "企业认证模型")
@Getter
@Setter
public class AuditingCompany implements Serializable {
    //private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ac_id", length = 11, columnDefinition = "bigint(5) COMMENT'企业认证id'")
    @ApiModelProperty(value = "企业认证id")
    private long id;


    @Column(name = "ac_code", columnDefinition = "varchar(50) COMMENT'企业认证表编码'")
    @ApiModelProperty(value = "企业认证表编码")
    private String code;


    @NotBlank(message = "公司名称不可为空!")
    @Column(name = "ac_company_name", columnDefinition = "varchar(255) COMMENT'公司名称'")
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    //@Pattern(regexp = "^[0-9]*$",message = "公司营业执照号不合法,必须为纯数字!")
    @Column(name = "ac_license_number", columnDefinition = "varchar(255) COMMENT'公司营业执照号'")
    @ApiModelProperty(value = "公司营业执照号")
    private String licenseNumber;

    @NotBlank(message = "公司地区不可为空!")
    @Column(name = "ac_address", columnDefinition = "varchar(255) COMMENT'公司地区'")
    @ApiModelProperty(value = "公司地区")
    private String address;

    //@NotBlank(message = "公司详细地址不可为空!")
    @Column(name = "ac_detailedAddress", columnDefinition = "TEXT COMMENT'公司详细地址'")
    @ApiModelProperty(value = "公司详细地址")
    private String detailedAddress;

    @NotBlank(message = "授权人名称不可为空!")
    @Column(name = "ac_certigier", columnDefinition = "varchar(255) COMMENT'授权人名称'")
    @ApiModelProperty(value = "授权人名称")
    private String certigier;

    @NotBlank(message = "企业营业执照不可为空!")
    @Column(name = "ac_license_file", columnDefinition = "TEXT  COMMENT'企业营业执照'")
    @ApiModelProperty(value = "企业营业执照")
    private String licenseFile;

    @NotBlank(message = "企业授权书不可为空!")
    @Column(name = "ac_authorization", columnDefinition = "TEXT COMMENT'企业授权书(提供模板)'")
    @ApiModelProperty(value = "企业授权书(提供模板)")
    private String authorization;

    @Column(name = "ac_honor_certificate", columnDefinition = "TEXT COMMENT'企业荣誉证书(选填)'")
    @ApiModelProperty(value = "企业荣誉证书(选填)")
    private String honorCertificate;

    @Column(name = "ac_status", length = 1, columnDefinition = "varchar(2) COMMENT'企业认证状态(0.待认证审核,1.审核通过,2审核失败,3.已注销)'")
    @ApiModelProperty(value = "企业认证状态(0.待认证审核,1.审核通过,2审核失败,3.已注销)")
    private String acStatus = "0";

    @Column(name = "ac_fail", columnDefinition = "TEXT COMMENT'审核失败原因'")
    @ApiModelProperty(value = "审核失败原因")
    private String acFail;

    @Column(name = "ac_auditor", columnDefinition = "varchar(255) COMMENT'审核人'")
    @ApiModelProperty(value = "审核人")
    private String acAuditor;

    @Column(name = "ac_create_time", columnDefinition = "TIMESTAMP  COMMENT'认证时间'", nullable = false, updatable = false)
    @ApiModelProperty(value = "认证时间")
    private Date createTime;

    /*@ManyToOne(cascade=CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(name="ac_code",referencedColumnName="us_code")
    private User user;*/

    public AuditingCompany() {
    }

    @Override
    public String toString() {
        return "{\"AuditingCompany\":{"
                + "\"id\":"
                + id
                + ",\"code\":\""
                + code + '\"'
                + ",\"companyName\":\""
                + companyName + '\"'
                + ",\"licenseNumber\":\""
                + licenseNumber + '\"'
                + ",\"address\":"
                + address
                + ",\"detailedAddress\":\""
                + detailedAddress + '\"'
                + ",\"certigier\":\""
                + certigier + '\"'
                + ",\"licenseFile\":\""
                + licenseFile + '\"'
                + ",\"authorization\":\""
                + authorization + '\"'
                + ",\"honorCertificate\":\""
                + honorCertificate + '\"'
                + ",\"acStatus\":\""
                + acStatus + '\"'
                + ",\"acFail\":\""
                + acFail + '\"'
                + ",\"acAuditor\":\""
                + acAuditor + '\"'
                + ",\"createTime\":\""
                + createTime + '\"'
                + "}}";

    }
}
