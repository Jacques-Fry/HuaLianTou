package com.transo.hualiantou.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "classify_realm")
@ApiModel(value = "领域模型")
@Getter
@Setter
public class ClassifyRealm implements Serializable {

    @Id
    @Column(name = "cr_unid", columnDefinition = "bigint COMMENT'领域主键id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "领域主键id")
    private long id;

    @Column(name = "cr_code", length = 6, columnDefinition = "varchar(255) COMMENT'领域编码'")
    @ApiModelProperty(value = "领域编码")
    private String code;

    @Column(name = "cr_name", columnDefinition = "varchar(255) COMMENT'领域名称'")
    @ApiModelProperty(value = "领域名称")
    private String name;

    @Column(name = "cr_create_time", columnDefinition = "timestamp COMMENT'创建时间'")
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonBackReference
    @OneToMany(targetEntity = ClassifyTag.class)
    @JoinColumn(name = "ct_realm_code", referencedColumnName = "cr_code")
    private List<ClassifyTag> tags = new ArrayList<>();

}
