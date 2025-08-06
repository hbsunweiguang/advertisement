package com.ruoyi.advertisement.domain;

import com.ruoyi.common.annotation.Excel;

/**
 * 审批vo类
 *
 */
public class AdvertisementVo {
    /** 广告主键ID */
    private Long adId;
    /** 违法行为描述 */
    private String adDescription;
    /** 违规类别 */
    private String violationType;
    /** 审核状态 */
    private String auditStatus;

    /** 法律编号 */
    private String clauseCode;

    /** 审核意见 */
    private String auditComments;

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }

    public String getViolationType() {
        return violationType;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getClauseCode() {
        return clauseCode;
    }

    public void setClauseCode(String clauseCode) {
        this.clauseCode = clauseCode;
    }

    public String getAuditComments() {
        return auditComments;
    }

    public void setAuditComments(String auditComments) {
        this.auditComments = auditComments;
    }
}
