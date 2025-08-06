package com.ruoyi.advertisement.service;

import java.util.List;
import com.ruoyi.advertisement.domain.Regional;

/**
 * 行政区域管理Service接口
 * 
 * @author mu
 * @date 2025-07-22
 */
public interface IRegionalService 
{
    /**
     * 获取 Redis 中所有行政区域的编码和名称，并以 Regional 对象列表形式返回
     *
     * @return 返回 Regional 对象列表
     */
    List<Regional> getAllRegionalFromCache();
    
    /**
     * 查询行政区域管理
     * 
     * @param code 行政区域管理主键
     * @return 行政区域管理
     */
    public Regional selectRegionalByCode(String code);

    /**
     * 查询行政区域管理列表
     * 
     * @param regional 行政区域管理
     * @return 行政区域管理集合
     */
    public List<Regional> selectRegionalList(Regional regional);

    /**
     * 新增行政区域管理
     * 
     * @param regional 行政区域管理
     * @return 结果
     */
    public int insertRegional(Regional regional);

    /**
     * 修改行政区域管理
     * 
     * @param regional 行政区域管理
     * @return 结果
     */
    public int updateRegional(Regional regional);

    /**
     * 批量删除行政区域管理
     * 
     * @param codes 需要删除的行政区域管理主键集合
     * @return 结果
     */
    public int deleteRegionalByCodes(String[] codes);

    /**
     * 删除行政区域管理信息
     * 
     * @param code 行政区域管理主键
     * @return 结果
     */
    public int deleteRegionalByCode(String code);
}
