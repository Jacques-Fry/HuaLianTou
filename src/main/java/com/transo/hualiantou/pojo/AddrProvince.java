package com.transo.hualiantou.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 省
 */
@Setter
@Getter
@Entity
@Table(name = "addr_province")
public class AddrProvince {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;


   // private List<AddrCity> citys;

}
