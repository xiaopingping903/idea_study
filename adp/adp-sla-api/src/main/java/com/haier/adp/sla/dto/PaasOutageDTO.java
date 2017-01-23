package com.haier.adp.sla.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Data
public class PaasOutageDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;//自增主键
    private String appName;//项目名称
    private String appId;//项目id
    private String serviceName;//服务名称
    private String containerId;//实例
    private Timestamp downtimeEnd;//宕机截止时间
    private Timestamp downtimeBegin;//宕机起始时间
    private Integer downtimeDelay;//宕机持续时间
    private String health;//状态：0健康；1宕机
    private String date;//统计日期

}
