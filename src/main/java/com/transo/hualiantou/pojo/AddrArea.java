package com.transo.hualiantou.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 区
 */
@Setter
@Getter
@Entity
@Table(name = "addr_area")
@ApiModel("区")
public class AddrArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    @Column(name = "id")
    private int id;
    @ApiModelProperty("区编码")@Column(name = "code")
    private String code;
    @ApiModelProperty()@Column(name = "name")
    private String name;
    @ApiModelProperty()@Column(name = "citycode")
    private String citycode;

   // private AddrCity city;

}
