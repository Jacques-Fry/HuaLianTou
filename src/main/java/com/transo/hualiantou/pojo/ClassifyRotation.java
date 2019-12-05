package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "classify_rotation")
@ApiModel(value = "轮次模型")
@Getter
@Setter
public class ClassifyRotation {


    @Id
    @Column(name = "ro_unid", columnDefinition = "bigint COMMENT'轮次主键id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "轮次主键id")
    private long id;

    @Column(name = "ro_code", columnDefinition = "varchar(255) COMMENT'轮次编码'")
    @ApiModelProperty(value = "轮次编码")
    private String code;

    @Column(name = "ro_name", columnDefinition = "varchar(255) COMMENT'轮次名称'")
    @ApiModelProperty(value = "轮次名称")
    private String name;

    @Column(name = "ro_create_time", columnDefinition = "timestamp COMMENT'创建时间'")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
