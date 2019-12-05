package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "collect")
@ApiModel(value = "收藏模型")
@Getter
@Setter
public class Collect {

    @Id
    @Column(name = "co_unid", length = 11, columnDefinition = "bigint COMMENT'收藏主键id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "收藏主键id")
    private long id;


    @Column(name = "co_user_id", columnDefinition = "bigint COMMENT'收藏人id'")
    @ApiModelProperty(value = "收藏人id")
    private long userId;


    @Column(name = "co_code", columnDefinition = "varchar(255) COMMENT'收藏项目或资金编码'")
    @ApiModelProperty(value = "收藏项目或资金编码")
    private String code;


    @Column(name = "co_name", columnDefinition = "varchar(255) ")
    private String name;

    @Column(name = "au_type", columnDefinition = "varchar(5) COMMENT'认证类型(无视)'")
    private String auType;

    @Column(name = "co_type", columnDefinition = "varchar(5) COMMENT'被收藏的类型(1.项目 , 2.资金)'")
    @ApiModelProperty(value = "被收藏的类型(1.项目 , 2.资金)")
    private String type;


    @Column(name = "co_create_time", columnDefinition = "TIMESTAMP COMMENT'创建时间'", nullable = false, updatable = false)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Transient
    private ProjectManage projectManage;

    @Transient
    private MoneyManage moneyManage;


}
