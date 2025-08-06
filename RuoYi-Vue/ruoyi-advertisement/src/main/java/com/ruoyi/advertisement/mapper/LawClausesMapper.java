package com.ruoyi.advertisement.mapper;

import java.util.List;
import com.ruoyi.advertisement.domain.LawClauses;

/**
 * 法律法规管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-22
 */
public interface LawClausesMapper 
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
     * 删除法律法规管理
     * 
     * @param id 法律法规管理主键
     * @return 结果
     */
    public int deleteLawClausesById(Long id);

    /**
     * 批量删除法律法规管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLawClausesByIds(Long[] ids);

    /**
     * 根据id批量查询法条记录
     * @param lawIds
     * @return
     */
    List<LawClauses> selectLawClausesByIds(String[] lawIds);
}
