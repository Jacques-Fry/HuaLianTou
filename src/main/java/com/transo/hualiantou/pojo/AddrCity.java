package com.transo.hualiantou.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 市
 */
@Getter
@Setter
@Entity
@Table(name = "addr_city")
public class AddrCity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "provincecode")
    private String provincecode;

   // private List<AddrArea> areas;
  //  private AddrProvince province;
}
