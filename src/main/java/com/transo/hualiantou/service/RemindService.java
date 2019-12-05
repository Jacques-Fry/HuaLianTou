package com.transo.hualiantou.service;

import com.transo.hualiantou.ReturnsStyle.RemindResultStyle;
import com.transo.hualiantou.pojo.Remind;
import com.transo.hualiantou.repository.RemindRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Jack_YD
 * @create 2019/9/5 14:05
 */
@Service
public class RemindService {

    @Autowired
    private RemindRepository remindRepository;


    //生成消息记录
    public void addRemind(Remind remind) {
        //数据初始化
        remind.setStatus("0");
        remind.setCreateTime(new Date());
        remindRepository.save(remind);
    }


    //分页查询消息
    public RemindResultStyle<Remind> pageQuery(long userId, String status, String type, int pageNo, int pageSize) {

        if (StringUtils.isBlank(status)) {
            status = "%";
        }

        if (StringUtils.isBlank(type)) {
            type = "%";
        }

        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
        Page<Remind> reminds = remindRepository.findByUserIdAndStatusLikeAndTypeLikeOrderByCreateTimeDesc(userId, status, type, pageable);

        RemindResultStyle<Remind> resultStyle = new RemindResultStyle<>(reminds.getTotalElements(), reminds.getContent());


        // 未读条数统计
        resultStyle.setSystemRemindCount(remindRepository.countByTypeAndStatusAndUserId("0", "0", userId));
        resultStyle.setDeliveryRemindCount(remindRepository.countByTypeAndStatusAndUserId("1", "0", userId));
        resultStyle.setTermSheetRemindCount(remindRepository.countByTypeAndStatusAndUserId("2", "0", userId));


        //查询后设置为已看
        remindRepository.updateStatusByUserId("1", userId, type);


        return resultStyle;
    }

    //设置消息为已读
    public void isLook(long id) {
        remindRepository.updateStatus("1", id);
    }

    //删除
    public void deleteRemind(long id) {
        if (!remindRepository.existsById(id)) {
            throw new RuntimeException("该数据已不存在!");
        }
        remindRepository.delete(id);
    }

}
