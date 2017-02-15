package com.haier.adp.sla.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haier.adp.jira.MetricJiraService;
import com.haier.adp.jira.dto.StoryTransferInfo;
import com.haier.adp.jira.impl.MetricJiraServiceImpl;
import com.haier.adp.sla.dao.SlaDetailDAO;
import com.haier.adp.sla.dao.SlaListDAO;
import com.haier.adp.sla.dto.SlaDetailDTO;
import com.haier.adp.sla.dto.SlaListDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Service
@Slf4j
public class SlaOTDServiceImpl implements SlaOTDService {
    @Resource
    SlaListDAO slaListDAO;
    @Resource
    SlaDetailDAO slaDetailDAO;
    // JIRA平台度量数据查询服务
    private static final MetricJiraService jiraService = new MetricJiraServiceImpl();
    @Override
    public List<SlaListDTO> getSlaList(SlaListDTO searchDTO) {
        List<SlaListDTO> resultList = new ArrayList<SlaListDTO>();
        resultList = slaListDAO.search(searchDTO);
        return resultList;
    }
    @Override
    public int queryListCount(SlaListDTO searchDTO) {
        int count = slaListDAO.queryCount(searchDTO);
        return count;
    }
    @Override
    public int queryDetailCount(SlaDetailDTO searchDTO) {
        int count = slaDetailDAO.queryCount(searchDTO);
        return count;
    }
    @Override
    public List<SlaDetailDTO> getSysOperSlaList(Date queryStartDate, Date queryEndDate, String project, String projectType) {
        List<SlaDetailDTO> resultList = new ArrayList<SlaDetailDTO>();
        SlaDetailDTO searchDTO = new SlaDetailDTO();
        searchDTO.setQueryStartDate(queryStartDate);
        searchDTO.setQueryEndDate(queryEndDate);
        resultList = slaDetailDAO.search(searchDTO);
        return resultList;
    }
    @Override
    public List<SlaDetailDTO> getSlaDetail(SlaDetailDTO searchDTO){
        List<SlaDetailDTO> resultList = new ArrayList<SlaDetailDTO>();
        resultList = slaDetailDAO.search(searchDTO);
        return resultList;
    }
    @Override
    public List<StoryTransferInfo> getJiraStoriesByProjectAndDate(String productShortName, Date queryStartDate, Date queryEndDate) {
        if (StringUtils.isBlank(productShortName)) {
            log.info("项目简称为空，则返回空信息一览");
            return Collections.EMPTY_LIST;
        }
        List<StoryTransferInfo> storyList = new ArrayList<StoryTransferInfo>();
        try {
            storyList = jiraService.getStoryList(productShortName, queryStartDate, queryEndDate);
        } catch (ParseException e) {
            log.error("jira查询信息时错误。{}", e);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return storyList;
    }
    @Override
    public int insertDetailData(SlaDetailDTO detailDTO){
        int detailId = slaDetailDAO.insertDetailData(detailDTO);
        return detailId;
    };
    @Override
    public void updateListIdForDetailData(SlaDetailDTO detailDTO){
        slaDetailDAO.updateListIdForDetailData(detailDTO);
    };
    @Override
    public void updateListShownStatus(SlaListDTO listDTO){
        slaListDAO.updateListShownStatus(listDTO);
    };
    @Override
    public void updateSlaDetail(SlaDetailDTO detailDTO){
        slaDetailDAO.updateSlaDetail(detailDTO);
    };
    @Override
    public void updateListPaidStatus(SlaListDTO listDTO){
        slaListDAO.updateListPaidStatus(listDTO);
    };

    @Override
    public int insertListData(SlaListDTO listDTO){
        slaListDAO.insertListData(listDTO);
        int listId = listDTO.getId();
        return  listId;
    };
}
