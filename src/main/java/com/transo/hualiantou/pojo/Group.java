package com.transo.hualiantou.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "`group`")
@ApiModel(value = "团队模型")
@Getter
@Setter
public class Group implements Serializable {
    @Id
    @Column(name = "gp_id", length = 11, columnDefinition = "bigint COMMENT'团队主键id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "团队主键id")
    private long id;

    @Column(name = "gp_photo", columnDefinition = "TEXT COMMENT'成员头像'")
    @ApiModelProperty(value = "成员头像")
    private String photo;

    @NotBlank(message = "团队成员名称不可为空!")
    @Column(name = "gp_name", columnDefinition = "varchar(255) COMMENT'成员名称'")
    @ApiModelProperty(value = "成员名称")
    private String name;

    @NotBlank(message = "成员职位不可为空!")
    @Column(name = "gp_position", columnDefinition = "varchar(255) COMMENT'成员职位'")
    @ApiModelProperty(value = "成员职位")
    private String position;

    //@NotBlank(message = "成员介绍不可为空!")
    @Column(name = "gp_introduce", columnDefinition = "TEXT COMMENT'成员介绍'")
    @ApiModelProperty(value = "成员介绍")
    private String introduce;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gp_create_time", columnDefinition = "TIMESTAMP COMMENT'创建时间'")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "mm_code")
    private String mmCode;


    @Column(name = "pm_code")
    private String pmCode;


}
