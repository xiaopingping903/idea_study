package com.generator;

import java.util.Date;

public class TSlaOutage {
    private Integer id;

    private String project;

    private String projectId;

    private String serviceName;

    private String serviceId;

    private Date outageEndDate;

    private Date outageStartDate;

    private Integer outageTime;

    private String ifOvertime;

    private String ifNotRun;

    private Integer deductScore;

    private Double lostMoney;

    private Integer cutPayment;

    private Date outageConfirmDate;

    private String supplier;

    private String supplierId;

    private Date createTime;

    private String operator;

    private String operatorId;

    private Integer paasId;

    private String status;

    private String ifDel;

    private Long tSlaListId;

    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project == null ? null : project.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public Date getOutageEndDate() {
        return outageEndDate;
    }

    public void setOutageEndDate(Date outageEndDate) {
        this.outageEndDate = outageEndDate;
    }

    public Date getOutageStartDate() {
        return outageStartDate;
    }

    public void setOutageStartDate(Date outageStartDate) {
        this.outageStartDate = outageStartDate;
    }

    public Integer getOutageTime() {
        return outageTime;
    }

    public void setOutageTime(Integer outageTime) {
        this.outageTime = outageTime;
    }

    public String getIfOvertime() {
        return ifOvertime;
    }

    public void setIfOvertime(String ifOvertime) {
        this.ifOvertime = ifOvertime == null ? null : ifOvertime.trim();
    }

    public String getIfNotRun() {
        return ifNotRun;
    }

    public void setIfNotRun(String ifNotRun) {
        this.ifNotRun = ifNotRun == null ? null : ifNotRun.trim();
    }

    public Integer getDeductScore() {
        return deductScore;
    }

    public void setDeductScore(Integer deductScore) {
        this.deductScore = deductScore;
    }

    public Double getLostMoney() {
        return lostMoney;
    }

    public void setLostMoney(Double lostMoney) {
        this.lostMoney = lostMoney;
    }

    public Integer getCutPayment() {
        return cutPayment;
    }

    public void setCutPayment(Integer cutPayment) {
        this.cutPayment = cutPayment;
    }

    public Date getOutageConfirmDate() {
        return outageConfirmDate;
    }

    public void setOutageConfirmDate(Date outageConfirmDate) {
        this.outageConfirmDate = outageConfirmDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public Integer getPaasId() {
        return paasId;
    }

    public void setPaasId(Integer paasId) {
        this.paasId = paasId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getIfDel() {
        return ifDel;
    }

    public void setIfDel(String ifDel) {
        this.ifDel = ifDel == null ? null : ifDel.trim();
    }

    public Long gettSlaListId() {
        return tSlaListId;
    }

    public void settSlaListId(Long tSlaListId) {
        this.tSlaListId = tSlaListId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }
}