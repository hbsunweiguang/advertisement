package com.ruoyi.advertisement.domain;

import com.ruoyi.common.annotation.Excel;

import java.util.List;

/**
 * <p>ClassName : AdvertisementBody</p>
 * <p>Description : </p>
 *
 * @author 孙伟光
 * @version 1.0
 * @date 2025/7/29 15:31
 */
public class AdvertisementBody {
    /** 违规类别 null是全部 1禁用词类 2虚假宣传 3低俗类 4敏感类 5其他类*/
    private String violationType;

    /** 登录人 */
    private String user;

    /** 登录人Id */
    private Integer userId;

    /** 审核状态 0 审核中 1 已通过 2 未通过 */
    private Integer auditStatus;

    /** 处理状态 0 待处理  1已完成 */
    private Integer handleStatus;

    public String getViolationType() {
        return violationType;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }
}
