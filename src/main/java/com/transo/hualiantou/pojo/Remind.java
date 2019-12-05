package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/9/5 11:43
 */
@ApiModel(value = "消息提醒")
@Entity
@Table(name = "message_remind")
@Getter@Setter
public class Remind implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "数据唯一主键")
    private long id;

    @ApiModelProperty(value = "通知类型(0.系统通知,1.收到投递,2.收到协议")
    private String type;

    @ApiModelProperty(value = "通知标题")
    private String title;

    @ApiModelProperty(value = "通知说明")
    private String content;//通知说明

    @ApiModelProperty(value = "通知对应的数据ID,可无视")
    private long dataId;//通知对应的数据ID

    @ApiModelProperty(value = "通知对应的数据名称,可无视")
    private String dataName;//通知对应的数据名称

    @ApiModelProperty(value = "状态:是否已读(0.未读,1.已读)")
    private String status;//状态:是否已读(0.未读,1.已读)

    @ApiModelProperty(value = "接收用户,可无视")
    private long userId;//接收用户

    @ApiModelProperty(value = "时间")
    private Date createTime;//时间

    @ApiModelProperty(value = "（系统通知类型）1.项目,2.资金,3认证")
    private String systemType;

//    @ApiModelProperty(value = "项目名称")
//    private String pmName;
//
//    @ApiModelProperty(value = "资金名称")
//    private String mmName;

    /*@ApiModelProperty(value = "投递数据")
    @Transient
    private Delivery delivery;//投递

    @ApiModelProperty(value = "投资协议数据")
    @Transient
    private TermSheet termSheet;//投资协议数据*/




    public Remind() {
    }

    public Remind(String type, String title, String content, long dataId, long userId) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.dataId = dataId;
        this.userId = userId;
    }

    public Remind(long id,long dataId,String systemType){
        this.id = id;
        this.dataId = dataId;
        this.systemType = systemType;
    }
}
