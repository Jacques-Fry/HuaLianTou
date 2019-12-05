package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "authority")
@ApiModel(value = "权限表")
@Getter
@Setter
public class Authority {

    @Id
    @Column(name = "at_unid", columnDefinition = "bigint COMMENT'资方占比'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "权限主键id")
    private long id;


   /* @Column(name = "at_code", columnDefinition = "varchar(255) COMMENT'权限编码'")
    @ApiModelProperty(value = "权限编码")
    private String code;*/

    @Column(name = "at_name", columnDefinition = "varchar(50) COMMENT'权限角色名称'")
    @ApiModelProperty(value = "权限角色名称")
    private String name;

    @Column(name = "at_create_time", columnDefinition = "timestamp COMMENT'创建时间'")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "at_content", columnDefinition = "varchar(255) COMMENT'访问权限：1. 2. 3. 4.'")
    @ApiModelProperty(value = "访问权限：1. 2. 3. 4. ")
    private String content;
}
