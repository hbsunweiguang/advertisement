package com.ruoyi.advertisement.service;

import java.util.List;
import com.ruoyi.advertisement.domain.LawClauses;

/**
 * 法律法规管理Service接口
 * 
 * @author ruoyi
 * @date 2025-07-22
 */
public interface ILawClausesService 
{
    /**
     * 查询法律法规管理
     * 
     * @param id 法律法规管理主键
     * @return 法律法规管理
     */
    public LawClauses selectLawClausesById(Long id);

    /**
     * 查询法律法规管理列表
     * 
     * @param lawClauses 法律法规管理
     * @return 法律法规管理集合
     */
    public List<LawClauses> selectLawClausesList(LawClauses lawClauses);

    /**
     * 新增法律法规管理
     * 
     * @param lawClauses 法律法规管理
     * @return 结果
     */
    public int insertLawClauses(LawClauses lawClauses);

    /**
     * 修改法律法规管理
     * 
     * @param lawClauses 法律法规管理
     * @return 结果
     */
    public int updateLawClauses(LawClauses lawClauses);

    /**
     * 批量删除法律法规管理
     * 
     * @param ids 需要删除的法律法规管理主键集合
     * @return 结果
     */
    public int deleteLawClausesByIds(Long[] ids);

    /**
     * 删除法律法规管理信息
     * 
     * @param id 法律法规管理主键
     * @return 结果
     */
    public int deleteLawClausesById(Long id);
}
