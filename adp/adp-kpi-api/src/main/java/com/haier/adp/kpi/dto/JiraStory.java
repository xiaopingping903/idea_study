package com.haier.adp.kpi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 01435337 on 2016/11/11.
 */
@Data
public class JiraStory implements Serializable {
    private static final long serialVersionUID = -3576444215535794900L;
    /**
     * Jira Epic Key
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
     * Jira Story title
     */
    private String storyTitle;

    /**
     * 需求发版日期
     */
    private Date releaseDate;
}
