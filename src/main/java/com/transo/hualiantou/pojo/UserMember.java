package com.transo.hualiantou.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户关联会员中间表
 *
 * @author chen
 * @create 2019/10/14 14:15
 */

@Entity
@Table(name = "user_member")
@Getter
@Setter
@ApiModel(value = "用户会员表")
public class UserMember implements Serializable {

    @Id
    @ApiModelProperty(value = "用户id")
    private long userId; //用户id
    @ApiModelProperty(value = "会员编码")
    private long memberCode; //会员编码
    @ApiModelProperty(value = "当前套餐编号")
    private long memberSystemId; //当前套餐编号

    @ApiModelProperty(value = "开通时间")
    private Date createTime;//开通时间
    @ApiModelProperty(value = "结束时间（精确到天）")
    private Date closedTime; //结束时间（精确到天）

    @ApiModelProperty(value ="剩余投递次数" )
    private Integer deliveryNumber;//剩余投递次数
    @ApiModelProperty(value ="项目允许上线中的个数" )
    private Integer projectNumber;
    @ApiModelProperty(value ="资金允许上线中的个数" )
    private Integer moneyNumber;

    @Transient
    @ApiModelProperty(value = "会员信息")
    private Member member;

    @Transient
    @ApiModelProperty(value = "套餐信息")
    private MemberSystem memberSystem;

}
