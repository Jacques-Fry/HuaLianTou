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
import java.util.List;

/**
 * 会员表
 *
 * @author chen
 * @create 2019/10/14 13:53
 */
@Entity
@Table(name = "member")
@Getter
@Setter
@ApiModel(value = "会员表")
public class Member  implements Serializable{

    @Id
    @ApiModelProperty(value ="会员编码" )
    private long code; //会员编码
    @ApiModelProperty(value ="会员名称" )
    private String name; //会员名称
    @ApiModelProperty(value ="会员说明" )
    private String content;//会员说明


    @ApiModelProperty(value ="个人允许项目上线中的个数" )
    private Integer projectNumberPersonal;//个人允许项目上线中的个数
    @ApiModelProperty(value ="企业允许项目上线中的个数" )
    private Integer projectNumberCompany;//企业允许项目上线中的个数
    @ApiModelProperty(value ="政府允许项目上线中的个数" )
    private Integer projectNumberGovernment;//政府允许项目上线中的个数


    @ApiModelProperty(value ="个人允许资金上线中的个数" )
    private Integer moneyNumberPersonal;  //个人允许资金上线中的个数
    @ApiModelProperty(value ="企业允许资金上线中的个数" )
    private Integer moneyNumberCompany;  //企业允许资金上线中的个数
    @ApiModelProperty(value ="政府允许资金上线中的个数" )
    private Integer moneyNumberGovernment;  //政府允许资金上线中的个数


    @ApiModelProperty(value ="原价/每365天" )
    private double price; //原价/每365天
    @ApiModelProperty(value ="会员类型" )
    private String type;//会员类型
    @ApiModelProperty(value ="会员创建时间" )
    private Date createTime; //会员创建时间
    @ApiModelProperty(value ="图标" )
    private String icon;//图标

    @Transient
    @ApiModelProperty(value ="会员创建时间" )
    private List<MemberSystem> memberSystems;

}
