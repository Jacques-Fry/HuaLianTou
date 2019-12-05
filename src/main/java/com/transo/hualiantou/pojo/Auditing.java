package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/8/21 13:44
 */
@ApiModel(value = "认证信息")
@Getter@Setter
public class Auditing implements Serializable {
    @ApiModelProperty(value = "认证编码")
    private String code;

    @ApiModelProperty(value = "对应认证ID")
    private Long id;

    @ApiModelProperty(value = "认证状态(0.待认证审核,1.审核通过,2审核失败,3.已注销)")
    private String status;

    @ApiModelProperty(value = "认证失败原因")
    private String fail;

    @ApiModelProperty(value = "审核人")
    private String auditor;

    @ApiModelProperty(value = "认证类型(1.个人认证,2.企业认证,3.政府认证)")
    private String type;

    @ApiModelProperty(value = "认证时间")
    private Date create_time;

    public Auditing() {
    }

    @Override
    public String toString() {
        return "{\"Auditing\":{"
                + "\"code\":\""
                + code + '\"'
                + ",\"id\":"
                + id
                + ",\"status\":\""
                + status + '\"'
                + ",\"fail\":\""
                + fail + '\"'
                + ",\"auditor\":\""
                + auditor + '\"'
                + ",\"type\":\""
                + type + '\"'
                + ",\"create_time\":\""
                + create_time + '\"'
                + "}}";

    }
}
