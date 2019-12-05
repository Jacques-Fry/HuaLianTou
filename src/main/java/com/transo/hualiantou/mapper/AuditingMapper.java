package com.transo.hualiantou.mapper;

import com.transo.hualiantou.pojo.Auditing;
import org.mapstruct.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/8/21 13:47
 */
@Mapper
@Component
public interface AuditingMapper {
    @Select("SELECT `code`,id,`status`,fail,auditor,create_time,`type` FROM ( SELECT `ap_code` `code` ,`ap_id` id,`ap_status` `status`, `ap_fail` fail,`ap_auditor` auditor,`ap_create_time` create_time,1 `type`  FROM auditing_personal  UNION SELECT `ac_code` `code` ,`ac_id` id,`ac_status` `status`, `ac_fail` fail,`ac_auditor` auditor,`ac_create_time` create_time,2 `type` FROM auditing_company UNION SELECT `ag_code` `code` ,`ag_id` id,`ag_status` `status`, `ag_fail` fail,`ag_auditor` auditor,`ag_create_time` create_time,3 `type` FROM auditing_government  ) a WHERE `code`=#{code} ORDER BY create_time DESC")
    public List<Auditing> getAuditing(String code);

    @Select("SELECT `code`,id,`status`,fail,auditor,create_time,`type` FROM ( SELECT `ac_code` `code` ,`ac_id` id,`ac_status` `status`, `ac_fail` fail,`ac_auditor` auditor,`ac_create_time` create_time,2 `type` FROM auditing_company UNION SELECT `ag_code` `code` ,`ag_id` id,`ag_status` `status`, `ag_fail` fail,`ag_auditor` auditor,`ag_create_time` create_time,3 `type` FROM auditing_government  ) a WHERE `code`=#{code} and status in ('0','1','2') ORDER BY create_time DESC")
    public List<Auditing> getAuditingStatus(String code);
}
