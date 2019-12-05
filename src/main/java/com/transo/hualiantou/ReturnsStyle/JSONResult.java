package com.transo.hualiantou.ReturnsStyle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

import java.io.Serializable;

/**
 * @author PC20
 * @create 2019/8/5
 */
@Getter @Setter
@ApiModel(value = "返回数据模型")
public class JSONResult implements Serializable {
    private static final long serialVersionUID = -4800793124936904868L;
    public static final String SUCCESS="SUCCESS";
    public static final String FAIL="FAIL";
    public static final int SUCCESS_CODE=200;
    public static final int ERROR_CODE=400;

    /**
     * 返回是否成功的状态码,200表示成功,
     * 201或其他值 表示失败
     */
    @ApiModelProperty(value = "响应码")
    private Integer code;
    /**
     * 返回是否成功的状态,success表示成功,
     * error表示失败
     */
    @ApiModelProperty(value = "响应状态")
    private String status;
    /**
     * 成功时候,返回的JSON数据
     */
    @ApiModelProperty(value = "响应数据")
    private Object data;
    /**
     * 是错误时候的错误消息
     */
    @ApiModelProperty(value = "响应提示")
    private String msg;

    /**
     * Token信息
    @ApiModelProperty(value = "响应Token")
    private String token;*/

    public JSONResult() {
        code=SUCCESS_CODE;
        status = SUCCESS;
        msg="ok";
    }

    public JSONResult(Integer code, String status, Object data, String msg) {
        this.code = code;
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public JSONResult(BindingResult br){
        code=ERROR_CODE;
        status = FAIL;
        data=null;
        this.msg=br.getFieldError().getDefaultMessage();
    }

    public JSONResult(String msg,Integer code){
        this.code=code;
        status = FAIL;
        data=null;
        this.msg=msg;
    }

    public JSONResult(Object data){
        code=SUCCESS_CODE;
        status = SUCCESS;
        this.data=data;
        msg="ok";
    }


    @Override
    public String toString() {
        return "{\"JSONResult\":{"
                + "\"code\":"
                + code
                + ",\"status\":\""
                + status + '\"'
                + ",\"data\":"
                + data
                + ",\"msg\":\""
                + msg + '\"'
                + "}}";

    }
}
