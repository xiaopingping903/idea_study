
package com.haier.adp.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>almJiraRelation complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="almJiraRelation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="almRequestId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="almTaskId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jiraEpicId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taskDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taskTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "almJiraRelation", propOrder = {
    "almRequestId",
    "almTaskId",
    "jiraEpicId",
    "taskDesc",
    "taskTitle"
})
public class AlmJiraRelation {

    protected String almRequestId;
    protected String almTaskId;
    protected String jiraEpicId;
    protected String taskDesc;
    protected String taskTitle;

    /**
     * ��ȡalmRequestId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlmRequestId() {
        return almRequestId;
    }

    /**
     * ����almRequestId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlmRequestId(String value) {
        this.almRequestId = value;
    }

    /**
     * ��ȡalmTaskId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlmTaskId() {
        return almTaskId;
    }

    /**
     * ����almTaskId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlmTaskId(String value) {
        this.almTaskId = value;
    }

    /**
     * ��ȡjiraEpicId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJiraEpicId() {
        return jiraEpicId;
    }

    /**
     * ����jiraEpicId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJiraEpicId(String value) {
        this.jiraEpicId = value;
    }

    /**
     * ��ȡtaskDesc���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaskDesc() {
        return taskDesc;
    }

    /**
     * ����taskDesc���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaskDesc(String value) {
        this.taskDesc = value;
    }

    /**
     * ��ȡtaskTitle���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaskTitle() {
        return taskTitle;
    }

    /**
     * ����taskTitle���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaskTitle(String value) {
        this.taskTitle = value;
    }

}
