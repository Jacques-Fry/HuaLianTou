package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/10/16 16:12
 */
@Entity
@Table(name = "due_diligence")
@Getter@Setter
public class DueDiligence {

    @Id
    @ApiModelProperty(value = "表ID")
    private long id;

    @ApiModelProperty(value = "上传用户")
    @Column(name = "user_id", columnDefinition = "int Comment '上传用户'")
    private long userId;

    @Column(name = "project_name", columnDefinition = "varchar Comment '项目名称'")
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @Column(name = "project_code", columnDefinition = "varchar Comment '项目编码'")
    @ApiModelProperty(value = "项目编码")
    private String projectCode;

    @Column(name = "invest_status", columnDefinition = "varchar Comment '尽调状态（0.待审核;1.已打分）'")
    @ApiModelProperty(value = "尽调状态（1.待审核;2.已打分）")
    private String investStatus;

    @Column(name = "invest_paper", columnDefinition = "text Comment '尽调上传表'")
    @ApiModelProperty(value = "尽调上传表")
    private String investPaper;

    @Column(name = "invest_repaper", columnDefinition = "text Comment '尽调回馈表'")
    @ApiModelProperty(value = "尽调回馈表")
    private String investRepaper;

    @Column(name = "invest_count", columnDefinition = "int Comment '尽调分数'")
    @ApiModelProperty(value = "尽调分数")
    private Integer investCount;

    @Column(name = "invest_date", columnDefinition = "date Comment '尽调上传时间'")
    @ApiModelProperty(value = "尽调上传时间")
    private Date investDate;
}
