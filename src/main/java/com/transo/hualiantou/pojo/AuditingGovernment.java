package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/8/13
 */
@Entity
@Table(name = "auditing_government")
@ApiModel(value = "政府认证模型")
@Getter
@Setter
public class AuditingGovernment implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ag_id", length = 11, columnDefinition = "bigint(5) COMMENT'政府认证主键id'")
    @ApiModelProperty(value = "政府认证主键id")
    private long id;

    @Column(name = "ag_code", columnDefinition = "varchar(255) COMMENT'政府认证表编码'")
    @ApiModelProperty(value = "政府认证表编码")
    private String code;

    @NotBlank(message = "单位名称不可为空!")
    @Column(name = "ag_units_name", columnDefinition = "varchar(255) COMMENT'单位名称'")
    @ApiModelProperty(value = "单位名称")
    private String unitsName;

    @NotBlank(message = "单位简介不可为空!")
    @Column(name = "ag_units_introduction", columnDefinition = "TEXT COMMENT'单位简介'")
    @ApiModelProperty(value = "单位简介")
    private String unitsIntroduction;

    @NotBlank(message = "公司单位地区不可为空!")
    @Column(name = "ag_units_address", columnDefinition = "TEXT COMMENT'单位地区'")
    @ApiModelProperty(value = "单位地区")
    private String address;

    //@NotBlank(message = "单位详细地址不可为空!")
    @Column(name = "ag_detailed_address", columnDefinition = "TEXT COMMENT'单位详细地址'")
    @ApiModelProperty(value = "单位详细地址")
    private String detailedAddress;

    @NotBlank(message = "单位法人证书不可为空!")
    @Column(name = "ag_units_certificate", columnDefinition = "TEXT COMMENT'单位法人证书'")
    @ApiModelProperty(value = "单位法人证书")
    private String unitsCertificate;

    @NotBlank(message = "单位授权书不可为空!")
    @Column(name = "ag_units_authorization", columnDefinition = "TEXT COMMENT'单位授权书(提供模板)'")
    @ApiModelProperty(value = "单位授权书(提供模板)")
    private String unitsAuthorization;

    @Column(name = "ag_status", length = 1, columnDefinition = "varchar(5) COMMENT'政府认证状态(0.待认证审核,1.审核通过,2审核失败,3.已注销)'")
    @ApiModelProperty(value = "政府认证状态(0.待认证审核,1.审核通过,2审核失败,3.已注销)")
    private String agStatus;

    @Column(name = "ag_fail", columnDefinition = "TEXT COMMENT'审核失败原因'")
    @ApiModelProperty(value = "审核失败原因")
    private String agFail;

    @Column(name = "ag_auditor", columnDefinition = "varchar(255) COMMENT'审核人'")
    @ApiModelProperty(value = "审核人")
    private String agAuditor;

    @Column(name = "ag_create_time", columnDefinition = "TIMESTAMP COMMENT'认证时间'", nullable = false, updatable = false)
    @ApiModelProperty(value = "认证时间")
    private String createTime;

    public AuditingGovernment() {
    }

    @Override
    public String toString() {
        return "{\"AuditingGovernment\":{"
                + "\"id\":"
                + id
                + ",\"code\":\""
                + code + '\"'
                + ",\"unitsName\":\""
                + unitsName + '\"'
                + ",\"unitsIntroduction\":\""
                + unitsIntroduction + '\"'
                + ",\"address\":"
                + address
                + ",\"detailedAddress\":\""
                + detailedAddress + '\"'
                + ",\"unitsCertificate\":\""
                + unitsCertificate + '\"'
                + ",\"unitsAuthorization\":\""
                + unitsAuthorization + '\"'
                + ",\"agStatus\":\""
                + agStatus + '\"'
                + ",\"agFail\":\""
                + agFail + '\"'
                + ",\"agAuditor\":\""
                + agAuditor + '\"'
                + ",\"createTime\":\""
                + createTime + '\"'
                + "}}";

    }
}
