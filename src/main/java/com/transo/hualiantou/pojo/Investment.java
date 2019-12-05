package com.transo.hualiantou.pojo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/8/23 14:32
 */
@Entity
@Table(name = "investment")
@Getter
@Setter
public class Investment implements Serializable {

    @Id
    @Column(name = "it_id", length = 11, columnDefinition = "bigint COMMENT'投资案例编号主键id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "投资案例编号主键id")
    private long id;

    /*@Column(name = "it_code")
    @ApiModelProperty(value = "投资案例编号")
    private String code;*/

    @NotBlank(message = "被投资项目名称不可为空!")
    @Column(name = "it_project_name", columnDefinition = "varchar(255) COMMENT'被投资项目名称'")
    @ApiModelProperty(value = "被投资项目名称")
    private String projectName;

    @Digits(integer = 9, fraction = 2, message = "投资金额数值不规范,0-1000000000之间!")
    @Column(name = "it_money", columnDefinition = "decimal (11,2) COMMENT'投资金额,单位:万'")
    @ApiModelProperty(value = "投资金额,单位:万")
    private double money;

    @Pattern(message = "投资时间日期格式不正确!", regexp = "^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")
    @Column(name = "it_time", columnDefinition = "date COMMENT'投资时间'")
    @ApiModelProperty(value = "投资时间")
    private String time;

    @Column(name = "us_uid", columnDefinition = "BIGINT (64) COMMENT '投资人ID'")
    @ApiModelProperty(value = "投资人ID")
    private long userID;

    @Column(name = "it_code",columnDefinition = "varchar(64) COMMENT'投资案例编码'")
    public String code;

    @Column(name = "it_project_code", columnDefinition = "varchar(255) COMMENT'被投资项目Code'")
    @ApiModelProperty(value = "被投资项目Code")
    private String projectCode;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "it_create_time", columnDefinition = "TIMESTAMP COMMENT'创建时间'")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "it_status", columnDefinition = "varchar(5) COMMENT'状态（1、可见 2.不可见）'")
    @ApiModelProperty(value = "状态（1、可见 2.不可见）")
    private String status ;


}
