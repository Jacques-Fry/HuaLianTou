package com.transo.hualiantou.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/8/20 14:01
 */
@Getter @Setter
public class UserSubject {

    @ApiModelProperty(value = "用户id")
    private Long id;
    @ApiModelProperty(value = "手机号",required = true)
    private String phone;
    /*@ApiModelProperty(value = "会员(1.是,其他为不是)")
    private String member;*/
    @ApiModelProperty(value = "认证编码")
    private String code;
    /*@ApiModelProperty(value = "权限")
    private String authority;*/
    /*@ApiModelProperty(value = "注册时间")
    private String createTime;*/
    /*@ApiModelProperty(value = "认证角色（0未认证,1个人,2企业(包括个人),3政府(单一主体)")
    private String us_auditing_type;*/

    public UserSubject() {
    }

    public UserSubject(Long id, String phone, String code) {
        this.id = id;
        this.phone = phone;
        this.code = code;
    }

    @Override
    public String toString() {
        return "{\"UserSubject\":{"
                + "\"id\":"
                + id
                + ",\"phone\":\""
                + phone + '\"'
                + ",\"code\":\""
                + code + '\"'
                + "}}";

    }
}
