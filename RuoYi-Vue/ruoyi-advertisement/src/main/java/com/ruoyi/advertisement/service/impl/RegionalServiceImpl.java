package com.ruoyi.advertisement.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.advertisement.mapper.RegionalMapper;
import com.ruoyi.advertisement.domain.Regional;
import com.ruoyi.advertisement.service.IRegionalService;

/**
 * 行政区域管理Service业务层处理
 * 
 * @author mu
 * @date 2025-07-22
 */
@Service
public class RegionalServiceImpl implements IRegionalService 
{
    @Autowired
    private RegionalMapper regionalMapper;

    /**
     * 获取所有行政区域的编码和名称，并以 Regional 对象列表形式返回
     *
     * @return 返回 Regional 对象列表
     */
    @Override
    public List<Regional> getAllRegionalFromCache() {
        Regional regional = new Regional();
        regional.setLevel(1L);
        List<Regional> regionals = regionalMapper.selectRegionalList(regional);
        Regional regional1 = new Regional();
        regional1.setLevel(2L);
        List<Regional> regionals1 = regionalMapper.selectRegionalList(regional1);
        Regional regional2 = new Regional();
        regional2.setLevel(3L);
        List<Regional> regionals2 = regionalMapper.selectRegionalList(regional2);
        regionals.addAll(regionals1);
        regionals.addAll(regionals2);
        return regionals;
    }

    /**
     * 查询行政区域管理
     * 
     * @param code 行政区域管理主键
     * @return 行政区域管理
     */
    @Override
    public Regional selectRegionalByCode(String code)
    {
        return regionalMapper.selectRegionalByCode(code);
    }

    /**
     * 查询行政区域管理列表
     * 
     * @param regional 行政区域管理
     * @return 行政区域管理
     */
    @Override
    public List<Regional> selectRegionalList(Regional regional)
    {
        return regionalMapper.selectRegionalList(regional);
    }

    /**
     * 新增行政区域管理
     * 
     * @param regional 行政区域管理
     * @return 结果
     */
    @Override
    public int insertRegional(Regional regional)
    {
        return regionalMapper.insertRegional(regional);
    }

    /**
     * 修改行政区域管理
     * 
     * @param regional 行政区域管理
     * @return 结果
     */
    @Override
    public int updateRegional(Regional regional)
    {
        return regionalMapper.updateRegional(regional);
    }

    /**
     * 批量删除行政区域管理
     * 
     * @param codes 需要删除的行政区域管理主键
     * @return 结果
     */
    @Override
    public int deleteRegionalByCodes(String[] codes)
    {
        return regionalMapper.deleteRegionalByCodes(codes);
    }

    /**
     * 删除行政区域管理信息
     * 
     * @param code 行政区域管理主键
     * @return 结果
     */
    @Override
    public int deleteRegionalByCode(String code)
    {
        return regionalMapper.deleteRegionalByCode(code);
    }
}
