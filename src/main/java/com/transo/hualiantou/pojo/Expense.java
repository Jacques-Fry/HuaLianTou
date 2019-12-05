package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 会员充值记录表
 *
 * @author chen
 * @create 2019/10/14 14:30
 */
@Entity
@Table(name = "expense")
@Getter
@Setter
@ApiModel(value = "订单记录表")
public class Expense  implements Serializable{

    @Id
    @ApiModelProperty(value ="订单编号" )
    private String orderCode;  //订单编号
    @ApiModelProperty(value ="消费商品编码" )
    private long goodsCode; //消费商品编码

    @ApiModelProperty(value ="商品名称" )
    private String goodsName;//商品名称

    @ApiModelProperty(value ="时长（单位：年）" )
    private Integer duration;//时长（单位：年）

    @ApiModelProperty(value ="投递条数" )
    private Integer deliveryNumber;
    @ApiModelProperty(value ="原价" )
    private double originalPrice;
    @ApiModelProperty(value ="实际价格(单位:元)" )
    private double actualPrice;

    @ApiModelProperty(value ="用户id" )
    private Long userId; //用户id
    @ApiModelProperty(value ="订单状态（0.待支付,1.成功；2.失败）" )
    @Column(name = "state")
    private Integer status;//订单状态
    @ApiModelProperty(value ="创建时间" )
    private Date createTime; //创建时间

    @ApiModelProperty(value ="订单类型(1.会员套餐,2.投递套餐)" )
    private Integer type;

    @ApiModelProperty(value ="标识(1.付费记录,2.活动赠送记录(官方赠送))" )
    private Integer tags;


}
