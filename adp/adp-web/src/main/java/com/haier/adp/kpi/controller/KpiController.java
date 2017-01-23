package com.haier.adp.kpi.controller;

import com.haier.adp.jira.dto.BugStatisticsInfo;
import com.haier.adp.kpi.dto.PPQAInfo;
import com.haier.adp.kpi.dto.PPQASearchCondition;
import com.haier.adp.kpi.dto.ReturnValue;
import com.haier.adp.kpi.service.KpiService;
import com.haier.adp.model.ResponseResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 01435337 on 2016/11/4.
 */
@Controller
@RequestMapping("/adp/kpi")
public class KpiController {
/*
    @Autowired
    private KpiService kpiService;

    *//**
     * 根据指定的查询条件获取BUG统计信息一览
     *
     * @param productShortName
     * @param fromDate
     * @param toDate
     * @return
     *//*
    @RequestMapping(value = "/getBugStatistics", method = RequestMethod.GET)
    public @ResponseBody ResponseResult getBugStatistics(
            @RequestParam(required = false) String productShortName,
            @RequestParam(required = false) Date fromDate,
            @RequestParam(required = false) Date toDate) {
        ResponseResult result = new ResponseResult();
        try {
            List<BugStatisticsInfo> bugStatistics = this.kpiService.getBugStatistics(productShortName, fromDate, toDate);
            if (CollectionUtils.isNotEmpty(bugStatistics)) {
                result.setSuccess(true);
                result.setContent(bugStatistics);
            } else {
                result.setSuccess(false);
                result.setContent("根据制定的查询条件，没有从JIRA中查找到BUG统计信息。");
            }
        } catch (RuntimeException ex) {
            result.setSuccess(false);
            result.setContent("JIRA查询过程中发生系统错误。");
        }
        return  result;
    }

    *//**
     * 根据指定的项目简称，查询所有的已关闭Story.
     *
     * @param productShortName
     * @return
     *//*
    @RequestMapping(value = "/getAllClosedJiraStories", method = RequestMethod.GET)
    public @ResponseBody ResponseResult getAllClosedJiraStories(@RequestParam String productShortName) {
        ResponseResult result = new ResponseResult();
        try {
            result.setSuccess(true);
            result.setContent(this.kpiService.getAllClosedJiraStories(productShortName));
        } catch (RuntimeException ex) {
            result.setSuccess(false);
            result.setContent("JIRA查询过程中发生系统错误。");
        }

        return result;
    }

    *//**
     * 根据查询条件判断对应的jira story是否开发及时
     *
     * @param productShortName
     * @param fromDate
     * @param toDate
     * @return
     *//*
    @RequestMapping(value = "/getStoryDeliveryInfos", method = RequestMethod.GET)
    public @ResponseBody ResponseResult getStoryDeliveryInfos(
            @RequestParam(required = false) String productShortName,
            @RequestParam(required = false) Date fromDate,
            @RequestParam(required = false) Date toDate) {
        ResponseResult result = new ResponseResult();
        try {
            result.setSuccess(true);
            result.setContent(this.kpiService.getStoryDeliveryInfos(productShortName, fromDate, toDate));
        } catch (RuntimeException ex) {
            result.setSuccess(false);
            result.setContent("JIRA查询过程中发生系统错误。");
        }

        return result;
    }

    *//**
     *
     * @param storyReleaseTimes
     * @return
     *//*
    @RequestMapping(value = "/updateStoryReleaseTime", method = RequestMethod.POST)
    public @ResponseBody ResponseResult updateStoryReleaseTime(@RequestParam Map<String, String> storyReleaseTimes) {
        ResponseResult result = new ResponseResult();
        try {
            this.kpiService.updateStoryReleaseTime(storyReleaseTimes);

            result.setSuccess(true);
            result.setContent("更新Story的发布时间成功。");
        } catch (RuntimeException ex) {
            result.setSuccess(false);
            result.setContent("更新Story的发布时间失败。");
        }

        return result;
    }

    *//**
     * 根据查询条件查询所有的PPQA信息
     *
     * @param startDate
     * @param endDate
     * @return
     *//*
    @RequestMapping(value = "/searchPPQAInfos", method = RequestMethod.GET)
    public @ResponseBody ResponseResult searchPPQAInfos(
            @RequestParam(value = "startDate", required = false) Date startDate,
            @RequestParam(value = "endDate", required = false) Date endDate) {
        PPQASearchCondition condition = new PPQASearchCondition();
        condition.setStartDate(startDate);
        condition.setEndDate(endDate);

        ResponseResult result = new ResponseResult();
        result.setSuccess(true);
        result.setContent(this.kpiService.searchPPQAInfos(condition));

        return result;
    }

    *//**
     * 上传PPQA报告文档
     *
     * @param reportFile
     * @param startDate
     * @param endDate
     * @return
     *//*
    @RequestMapping(value = "/uploadPPQAReport", method = RequestMethod.POST)
    public @ResponseBody ResponseResult uploadPPQAReport(
            @RequestParam("file") CommonsMultipartFile reportFile,
            @RequestParam(value = "startDate", required = false) Date startDate,
            @RequestParam(value = "endDate", required = false) Date endDate) {
        ResponseResult result = new ResponseResult();

        if (null == reportFile || reportFile.isEmpty()) {
            result.setSuccess(false);
            result.setContent("上传的报告文档不存在或者为空。");
            return result;
        }

        String fileName = reportFile.getName();
        if (StringUtils.isEmpty(fileName)
                || !StringUtils.equalsIgnoreCase(fileName, "xlsx")
                || !StringUtils.equalsIgnoreCase(fileName, "xls")) {
            result.setSuccess(false);
            result.setContent("上传的报告文档不是合法的Excel文档。");
            return result;
        }

        PPQAInfo ppqaInfo = this.extractPPQAInfo(reportFile);
        if (null == ppqaInfo) {
            result.setSuccess(false);
            result.setContent("解析PPQA报告失败，请再次确认PPQA报告内容或格式是否准确。");
            return result;
        }

        PPQASearchCondition searchCondition = new PPQASearchCondition();
        searchCondition.setStartDate(startDate);
        searchCondition.setEndDate(endDate);
        ReturnValue rv = this.kpiService.addPPQAInfo(ppqaInfo, searchCondition);

        result.setSuccess(rv.isSuccess());
        result.setContent(rv.getContent());

        return result;
    }

    *//*
     * 从上传的报告文档中解析需要的PPQA信息
     *//*
    private PPQAInfo extractPPQAInfo(CommonsMultipartFile reportFile) {
        try {
            POIFSFileSystem poiFS = new POIFSFileSystem(reportFile.getInputStream());
            HSSFWorkbook workbook = new HSSFWorkbook(poiFS);
            HSSFSheet sheet = workbook.getSheet("MasterSheet");

            PPQAInfo ppqaInfo = new PPQAInfo();

            // extract ppqa data
            HSSFRow authorRow = sheet.getRow(5);
            HSSFCell authorCell = authorRow.getCell(6);
            ppqaInfo.setAuthorName(StringUtils.trim(authorCell.getStringCellValue()));

            HSSFRow startDateRow = sheet.getRow(6);
            HSSFCell startYearCell = startDateRow.getCell(6);
            int startYear = 0;
            try {
                startYear = Integer.parseInt(startYearCell.getStringCellValue());
            } catch (NumberFormatException ex) {
                return null;
            }
            HSSFCell startMonthCell = startDateRow.getCell(7);
            LocalDate startDate =
                    new LocalDate(startYear, getMonthIntValue(startMonthCell.getStringCellValue()), 1);
            ppqaInfo.setStartDate(startDate.toDate());

            HSSFRow endDateRow = sheet.getRow(7);
            HSSFCell endYearCell = endDateRow.getCell(6);
            int endYear = 0;
            try {
                endYear = Integer.parseInt(endYearCell.getStringCellValue());
            } catch (NumberFormatException ex) {
                return null;
            }
            HSSFCell endMonthCell = startDateRow.getCell(7);
            LocalDate endDate =
                    new LocalDate(endYear, getMonthIntValue(endMonthCell.getStringCellValue()), 1).dayOfMonth().withMaximumValue();
            ppqaInfo.setEndDate(endDate.toDate());

            HSSFRow scoreRow = sheet.getRow(20);
            HSSFCell scoreCell = scoreRow.getCell(5);
            try {
                Float.parseFloat(scoreCell.getStringCellValue());
            } catch (NumberFormatException ex) {
                return null;
            }

            return ppqaInfo;
        } catch (Exception ex) {
            return null;
        }
    }

    private int getMonthIntValue(String month) {
        month = StringUtils.lowerCase(month);
        int val = 1;
        switch (month) {
            case "jan":
                val = 1;
                break;
            case "feb":
                val = 2;
                break;
            case "mar":
                val = 3;
                break;
            case "apr":
                val = 4;
                break;
            case "may":
                val = 5;
                break;
            case "jun":
                val = 6;
                break;
            case "jul":
                val = 7;
                break;
            case "aug":
                val = 8;
                break;
            case "sep":
                val = 9;
                break;
            case "oct":
                val = 10;
                break;
            case "nov":
                val = 11;
                break;
            case "dec":
                val = 12;
                break;
        }
        return val;
    }*/
}
