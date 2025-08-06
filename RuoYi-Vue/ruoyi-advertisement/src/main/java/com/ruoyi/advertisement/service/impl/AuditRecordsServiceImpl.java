package com.ruoyi.advertisement.service.impl;

import java.util.List;

import com.ruoyi.advertisement.mapper.LawClausesMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.advertisement.mapper.AuditRecordsMapper;
import com.ruoyi.advertisement.domain.AuditRecords;
import com.ruoyi.advertisement.service.IAuditRecordsService;

import javax.annotation.Resource;

/**
 * 审核记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-22
 */
@Service
public class AuditRecordsServiceImpl implements IAuditRecordsService 
{
    @Autowired
    private AuditRecordsMapper auditRecordsMapper;

    /**
     * 查询审核记录
     * 
     * @param id 审核记录主键
     * @return 审核记录
     */
    @Override
    public AuditRecords selectAuditRecordsById(Long id)
    {
        return auditRecordsMapper.selectAuditRecordsById(id);
    }

    @Resource
    private LawClausesMapper lawClausesMapper;
    /**
     * 查询审核记录列表
     * 
     * @param auditRecords 审核记录
     * @return 审核记录
     */
    @Override
    public List<AuditRecords> selectAuditRecordsList(AuditRecords auditRecords)
    {
        //查看记录列表
        List<AuditRecords> auditRecordsList = auditRecordsMapper.selectAuditRecordsList(auditRecords);
        //遍历获取法条记录
        for (int i = 0; i < auditRecordsList.size(); i++) {
            if (StringUtils.isNotEmpty(auditRecordsList.get(i).getClauseCode())){
                //拆分法条数组
                String[] lawIds = auditRecordsList.get(i).getClauseCode().split(",");
                //根据法条id批量查询
                auditRecordsList.get(i).setLawClausesList(lawClausesMapper.selectLawClausesByIds(lawIds));
            }
        }
        return auditRecordsList;
    }

    /**
     * 新增审核记录
     * 
     * @param auditRecords 审核记录
     * @return 结果
     */
    @Override
    public int insertAuditRecords(AuditRecords auditRecords)
    {
        auditRecords.setCreateTime(DateUtils.getNowDate());
        return auditRecordsMapper.insertAuditRecords(auditRecords);
    }

    /**
     * 修改审核记录
     * 
     * @param auditRecords 审核记录
     * @return 结果
     */
    @Override
    public int updateAuditRecords(AuditRecords auditRecords)
    {
        auditRecords.setUpdateTime(DateUtils.getNowDate());
        return auditRecordsMapper.updateAuditRecords(auditRecords);
    }

    /**
     * 批量删除审核记录
     * 
     * @param ids 需要删除的审核记录主键
     * @return 结果
     */
    @Override
    public int deleteAuditRecordsByIds(Long[] ids)
    {
        return auditRecordsMapper.deleteAuditRecordsByIds(ids);
    }

    /**
     * 删除审核记录信息
     * 
     * @param id 审核记录主键
     * @return 结果
     */
    @Override
    public int deleteAuditRecordsById(Long id)
    {
        return auditRecordsMapper.deleteAuditRecordsById(id);
    }
}
