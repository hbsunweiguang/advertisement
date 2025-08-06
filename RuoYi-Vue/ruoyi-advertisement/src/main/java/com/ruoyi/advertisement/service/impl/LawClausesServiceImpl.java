package com.ruoyi.advertisement.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.advertisement.mapper.LawClausesMapper;
import com.ruoyi.advertisement.domain.LawClauses;
import com.ruoyi.advertisement.service.ILawClausesService;

/**
 * 法律法规管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-22
 */
@Service
public class LawClausesServiceImpl implements ILawClausesService 
{
    @Autowired
    private LawClausesMapper lawClausesMapper;

    /**
     * 查询法律法规管理
     * 
     * @param id 法律法规管理主键
     * @return 法律法规管理
     */
    @Override
    public LawClauses selectLawClausesById(Long id)
    {
        return lawClausesMapper.selectLawClausesById(id);
    }

    /**
     * 查询法律法规管理列表
     * 
     * @param lawClauses 法律法规管理
     * @return 法律法规管理
     */
    @Override
    public List<LawClauses> selectLawClausesList(LawClauses lawClauses)
    {

        return lawClausesMapper.selectLawClausesList(lawClauses);
    }

    /**
     * 新增法律法规管理
     * 
     * @param lawClauses 法律法规管理
     * @return 结果
     */
    @Override
    public int insertLawClauses(LawClauses lawClauses)
    {
        //当前登录的用户id
        lawClauses.setCreateBy(Long.toString(SecurityUtils.getLoginUser().getUserId()));
        lawClauses.setCreateTime(DateUtils.getNowDate());
        return lawClausesMapper.insertLawClauses(lawClauses);
    }

    /**
     * 修改法律法规管理
     * 
     * @param lawClauses 法律法规管理
     * @return 结果
     */
    @Override
    public int updateLawClauses(LawClauses lawClauses)
    {
        //当前登录的用户id
        lawClauses.setCreateBy(Long.toString(SecurityUtils.getLoginUser().getUserId()));
        lawClauses.setUpdateTime(DateUtils.getNowDate());
        return lawClausesMapper.updateLawClauses(lawClauses);
    }

    /**
     * 批量删除法律法规管理
     * 
     * @param ids 需要删除的法律法规管理主键
     * @return 结果
     */
    @Override
    public int deleteLawClausesByIds(Long[] ids)
    {
        return lawClausesMapper.deleteLawClausesByIds(ids);
    }

    /**
     * 删除法律法规管理信息
     * 
     * @param id 法律法规管理主键
     * @return 结果
     */
    @Override
    public int deleteLawClausesById(Long id)
    {
        return lawClausesMapper.deleteLawClausesById(id);
    }
}
