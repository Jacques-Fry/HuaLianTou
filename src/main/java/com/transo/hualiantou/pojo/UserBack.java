package com.transo.hualiantou.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Jack_YD
 * @create 2019/9/2 17:18
 */
@Entity
@Table(name = "user_back")
@Getter
@Setter
public class UserBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ub_uid",columnDefinition = "bigint (64) COMMENT'用户主键'")
    private long ub_uid;
    @Column(name="ub_phone",columnDefinition = "varchar (11) COMMENT'手机号码'")
    private String ub_phone;
    @Column(name="ub_password",columnDefinition = "varchar(255) COMMENT'密码'")
    private String ub_password;
    @Column(name="ub_create_time",columnDefinition = "timestamp COMMENT'注册时间'", nullable = false, updatable = false)
    private Date ub_create_time;
    @Column(name="ub_authority",columnDefinition = "varchar(64) COMMENT'权限（例112,113,114）'")
    private String ub_authority;

}
