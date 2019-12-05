package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Parent;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "project_rate")
@ApiModel(value = "项目进度模型")
@Getter
@Setter
public class ProjectRate implements Serializable {

    @Id
    @Column(name = "pr_unid", length = 11, columnDefinition = "bigint COMMENT'主键id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键id")
    private long id;

    @Pattern(message = "日期格式不正确", regexp = "^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")
    @Column(name = "pr_progress_date", columnDefinition = "DATE comment'进度对应时间' ")
    @ApiModelProperty(value = "进度对应时间")
    private String progressDate;

    @NotBlank(message = "地址不能为空")
    @Column(name = "pr_address", columnDefinition = "varchar(255) COMMENT'地址'")
    @ApiModelProperty(value = "地址")
    private String address;


    @Column(name = "pr_content", columnDefinition = "TEXT COMMENT'内容'")
    @ApiModelProperty(value = "内容")
    private String content;

    @NotBlank(message = "项目编码不能为空")
    @Column(name = "pr_project_code", columnDefinition = "varchar(255) COMMENT'项目code'")
    @ApiModelProperty(value = "项目code")
    private String projectCode;


    @Column(name = "pr_project_uesr_id", columnDefinition = "bigint COMMENT'项目方ID'")
    @ApiModelProperty(value = "项目方ID")
    private long projectUserId;

    @Column(name = "pr_money_uesr_id", columnDefinition = "bigint COMMENT'投资方id'")
    @ApiModelProperty(value = "投资方id")
    private long moneyUserId;

    @Column(name = "pr_video", columnDefinition = "varchar(255) COMMENT'视频文件'")
    @ApiModelProperty(value = "视频文件")
    private String video;

    @Column(name = "pr_audio", columnDefinition = "varchar(255) COMMENT'音频文件'")
    @ApiModelProperty(value = "音频文件")
    private String audio;

    @Column(name = "pr_picture", columnDefinition = "varchar(255) COMMENT'图片文件'")
    @ApiModelProperty(value = "图片文件")
    private String picture;

    @Column(name = "pr_file", columnDefinition = "varchar(255) COMMENT'普通文件'")
    @ApiModelProperty(value = "普通文件")
    private String file;

    @NotBlank(message = "类型不能为空")
    @Column(name = "pr_type", columnDefinition = "varchar(2) COMMENT'类型(1.项目方2.投资方)'")
    @ApiModelProperty(value = "类型(1.项目方2.投资方)")
    private String type;

    @Column(name = "pr_status", columnDefinition = "varchar(2) COMMENT'状态（1、可见 2.不可见）'")
    @ApiModelProperty(value = "状态（1、可见 2.不可见）")
    private String prStatus = "1";


    @Column(name = "pr_create_time", columnDefinition = "TIMESTAMP comment'创建时间'", nullable = false, updatable = false)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
