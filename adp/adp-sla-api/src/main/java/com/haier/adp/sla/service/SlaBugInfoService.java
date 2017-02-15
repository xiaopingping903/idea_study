package com.haier.adp.sla.service;

import com.haier.adp.jira.dto.BugStatisticsInfo;
import com.haier.adp.sla.dto.BugInfoDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by rsdeep on 2016/12/26.
 */
public interface SlaBugInfoService {
    List<BugInfoDTO> getBugInfoList(BugInfoDTO searchDTO);
    int querySlaBugInfoCount(BugInfoDTO searchDTO);
    int insertBugInfo(BugInfoDTO insertDTO);
    void updateBugListId(List<BugInfoDTO> bugs, int listId);
}
