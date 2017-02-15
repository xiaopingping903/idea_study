package com.haier.adp.sla.service;

import com.haier.adp.jira.MetricJiraService;
import com.haier.adp.jira.dto.BugStatisticsInfo;
import com.haier.adp.jira.impl.MetricJiraServiceImpl;
import com.haier.adp.sla.dao.SlaBugInfoDAO;
import com.haier.adp.sla.dto.BugInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by rsdeep on 2016/12/26.
 */
@Slf4j
public class SlaBugInfoServiceImpl implements SlaBugInfoService {
    private static final MetricJiraService jiraService = new MetricJiraServiceImpl();

    @Autowired
    private SlaBugInfoDAO slaBugInfoDAO;
    @Override
    public List<BugInfoDTO> getBugInfoList(BugInfoDTO searchDTO) {
        List<BugInfoDTO> result = slaBugInfoDAO.search(searchDTO);
        return result;
    }

    @Override
    public int querySlaBugInfoCount(BugInfoDTO searchDTO) {
        int bugCount = slaBugInfoDAO.queryCount(searchDTO);
        return bugCount;
    }
    @Override
    public int insertBugInfo(BugInfoDTO insertDTO){
        int bugId = slaBugInfoDAO.insertBugInfo(insertDTO);
        return bugId;
    }
    @Override
    public void updateBugListId(List<BugInfoDTO> list, int listId){
        for (BugInfoDTO info : list) {
            info.setListId(listId);
            slaBugInfoDAO.insertBugInfo(info);
        }
    }
}
