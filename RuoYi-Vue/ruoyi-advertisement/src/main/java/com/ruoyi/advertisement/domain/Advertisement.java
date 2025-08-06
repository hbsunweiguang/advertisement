package com.ruoyi.advertisement.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 广告列表对象 tb_advertisement
 * 
 * @author ruoyi
 * @date 2025-07-21
 */
public class Advertisement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 营利类型 */
    @Excel(name = "营利类型")
    private String adProfitabilityType;

    /** 行业分类 */
    @Excel(name = "行业分类")
    private String adIndustryType;
    private List<String> adIndustryTypes;

    /** 媒体类型 */
    @Excel(name = "媒体类型")
    private String adMediumType;
    private List<String> adMediumTypes;

    /** 违法行为描述 */
    @Excel(name = "违法行为描述")
    private String adDescription;

    /** 广告图片路径 */
    @Excel(name = "广告图片路径")
    private String adImages;

    /** 违规类别 */
    @Excel(name = "违规类别")
    private String violationType;

    /** 省  */
    @Excel(name = "省 ")
    private String province;

    /** 市  */
    @Excel(name = "市 ")
    private String city;

    /** 区/县 */
    @Excel(name = "区/县")
    private String district;

    /** 街道 */
    @Excel(name = "街道")
    private String street;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String address;

    /** 经度 */
    @Excel(name = "经度")
    private String latitude;

    /** 维度 */
    @Excel(name = "维度")
    private String longitude;

    /** 广告主 */
    @Excel(name = "广告主")
    private String advertiser;

    /** 采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采集时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date surveyTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginSurveyTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endSurveyTime;

    /** 监测人Id */
    private Integer surveyorId;
    /** 监测人 */
    @Excel(name = "监测人")
    private String surveyor;

    /** 查看状态 */
    @Excel(name = "查看状态")
    private String checkStatus;

    /** 查看时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "查看时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    /** 处理状态  */
    @Excel(name = "处理状态 ")
    private String handleStatus;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;
    /** 审核状态的集合，用于接收多个状态值 */
    private List<String> auditStatuses;

    /** 报告地址 */
    @Excel(name = "报告地址")
    private String report;

    /** 逻辑删除标志 */
    @Excel(name = "逻辑删除标志")
    private Integer isDeleted;

    /** 创建者姓名 */
    private String createName;

    /** 更新者姓名 */
    private String updateName;

    private List<AuditRecords> auditRecordsList;

    private Enforcement enforcement;

    public Date getBeginSurveyTime() {
        return beginSurveyTime;
    }

    public void setBeginSurveyTime(Date beginSurveyTime) {
        this.beginSurveyTime = beginSurveyTime;
    }

    public Date getEndSurveyTime() {
        return endSurveyTime;
    }

    public void setEndSurveyTime(Date endSurveyTime) {
        this.endSurveyTime = endSurveyTime;
    }

    public List<String> getAuditStatuses() {
        return auditStatuses;
    }

    public void setAuditStatuses(List<String> auditStatuses) {
        this.auditStatuses = auditStatuses;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public List<String> getAdIndustryTypes() {
        return adIndustryTypes;
    }

    public void setAdIndustryTypes(List<String> adIndustryTypes) {
        this.adIndustryTypes = adIndustryTypes;
    }

    public List<String> getAdMediumTypes() {
        return adMediumTypes;
    }

    public void setAdMediumTypes(List<String> adMediumTypes) {
        this.adMediumTypes = adMediumTypes;
    }

    public void setAdProfitabilityType(String adProfitabilityType)
    {
        this.adProfitabilityType = adProfitabilityType;
    }

    public String getAdProfitabilityType() 
    {
        return adProfitabilityType;
    }

    public void setAdIndustryType(String adIndustryType) 
    {
        this.adIndustryType = adIndustryType;
    }

    public String getAdIndustryType() 
    {
        return adIndustryType;
    }

    public void setAdMediumType(String adMediumType) 
    {
        this.adMediumType = adMediumType;
    }

    public String getAdMediumType() 
    {
        return adMediumType;
    }

    public void setAdDescription(String adDescription) 
    {
        this.adDescription = adDescription;
    }

    public String getAdDescription() 
    {
        return adDescription;
    }

    public void setAdImages(String adImages) 
    {
        this.adImages = adImages;
    }

    public String getAdImages() 
    {
        return adImages;
    }

    public void setViolationType(String violationType) 
    {
        this.violationType = violationType;
    }

    public String getViolationType() 
    {
        return violationType;
    }

    public void setProvince(String province) 
    {
        this.province = province;
    }

    public String getProvince() 
    {
        return province;
    }

    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }

    public void setDistrict(String district) 
    {
        this.district = district;
    }

    public String getDistrict() 
    {
        return district;
    }

    public void setStreet(String street) 
    {
        this.street = street;
    }

    public String getStreet() 
    {
        return street;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setLatitude(String latitude) 
    {
        this.latitude = latitude;
    }

    public String getLatitude() 
    {
        return latitude;
    }

    public void setLongitude(String longitude) 
    {
        this.longitude = longitude;
    }

    public String getLongitude() 
    {
        return longitude;
    }

    public void setAdvertiser(String advertiser) 
    {
        this.advertiser = advertiser;
    }

    public String getAdvertiser() 
    {
        return advertiser;
    }

    public void setSurveyTime(Date surveyTime) 
    {
        this.surveyTime = surveyTime;
    }

    public Date getSurveyTime() 
    {
        return surveyTime;
    }

    public void setSurveyor(String surveyor) 
    {
        this.surveyor = surveyor;
    }

    public String getSurveyor() 
    {
        return surveyor;
    }

    public void setCheckStatus(String checkStatus) 
    {
        this.checkStatus = checkStatus;
    }

    public String getCheckStatus() 
    {
        return checkStatus;
    }

    public void setCheckTime(Date checkTime) 
    {
        this.checkTime = checkTime;
    }

    public Date getCheckTime() 
    {
        return checkTime;
    }

    public void setHandleStatus(String handleStatus) 
    {
        this.handleStatus = handleStatus;
    }

    public String getHandleStatus() 
    {
        return handleStatus;
    }

    public void setAuditStatus(String auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus() 
    {
        return auditStatus;
    }

    public void setReport(String report) 
    {
        this.report = report;
    }

    public String getReport() 
    {
        return report;
    }

    public void setIsDeleted(Integer isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted() 
    {
        return isDeleted;
    }

    public Integer getSurveyorId() {
        return surveyorId;
    }

    public void setSurveyorId(Integer surveyorId) {
        this.surveyorId = surveyorId;
    }

    public List<AuditRecords> getAuditRecordsList() {
        return auditRecordsList;
    }

    public void setAuditRecordsList(List<AuditRecords> auditRecordsList) {
        this.auditRecordsList = auditRecordsList;
    }

    public Enforcement getEnforcement() {
        return enforcement;
    }

    public void setEnforcement(Enforcement enforcement) {
        this.enforcement = enforcement;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("adProfitabilityType", getAdProfitabilityType())
            .append("adIndustryType", getAdIndustryType())
            .append("adMediumType", getAdMediumType())
            .append("adDescription", getAdDescription())
            .append("adImages", getAdImages())
            .append("violationType", getViolationType())
            .append("province", getProvince())
            .append("city", getCity())
            .append("district", getDistrict())
            .append("street", getStreet())
            .append("address", getAddress())
            .append("latitude", getLatitude())
            .append("longitude", getLongitude())
            .append("advertiser", getAdvertiser())
            .append("surveyTime", getSurveyTime())
            .append("surveyor", getSurveyor())
            .append("checkStatus", getCheckStatus())
            .append("checkTime", getCheckTime())
            .append("handleStatus", getHandleStatus())
            .append("auditStatus", getAuditStatus())
            .append("report", getReport())
            .append("isDeleted", getIsDeleted())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
