package com.haier.adp.dto;

import com.haier.adp.sla.dto.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsdeep on 2017/2/10.
 */
@Data
public class AlmResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    String successFalg;

    String failureMessage;

    ArrayList<SlaDetailDTO> taskList;

    ArrayList<BugInfoDTO> bugList;

    ArrayList<AlmOutageInterfaceDTO> outageList;

    ArrayList<SlaMonitorInterfaceDTO> dubboList;

    ArrayList<SlaBonuseInterfaceDTO> bonuseList;
}
