package com.ruoyi.advertisement.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 执行结果记录对象 tb_enforcement
 * 
 * @author ruoyi
 * @date 2025-07-22
 */
public class Enforcement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 处理Id */
    private Long id;
    /**处理单号**/
    private String adCode;
    /** 广告Id */
    @Excel(name = "广告Id")
    private Long adId;

    /** 处理结果 0未处理 1已处理（已拆除） 2已处理（已罚款） */
    @Excel(name = "处理结果 0未处理 1已处理", readConverterExp = "已=拆除")
    private String handleResult;

    /** 处理后照片 */
    @Excel(name = "处理后照片")
    private String postHandleImage;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date handleTime;

    /** 处理者 */
    @Excel(name = "处理者")
    private String handler;
    /**广告详情*/
    private Advertisement advertisement;
    // 用于接收查询参数，不对应数据库字段
    private String violationType;
    // 用于接收查询参数，不对应数据库字段
    @Excel(name = "营利类型")
    private String adProfitabilityType;
    // 用于接收查询参数，不对应数据库字段
    @Excel(name = "行业分类")
    // 用于接收查询参数，不对应数据库字段
    private String adIndustryType;
    private List<String> adIndustryTypes;
    // 用于接收查询参数，不对应数据库字段
    /** 媒体类型 */
    @Excel(name = "媒体类型")
    private String adMediumType;
    private List<String> adMediumTypes;
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
    // getter 和 setter
    public String getViolationType() {
        return violationType;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }


    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAdId(Long adId) 
    {
        this.adId = adId;
    }

    public Long getAdId() 
    {
        return adId;
    }

    public void setHandleResult(String handleResult) 
    {
        this.handleResult = handleResult;
    }

    public String getHandleResult() 
    {
        return handleResult;
    }

    public void setPostHandleImage(String postHandleImage) 
    {
        this.postHandleImage = postHandleImage;
    }

    public String getPostHandleImage() 
    {
        return postHandleImage;
    }

    public void setHandleTime(Date handleTime) 
    {
        this.handleTime = handleTime;
    }

    public Date getHandleTime() 
    {
        return handleTime;
    }

    public void setHandler(String handler) 
    {
        this.handler = handler;
    }

    public String getHandler() 
    {
        return handler;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public String getAdProfitabilityType() {
        return adProfitabilityType;
    }

    public void setAdProfitabilityType(String adProfitabilityType) {
        this.adProfitabilityType = adProfitabilityType;
    }

    public String getAdIndustryType() {
        return adIndustryType;
    }

    public void setAdIndustryType(String adIndustryType) {
        this.adIndustryType = adIndustryType;
    }

    public String getAdMediumType() {
        return adMediumType;
    }

    public void setAdMediumType(String adMediumType) {
        this.adMediumType = adMediumType;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("adCode",getAdCode())
            .append("adId", getAdId())
            .append("handleResult", getHandleResult())
            .append("postHandleImage", getPostHandleImage())
            .append("handleTime", getHandleTime())
            .append("handler", getHandler())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
