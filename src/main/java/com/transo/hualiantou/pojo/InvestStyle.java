package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invest_style")
@ApiModel(value = "投资融资方式模型")
@Getter
@Setter
public class InvestStyle {

    @Id
    @Column(name = "is_unid", length = 11, columnDefinition = "bigint COMMENT'方式主键id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "方式主键id")
    private long id;

    @Column(name = "is_name", columnDefinition = "varchar(255) COMMENT'方式名称'")
    @ApiModelProperty(value = "方式名称")
    private String name;

    @Column(name = "is_create_time", columnDefinition = "TIMESTAMP COMMENT'创建时间'")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
