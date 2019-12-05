package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/8/13
 */
@Entity
@Table(name = "auditing_personal")
@ApiModel(value = "个人认证模型")
@Getter
@Setter
public class AuditingPersonal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ap_id", length = 11, columnDefinition = "bigint COMMENT'主键id'")
    @ApiModelProperty(value = "主键id")
    private long id;

    @Column(name = "ap_code", columnDefinition = "varchar(155) COMMENT'认证表编码（关联user）'")
    @ApiModelProperty(value = "认证表编码（关联user）")
    private String code;

    @NotBlank(message = "真实姓名不可为空!")
    @Column(name = "ap_real_name", columnDefinition = "varchar(50) COMMENT'真实姓名'")
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    //    @Pattern(regexp = "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/",message = "身份证号码不合法!")
    @NotBlank(message = "身份证不可为空!")
    @Column(name = "ap_card_id", columnDefinition = "varchar(18) COMMENT'身份证'")
    @ApiModelProperty(value = "身份证")
    private String idcard;

    @NotBlank(message = "身份证正面文件未上传!")
    @Column(name = "ap_identity_front", columnDefinition = "TEXT COMMENT'身份证正面'")
    @ApiModelProperty(value = "身份证正面")
    private String identityFront;

    @NotBlank(message = "身份证反面文件未上传!")
    @Column(name = "ap_identity_behind", columnDefinition = "TEXT COMMENT'身份证反面'")
    @ApiModelProperty(value = "身份证反面")
    private String identityBehind;

    @Column(name = "ap_status", columnDefinition = "varchar(5) COMMENT'个人认证状态(0.待认证审核,1.审核通过,2审核失败,3.已注销)'")
    @ApiModelProperty(value = "个人认证状态(0.待认证审核,1.审核通过,2审核失败,3.已注销)")
    private String apStatus;

    @Column(name = "ap_fail", columnDefinition = "TEXT COMMENT'失败原因'")
    @ApiModelProperty(value = "失败原因")
    private String apFail;

    @Column(name = "ap_create_time", columnDefinition = "TIMESTAMP COMMENT'认证时间'", nullable = false, updatable = false)
    @ApiModelProperty(value = "认证时间")
    private Date createTime;

    @Column(name = "ap_auditor", columnDefinition = "varchar(30) COMMENT'审核人'")
    @ApiModelProperty(value = "审核人")
    private String ap_auditor;

    public AuditingPersonal() {
    }

    public AuditingPersonal(String code, String realName, String idcard, String identityFront, String identityBehind, String apStatus, String apFail, Date createTime, String ap_auditor) {
        this.code = code;
        this.realName = realName;
        this.idcard = idcard;
        this.identityFront = identityFront;
        this.identityBehind = identityBehind;
        this.apStatus = apStatus;
        this.apFail = apFail;
        this.createTime = createTime;
        this.ap_auditor = ap_auditor;
    }

    @Override
    public String toString() {
        return "{\"AuditingPersonal\":{"
                + "\"id\":"
                + id
                + ",\"code\":\""
                + code + '\"'
                + ",\"realName\":\""
                + realName + '\"'
                + ",\"idcard\":\""
                + idcard + '\"'
                + ",\"identityFront\":\""
                + identityFront + '\"'
                + ",\"identityBehind\":\""
                + identityBehind + '\"'
                + ",\"apStatus\":\""
                + apStatus + '\"'
                + ",\"apFail\":\""
                + apFail + '\"'
                + ",\"createTime\":\""
                + createTime + '\"'
                + ",\"ap_auditor\":\""
                + ap_auditor + '\"'
                + "}}";

    }
}
