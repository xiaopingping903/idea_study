package com.haier.adp.sla.dto;

import com.haier.adp.sla.dto.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rsdeep on 2017/2/10.
 */
@Data
public class AlmResult implements Serializable {
    private static final long serialVersionUID = 1L;

    //返回值成功与否标志位
    private String flag;
    //返回信息
    private String content;
    // task列表
    List<SlaDetailDTO> taskList;
    // bug信息列表
    List<BugInfoDTO> bugList;
    // 宕机信息列表
    List<AlmOutageInterfaceDTO> outageList;
    // dubbo信息列表
    List<SlaMonitorInterfaceDTO> dubboList;
    // 奖励信息列表
    List<SlaBonuseInterfaceDTO> bonuseList;
}
