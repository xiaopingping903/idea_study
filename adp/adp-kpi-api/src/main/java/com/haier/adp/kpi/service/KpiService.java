package com.haier.adp.kpi.service;

import com.haier.adp.jira.dto.BugStatisticsInfo;
import com.haier.adp.kpi.dto.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 01435337 on 2016/11/3.
 */
public interface KpiService {

    /**
     * 通过JIRA平台查询BUG统计信息一览
     *
     * @param projectShortName 项目简称
     * @param fromDate 开始时间
     * @param toDate 结束时间
     * @return BUG统计信息一览
     */
    List<BugStatisticsInfo> getBugStatistics(String projectShortName, Date fromDate, Date toDate);

    /**
     * 查询Jira Story开发交付
     *
     * @param productShortName
     * @param fromDate
     * @param toDate
     * @return
     */
    List<StoryDeliveryInfo> getStoryDeliveryInfos(String productShortName, Date fromDate, Date toDate);

    /**
     *
     * @param productShortName
     * @return
     */
    List<JiraStory> getAllClosedJiraStories(String productShortName);

    /**
     * 更新Story的发布时间
     *
     * @param params
     */
    void updateStoryReleaseTime(Map<String, String> params);

    /**
     * 根据查询条件检索所有的PPQA信息
     *
     * @param condition
     * @return
     */
    List<PPQAInfo> searchPPQAInfos(PPQASearchCondition condition);

    /**
     * 追加新的PPQA信息
     *
     * @param ppqaInfo
     * @param searchCondition
     * @return
     */
    ReturnValue addPPQAInfo(PPQAInfo ppqaInfo, PPQASearchCondition searchCondition);
}