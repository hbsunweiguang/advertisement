package com.ruoyi.advertisement.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.advertisement.domain.Enforcement;
import org.apache.ibatis.annotations.Param;

/**
 * 执行结果记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-22
 */
public interface EnforcementMapper 
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
     * 删除执行结果记录
     * 
     * @param id 执行结果记录主键
     * @return 结果
     */
    public int deleteEnforcementById(Long id);

    /**
     * 批量删除执行结果记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEnforcementByIds(Long[] ids);
    /**
     * 根据广告id查询执行结果记录
     * @param adId
     * @return
     */
    public Enforcement selectEnforcementByAdId(Long adId);

    /**
     * 根据用户ids，查询处理任务最少的一位处理人员
     * @param userIds
     * @return
     */
    public Enforcement selectEnforcementByUserIds(@Param("userIds") List<Long> userIds);
    /**
     * wx端查询所有执行结果记录
     * @param enforcement
     * @return
     */
    public List<Enforcement> selectEnforcementAll(Enforcement enforcement);

    /**
     * 查询今日新增的处理数量
     * @return
     */
    public int selectAddTodayByHandle();

    /**
     * 查询未处理数量
     * @return
     */
    public int selectHandleCount();

    /**
     * 查询48小时未处理数量
     * @return
     */
    public int selectLongTermUntreatedByHandle();

    /**
     * 获取处理人员
     * @return
     */
    public Map<String, Integer> getHandler(Long userId);
}
