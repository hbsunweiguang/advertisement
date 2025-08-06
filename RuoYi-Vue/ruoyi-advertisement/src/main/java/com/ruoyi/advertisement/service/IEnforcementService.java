package com.ruoyi.advertisement.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.advertisement.domain.Enforcement;

/**
 * 执行结果记录Service接口
 * 
 * @author ruoyi
 * @date 2025-07-22
 */
public interface IEnforcementService 
{
    /**
     * 查询执行结果记录
     * 
     * @param id 执行结果记录主键
     * @return 执行结果记录
     */
    public Enforcement selectEnforcementById(Long id);

    /**
     * 查询执行结果记录列表
     * 
     * @param enforcement 执行结果记录
     * @return 执行结果记录集合
     */
    public List<Enforcement> selectEnforcementList(Enforcement enforcement);

    /**
     * 新增执行结果记录
     * 
     * @param enforcement 执行结果记录
     * @return 结果
     */
    public int insertEnforcement(Enforcement enforcement);

    /**
     * 修改执行结果记录
     * 
     * @param enforcement 执行结果记录
     * @return 结果
     */
    public int updateEnforcement(Enforcement enforcement);

    /**
     * 批量删除执行结果记录
     * 
     * @param ids 需要删除的执行结果记录主键集合
     * @return 结果
     */
    public int deleteEnforcementByIds(Long[] ids);

    /**
     * 删除执行结果记录信息
     * 
     * @param id 执行结果记录主键
     * @return 结果
     */
    public int deleteEnforcementById(Long id);
    /**
     * 根据广告id查询执行结果记录
     * @param adId
     * @return
     */
    public Enforcement selectEnforcementByAdId(Long adId);

    /**
     * 查看pdf
     * @param adId
     * @return
     */
    public String selectReport(Long adId);
    /**
     * wx端查询所有执行结果记录
     * @param enforcement
     * @return
     */
    public List<Enforcement> selectEnforcementAll(Enforcement enforcement);

    /**
     * 获取执行数量
     * @return
     */
    public Map<String, Integer> getHandler();
}
