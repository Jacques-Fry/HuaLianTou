package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/10/16 17:53
 */
@Entity
@Table(name = "member_system")
@Getter@Setter
public class MemberSystem {

    @Id
    @Column(name = "ms_uid")
    @ApiModelProperty(value = "主键ID")
    private long id;
    @Column(name = "ms_name")
    @ApiModelProperty(value = "套餐名称")
    private String name;
    @Column(name = "ms_content")
    @ApiModelProperty(value = "套餐介绍")
    private String content;
    @Column(name = "ms_duration")
    @ApiModelProperty(value = "套餐时长（单位:年）")
    private Integer duration;
    @Column(name = "ms_discount")
    @ApiModelProperty(value = "套餐折扣")
    private double discount;
    @Column(name = "ms_create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @Column(name = "ms_belong_member")
    @ApiModelProperty(value = "套餐所属会员")
    private long belongMember;
    @Column(name = "ms_delivery_number")
    @ApiModelProperty(value = "赠送的投递次数")
    private Integer deliveryNumber;

    @Transient
    @ApiModelProperty(value = "套餐价格")
    private double price;
}
