package com.transo.hualiantou.pojo;

import com.transo.hualiantou.repository.AuditingCompanyRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 操作记录表
 */
@Entity
@Table(name = "record")
@ApiModel(value = "操作记录表")
@Getter
@Setter
public class Record {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rd_id", length = 11, columnDefinition = "bigint COMMENT'记录表id'")
    @ApiModelProperty(value = "记录表id")
    private long id;

    @Column(name = "code", columnDefinition = "varchar(255) COMMENT'操作数据编码'")
    @ApiModelProperty(value = "操作数据编码")
    private String code;

    @Column(name = "rd_old_content", columnDefinition = "TEXT COMMENT'操作旧的数据主体'")
    @ApiModelProperty(value = "操作数据主体")
    private String oldContent;
    @Column(name = "rd_new_content", columnDefinition = "TEXT COMMENT'操作新的数据主体'")
    @ApiModelProperty(value = "操作数据主体")
    private String newContent;


    @Column(name = "rd_content_type", columnDefinition = "varchar(5) COMMENT'操作数据主体的类型(0,项目1.资金)'")
    @ApiModelProperty(value = "操作数据主体的类型")
    private String contentType;

    @Column(name = "rd_operation_type", columnDefinition = "varchar(5) COMMENT'操作数据操作的类型 （0.修改 1.删除）'")
    @ApiModelProperty(value = "操作数据操作的类型 （修改 删除）")
    private String operationType;

    @Column(name = "rd_user_id", columnDefinition = "bigint COMMENT'操作数据的用户'")
    @ApiModelProperty(value = "操作数据的用户")
    private long userId;

    @Column(name = "rd_creater_time", columnDefinition = "TIMESTAMP COMMENT'操作记录时间'")
    @ApiModelProperty(value = "操作记录时间")
    private Date createrTime;


}
