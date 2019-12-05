package com.transo.hualiantou.ReturnsStyle;

import com.transo.hualiantou.pojo.Remind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Jack_YD
 * @create 2019/9/6 10:04
 */
@ApiModel
public class RemindResult implements Serializable {


    @ApiModelProperty(value = "统计")
    private Statistics statistics;
    @ApiModelProperty(value = "消息")
    private RemindResultStyle<Remind> messageReminds;

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public RemindResultStyle<Remind> getMessageReminds() {
        return messageReminds;
    }

    public void setMessageReminds(RemindResultStyle<Remind> messageReminds) {
        this.messageReminds = messageReminds;
    }

}