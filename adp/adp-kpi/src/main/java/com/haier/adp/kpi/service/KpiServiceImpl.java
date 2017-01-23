package com.haier.adp.kpi.service;

import com.haier.adp.jira.MetricJiraService;
import com.haier.adp.jira.dto.BugStatisticsInfo;
import com.haier.adp.jira.dto.StoryTransferInfo;
import com.haier.adp.jira.impl.MetricJiraServiceImpl;
import com.haier.adp.kpi.dao.PPQAInfoDao;
import com.haier.adp.kpi.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 01435337 on 2016/11/3.
 */
@Service(value = "kpiService")
@Slf4j
public class KpiServiceImpl implements KpiService {

    // JIRA平台度量数据查询服务
    private static final MetricJiraService jiraService = new MetricJiraServiceImpl();

    @Autowired
    private PPQAInfoDao ppqaDao;

    /**
     * 通过JIRA平台查询BUG统计信息一览
     *
     * @param projectShortName 项目简称
     * @param fromDate 开始时间
     * @param toDate 结束时间
     * @return BUG统计信息一览
     */
    @Override
    public List<BugStatisticsInfo> getBugStatistics(String projectShortName, Date fromDate, Date toDate) {
        if (StringUtils.isBlank(projectShortName)) {
            log.info("项目简称为空，则返回空信息一览");
            return Collections.EMPTY_LIST;
        }
        if ((fromDate == null && toDate == null)
                || (fromDate != null && toDate != null && fromDate.compareTo(toDate) > 0)) {
            log.info("开始时间和结束时间为空、或开始时间大于结束时间时，则返回空信息一览。");
            return Collections.EMPTY_LIST;
        }

        try {
             return jiraService.getBugStatistics(projectShortName, fromDate, toDate);
        } catch (Exception e) {
            log.error("查询JIRA信息时发生系统错误。{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询Jira Story开发交付
     *
     * @param productShortName
     * @param fromDate
     * @param toDate
     * @return
     */
    @Override
    public List<StoryDeliveryInfo> getStoryDeliveryInfos(String productShortName, Date fromDate, Date toDate) {
        if (StringUtils.isBlank(productShortName)) {
            log.info("项目简称为空，则返回空信息一览");
            return Collections.EMPTY_LIST;
        }
        if ((fromDate == null && toDate == null)
                || (fromDate != null && toDate != null && fromDate.compareTo(toDate) > 0)) {
            log.info("开始时间和结束时间为空、或开始时间大于结束时间时，则返回空信息一览。");
            return Collections.EMPTY_LIST;
        }

        List<StoryTransferInfo> infos = null;
        try {
            infos = jiraService.getStoryList(productShortName, null, null);
        } catch (Exception e) {
            log.error("查询JIRA信息时发生系统错误。{}", e);
            throw new RuntimeException(e);
        }

        if (CollectionUtils.isEmpty(infos)) {
            return Collections.EMPTY_LIST;
        }

        List<StoryDeliveryInfo> storyDeliveryInfos = new ArrayList<>(infos.size());
        for (StoryTransferInfo info : infos) {
            StoryDeliveryInfo storyDeliveryInfo = new StoryDeliveryInfo();
            storyDeliveryInfo.setEpicKey(info.getEpicKey());
            storyDeliveryInfo.setStoryKey(info.getStoryKey());
            storyDeliveryInfo.setRequireId(info.getRequireId());
            storyDeliveryInfo.setTaskId(info.getTaskId());
            storyDeliveryInfo.setReleaseDate(info.getReleaseDate());
            storyDeliveryInfo.setDueDate(info.getDueDate());
            storyDeliveryInfo.setOnTime(this.checkWhetherOnTimeDelivery(info.getDueDate(), info.getReleaseDate()));

            storyDeliveryInfos.add(storyDeliveryInfo);
        }

        return storyDeliveryInfos;
    }

    // 判断需求交付是否及时？
    private boolean checkWhetherOnTimeDelivery(Date dueDate, Date releaseDate) {
        if (dueDate == null) {
            return true;
        }
        if (releaseDate == null) {
            return false;
        }

        if (dueDate.compareTo(releaseDate) < 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据项目简称查询所有已关闭的Story.
     *
     * @param productShortName
     * @return
     */
    @Override
    public List<JiraStory> getAllClosedJiraStories(String productShortName) {
        if (StringUtils.isBlank(productShortName)) {
            log.info("项目简称为空，则返回空信息一览");
            return Collections.EMPTY_LIST;
        }
        List<StoryTransferInfo> infos = null;
        try {
            infos = jiraService.getStoryList(productShortName, null, null);
        } catch (Exception e) {
            log.error("查询JIRA信息时发生系统错误。{}", e);
            throw new RuntimeException(e);
        }

        if (CollectionUtils.isEmpty(infos)) {
            return Collections.EMPTY_LIST;
        }
        List<JiraStory> stories = new ArrayList<>(infos.size());
        for (StoryTransferInfo info : infos) {
            JiraStory story = new JiraStory();
            story.setEpicKey(info.getEpicKey());
            story.setStoryKey(info.getStoryKey());
            story.setRequireId(info.getRequireId());
            story.setTaskId(info.getTaskId());
            story.setStoryTitle(info.getSummary());
            story.setReleaseDate(info.getReleaseDate());

            stories.add(story);
        }
        return stories;
    }

    /**
     * 更新Story的发布时间
     *
     * @param params
     */
    @Override
    public void updateStoryReleaseTime(Map<String, String> params) {
        if (MapUtils.isEmpty(params)) {
            log.error("更新Story用参数为空。");
            return;
        }

        try {
            jiraService.updateStorylistReleaseTime(params);
        } catch (Exception ex) {
            log.error("更新Story的发布时间时出现系统错误。{}", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * 根据查询条件检索所有的PPQA信息
     *
     * @param condition
     * @return
     */
    @Override
    public List<PPQAInfo> searchPPQAInfos(PPQASearchCondition condition) {
        return ppqaDao.search(condition);
    }

    /**
     * 追加新的PPQA信息,并返回根据查询条件取得的最新数据
     *
     * @param ppqaInfo
     * @param searchCondition
     * @return
     */
    @Override
    public ReturnValue addPPQAInfo(PPQAInfo ppqaInfo, PPQASearchCondition searchCondition) {
        ReturnValue rv = new ReturnValue();

        // 1) 本次考核周期开始日 > 既有周期的结束日
        // 2）本次考核周期的结束日 < 既有周期的开始日
        if ((ppqaInfo.getStartDate() != null
                && ppqaDao.isLaterThanEndDate(ppqaInfo.getStartDate()))
                || (ppqaInfo.getEndDate() != null
                && ppqaDao.isEalierThanStartDate(ppqaInfo.getEndDate()))) {

            // 插入本次考核周期PPQA信息
            ppqaDao.insert(ppqaInfo);

            // 根据查询条件取得最新的PPQA信息
            List<PPQAInfo> searchResults = ppqaDao.search(searchCondition);
            rv.setSuccess(true);
            rv.setContent(searchResults);
        } else {
            rv.setSuccess(false);
            rv.setContent("本次考核周期开始结束时期与既有的周期重叠。");
        }

        return rv;
    }
}
