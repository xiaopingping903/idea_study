package com.haier.adp.sla.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/13.
 */
public class StoryInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String epicKey;
    private String requireId;
    private String storyKey;
    private String taskId;
    private String assigneeId;
    private String assigneeName;
    private Date dueDate;
    private Date releaseDate;
    private Date closeDate;
    private String summary;
    private String epicTitle;
    private String status;

    public StoryInfo() {
    }

    public String getEpicKey() {
        return this.epicKey;
    }

    public void setEpicKey(String epicKey) {
        this.epicKey = epicKey;
    }

    public String getRequireId() {
        return this.requireId;
    }

    public void setRequireId(String requireId) {
        this.requireId = requireId;
    }

    public String getStoryKey() {
        return this.storyKey;
    }

    public void setStoryKey(String storyKey) {
        this.storyKey = storyKey;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAssigneeId() {
        return this.assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeName() {
        return this.assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getCloseDate() {
        return this.closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEpicTitle() {
        return this.epicTitle;
    }

    public void setEpicTitle(String epicTitle) {
        this.epicTitle = epicTitle;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
