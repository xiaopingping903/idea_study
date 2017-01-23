package com.haier.adp.sla.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.sf.json.JSONArray;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Data
public class SlaOutageDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;//自增主键

    private String almShortName;

    private String almApplicationId;
    private String projectName;//项目名称
    private String projectId;//项目id
    private String serviceName;//服务名称
    private String serviceId;//服务id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp outageEndDate;//宕机截止时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp outageStartDate;//宕机起始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp queryEndDate;//查询截止时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp queryStartDate;//查询起始时间
    private Integer outageTime;//宕机时间
    private String ifOvertime;//是否超30min （0:否；1：是）
    private String ifNotRun;//是否业务无法执行造成严重损失 （0:否；1：是）
    private Integer deductScore;//扣分
    private Integer lostMoney;//扣款 金额
    private Integer cutPayment;//扣款 经理人天
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp outageConfirmDate;//宕机确认时间(最后一次确认时间)
/*    private String supplier;//供应商
    private String supplierId;//供应商id*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;//创建时间
    private String operator;//操作人（最后一次修改人）
    private String operatorId;//操作人id（最后一次修改人id）
    private Integer paasId;//paas平台唯一标识
    private String status;//处理状态 （0:未处理；1：已处理）
    private String ifDel;//是否删除 （0:否；1：是）
    private String tSlaListId;//t_sla_list主键
    private String url;//宕机时间链接地址
    private String assessStatus;//考核状态 0:不考核；1考核
    private String cancelAssessReason;//取消考核原因
    private JSONArray supplierList;

}
