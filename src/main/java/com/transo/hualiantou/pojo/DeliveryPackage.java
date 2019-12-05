package com.transo.hualiantou.pojo;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="delivery_package")
public class DeliveryPackage {

  @Id
  @ApiModelProperty(value = "ID")
  private long dpId;
  @ApiModelProperty(value = "套餐说明")
  private String dpContent;
  @ApiModelProperty(value = "套餐名称")
  private String dpName;
  @ApiModelProperty(value = "投递个数")
  private Integer dpNumber;
  @ApiModelProperty(value = "价格")
  private double dpPrice;
  @ApiModelProperty(value = "创建时间")
  private java.util.Date dpCreateTime;
  @ApiModelProperty(value = "套餐折扣")
  private double dpDiscount;


  public long getDpId() {
    return dpId;
  }

  public void setDpId(long dpId) {
    this.dpId = dpId;
  }


  public String getDpContent() {
    return dpContent;
  }

  public void setDpContent(String dpContent) {
    this.dpContent = dpContent;
  }


  public String getDpName() {
    return dpName;
  }

  public void setDpName(String dpName) {
    this.dpName = dpName;
  }


  public Integer getDpNumber() {
    return dpNumber;
  }

  public void setDpNumber(Integer dpNumber) {
    this.dpNumber = dpNumber;
  }


  public double getDpPrice() {
    return dpPrice;
  }

  public void setDpPrice(double dpPrice) {
    this.dpPrice = dpPrice;
  }


  public java.util.Date getDpCreateTime() {
    return dpCreateTime;
  }

  public void setDpCreateTime(java.util.Date dpCreateTime) {
    this.dpCreateTime = dpCreateTime;
  }


  public double getDpDiscount() {
    return dpDiscount;
  }

  public void setDpDiscount(double dpDiscount) {
    this.dpDiscount = dpDiscount;
  }

}
