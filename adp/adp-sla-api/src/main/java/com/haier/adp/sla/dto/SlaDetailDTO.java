package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Data
public class SlaDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; //id
    private int listId; //与list表关联的ID，一个list_id可以关联多个detail
    private String jiraEpicId; //jira epic id
    private String jiraStoryId; //jira story id
    private String almRequestId; //alm需求号
    private String almTaskId; //alk任务号
    private boolean ifOnTime; //是否及时
    private String requestPlanDate; //需求到期日
    private String requestActualDate; //需求上线日
    private String requestCloseDate;//需求关闭日期
    private int applyPD; //提报人天
    private int managerPrice;//项目经理人天
    private int delayDays; //延迟天数
    private int spentDays;//      ！！
    private String assignPerson; //经办人 ??assigneeId
    private int cutPoints; //扣分//
    private int cutPayment; //扣款//
    private boolean ifPaid; //是否已付款//
    private String type; //SLA类型，分为资源池与系统运维类//
    private String taskDesc;//
    private Date queryStartDate; //查询开始日期
    private Date queryEndDate; //查询结束日期
    private String projectName;
    private String almAppId;
    private boolean isReleaseTask;//是否是发版任务
    private boolean ifSatisfied;//
}
