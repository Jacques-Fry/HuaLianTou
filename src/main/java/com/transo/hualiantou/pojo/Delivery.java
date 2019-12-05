package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "delivery")
@ApiModel(value = "投递项目模型")
@Getter
@Setter
public class Delivery  implements Serializable {


    @Id
    @Column(name = "dy_unid", length = 11, columnDefinition = "bigint COMMENT'投递主键id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "投递主键id")
    private long id;

    @Column(name = "dy_send_id", columnDefinition = "bigint COMMENT'投递人(项目方)ID'")
    @ApiModelProperty(value = "投递人(项目方)ID")
    private long sendUserId;

    @Column(name = "dy_receive_id", columnDefinition = "bigint COMMENT'接收者(资金方)ID'")
    @ApiModelProperty(value = "接收者(资金方)ID")
    private long receiveUserId;

    @Column(name = "dy_project_code", columnDefinition = "varchar(50) COMMENT'项目编码'")
    @ApiModelProperty(value = "项目编码")
    private String projectCode;

    @Column(name = "dy_money_code", columnDefinition = "varchar(50) COMMENT'资金编码'")
    @ApiModelProperty(value = "资金编码")
    private String moneyCode;

    @Column(name = "dy_create_time", columnDefinition = "TIMESTAMP COMMENT'投递时间'", nullable = false)
    @ApiModelProperty(value = "投递时间")
    private String  createTime;

    @Column(name = "dy_status", columnDefinition = "varchar(5) COMMENT'资方占比'")
    @ApiModelProperty(value = "投递状态(0.待查看,1.已查看,2.对方已忽略)")
    private String status;

    @Transient
    @ApiModelProperty(value = "是否被收藏")
    private String isColler;

    @Transient
    @ApiModelProperty(value = "项目名称")
    private String pmName;

    @Transient
    @ApiModelProperty(value = "资金名称")
    private String mmName;

    @Transient
    @ApiModelProperty(value = "项目对象")
    private ProjectManage projectManage;

    @Transient
    @ApiModelProperty(value = "资金对象")
    private MoneyManage moneyManage;


    @Column(name="dy_show",columnDefinition = "varchar (5) comment'是否展示(1,展示2,隐藏)'")
    private String show;

}
