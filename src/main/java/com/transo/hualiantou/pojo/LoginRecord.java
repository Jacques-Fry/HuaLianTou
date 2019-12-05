package com.transo.hualiantou.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/9/2 17:18
 */
@Entity
@Table(name = "login_record")
@Getter
@Setter
public class LoginRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lr_id",columnDefinition = "bigint (64) COMMENT'用户主键'")
    private long lr_id;
    @Column(name="lr_operation",columnDefinition = "varchar (255) COMMENT'操作'")
    private String lr_operation;
    @Column(name="lr_ip",columnDefinition = "varchar(255) COMMENT'操作主机'")
    private String lr_ip;
    @Column(name="lr_phone_id",columnDefinition = "varchar(255) COMMENT'登录账号id关联userback的主键'")
    private String lr_phone_id;
    @Column(name="lr_createtime",columnDefinition = "timestamp COMMENT'操作时间'", nullable = false, updatable = false)
    private Date lr_createtime;

}
