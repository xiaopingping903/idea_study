package com.haier.adp.kpi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 需求开发交付信息
 *
 * Created by 01435337 on 2016/11/10.
 */
@Data
public class StoryDeliveryInfo implements Serializable {
    private static final long serialVersionUID = -3640562968183180929L;
    /**
     * Epic Key
     */
    private String epicKey;

    /**
     * Jira Story Key
     */
    private String storyKey;

    /**
     * ALM需求ID
     */
    private String requireId;

    /**
     * ALM任务ID
     */
    private String taskId;

    /**
     * 需求截止日
     */
    private Date dueDate;

    /**
     * 需求发布日
     */
    private Date releaseDate;

    /**
     * 经办人Id
     */
    private String operatorId;

    /**
     * 经办人姓名
     */
    private String operatorName;

    /**
     * 需求是否及时交付
     */
    private boolean onTime;

    /**
     * 需求及时率状态显示Title
     */
    public String getDeliveryStatusTitle() {
        return (this.onTime ? "准时" : "拖期");
    }
}
