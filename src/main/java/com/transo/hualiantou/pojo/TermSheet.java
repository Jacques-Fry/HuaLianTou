package com.transo.hualiantou.pojo;

import com.transo.hualiantou.repository.AuditingCompanyRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 投资协议
 */

@Entity
@Table(name = "term_sheet")
@ApiModel(value = "投资协议")
@Getter
@Setter
public class TermSheet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ts_id", length = 11, columnDefinition = "bigint  COMMENT'投资协议id'")
    @ApiModelProperty(value = "投资协议id")
    private long id;

    @Column(name = "ts_code", columnDefinition = "varchar (255)  COMMENT'协议编码'")
    @ApiModelProperty(value = "协议编码")
    private String code;

    @NotBlank(message = "项目编码不能为空")
    @Column(name = "pm_code", columnDefinition = "varchar (255)  COMMENT'项目编码'")
    @ApiModelProperty(value = "项目编码")
    private String pmCode;

    @Column(name = "pm_name", columnDefinition = "varchar (255)  COMMENT'项目名称'")
    @ApiModelProperty(value = "项目名称")
    private String pmName;

    @Column(name = "pmUserId", columnDefinition = "bigint COMMENT'发起人ID'")
    @ApiModelProperty(value = "发起人ID")
    private long pmUserId;

    @Column(name = "mm_userId", columnDefinition = "bigint COMMENT'资金方ID(自动获取,可无视)'")
    @ApiModelProperty(value = "资金方ID(自动获取,可无视)")
    private long mmUserId;

    @Column(name = "mm_name", columnDefinition = "varchar (255)  COMMENT'资金名称'")
    @ApiModelProperty(value = "资金名称(投资机构,自动获取)")
    private String mmName;

    @Column(name = "pm_Round", columnDefinition = "varchar (255) COMMENT'融资轮次'")
    @ApiModelProperty(value = "融资轮次")
    private String Round;

    @NotBlank(message = "资金编码不可为空")
    @Column(name = "mm_code", columnDefinition = "varchar (255) COMMENT'资金编码'")
    @ApiModelProperty(value = "资金编码")
    private String mmCode;

    //@Pattern(message = "融资金额格式错误", regexp = "/(^[1-9]([0-9]+)?(\\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\\.[0-9](0-9)?$)/")
    //@Digits(integer = 20, fraction = 0, message = "投资金额数值不合法 必须为正整数!")
    //@Pattern(regexp = "^[1-9]\\d*$",message = "当前轮次金额不合法 请输入正整数!")
    @Column(name = "pm_now_money")
    @ApiModelProperty(value = "投资金额(单位：万)")
    private double money;

    @Pattern(regexp = "^[1-9][0-9]{0,1}|(0|[1-9][0-9]?)(\\.[0-9]{0,2}[1-9])|100?$",message = "投资占比 请输入0.001-100的数字,小数点后最多三位!")
    @Column(name = "pm_now_proportion")
    @ApiModelProperty(value = "资方占比")
    private String proportion;


    @Column(name = "ts_transaction_certificate ", columnDefinition = "text COMMENT'交易流水证明'")
    @ApiModelProperty(value = "交易流水证明")
    private String transactionCertificate;


    @Column(name = "ts_contract_file", columnDefinition = "text COMMENT'合同文件'")
    @ApiModelProperty(value = "合同文件")
    private String contractFile;

    @Column(name = "ts_status", columnDefinition = "varchar(2) COMMENT'状态:0.待定，1成功 ，2失败'")
    @ApiModelProperty(value = "状态:0.待定，1成功 ，2失败")
    private String status;

    @Column(name = "ts_message", columnDefinition = "text COMMENT'拒绝说明'")
    @ApiModelProperty(value = "拒绝说明")
    private String message;


    @Column(name = "ts_create_time", columnDefinition = "TIMESTAMP COMMENT'发布时间'", nullable = false, updatable = false)
    @ApiModelProperty(value = "发布时间")
    private String createTime;

    @Column(name="rd_new_content",columnDefinition = "varchar(255) COMMENT'操作数据主体(新)'")
    private String idNewContent;


    @Transient
    private ProjectManage projectManage;

    public TermSheet() {
    }

    public TermSheet(long mmUserId, String mmName,String pmCode, String pmName,long pmUserId) {
        this.pmCode = pmCode;
        this.pmName = pmName;
        this.mmName = mmName;
        this.mmUserId=mmUserId;
        this.pmUserId=pmUserId;
    }
}
