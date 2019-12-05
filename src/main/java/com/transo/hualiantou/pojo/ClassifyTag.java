package com.transo.hualiantou.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "classify_tag")
@ApiModel(value = "标签模型")
@Getter
@Setter
public class ClassifyTag implements Serializable {

    @Id
    @Column(name = "ct_unid", length = 11, columnDefinition = "bigint COMMENT'标签主键id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "标签主键id")
    private long id;

    @Column(name = "ct_code", length = 6, columnDefinition = "varchar(255) COMMENT'标签编码'")
    @ApiModelProperty(value = "标签编码")
    private String code;

    @Column(name = "ct_name", columnDefinition = "varchar(255) COMMENT'标签名称'")
    @ApiModelProperty(value = "标签名称")
    private String name;

    /*@Column(name = "ct_realm_code")
    @ApiModelProperty(value = "领域编码")
    private String realmCode;*/

    @Column(name = "ct_create_time", columnDefinition = "TIMESTAMP COMMENT'标签创建时间'")
    @ApiModelProperty(value = "标签创建时间")
    private Date createTime;


    @JsonBackReference
    @ManyToOne(targetEntity = ClassifyRealm.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "ct_realm_code", referencedColumnName = "cr_code")
    private ClassifyRealm realm;//用它的主键，对应联系人表中的外键


}
