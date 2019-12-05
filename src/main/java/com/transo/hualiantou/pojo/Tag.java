package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/9/12 11:20
 */
@Entity
@Table(name = "third_party_tag")
@Getter@Setter
public class Tag implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_id", length = 64, columnDefinition = "bigint(64) COMMENT'第三方标记id'")
    @ApiModelProperty(value = "第三方标记id")
    private long id;
    @Column(name = "t_code", length = 64, columnDefinition = "varchar(255) COMMENT'第三方标记code'")
    @ApiModelProperty(value = "第三方标记code")
    private String code;
    @Column(name = "t_name", length = 64, columnDefinition = "varchar(255) COMMENT'第三方标记名称'")
    @ApiModelProperty(value = "第三方标记名称")
    private String name;
    @Column(name = "t_instructions", length = 64, columnDefinition = "text COMMENT'第三方标记来源说明'")
    @ApiModelProperty(value = "第三方标记说明")
    private String instructions;@Column(name = "t_create_time", columnDefinition = "TIMESTAMP comment'创建时间'", nullable = false, updatable = false)
    @ApiModelProperty(value = "创建时间")
    private Date createTime ;

}
