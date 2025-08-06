package com.ruoyi.advertisement.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.domain.AdvertisementBody;
import org.apache.ibatis.annotations.MapKey;

/**
 * 广告列表Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-21
 */
public interface AdvertisementMapper 
{
    /**
     * 查询广告列表
     * 
     * @param id 广告列表主键
     * @return 广告列表
     */
    public Advertisement selectAdvertisementById(Long id);

    /**
     * 根据id查询广告、审批记录、处理结果详细数据
     *
     * @param id 广告列表主键
     * @return 广告列表
     */
    public Advertisement selectAdvertisementAndAuditAndEnforcementById(Long id);
    /**
     * 查询广告列表列表
     * 
     * @param advertisement 广告列表
     * @return 广告列表集合
     */
    public List<Advertisement> selectAdvertisementList(Advertisement advertisement);

    /**
     * 小程序查询广告列表
     *
     * @param advertisement 广告列表
     * @return 广告列表集合
     */
    public List<Advertisement> selectAdvertisements(AdvertisementBody advertisement);

    /**
     * 新增广告列表
     * 
     * @param advertisement 广告列表
     * @return 结果
     */
    public int insertAdvertisement(Advertisement advertisement);

    /**
     * 修改广告列表
     * 
     * @param advertisement 广告列表
     * @return 结果
     */
    public int updateAdvertisement(Advertisement advertisement);

    /**
     * 删除广告列表
     * 
     * @param id 广告列表主键
     * @return 结果
     */
    public int deleteAdvertisementById(Long id);

    /**
     * 批量删除广告列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAdvertisementByIds(Long[] ids);

    public List<Advertisement> selectIndustryAndMedium();

    /**
     * 待查看广告数量
     * @return 数量
     */
    public int selectCheckCount();

    /**
     * 各审核状态广告数量
     * @param auditStatus 0未审核  1初审合法  2初审存疑  3初审违法  4复审合法  5驳回初审  6复审违法  7自动复审合法  8自动复审违法
     * @return 数量
     */
    public int selectApproveCount(String auditStatus);

    /**
     * 添加今日广告数量
     * @param status 1 初审今日新增  2 复审今日新增  3 处理今日新增
     * @return 数量
     */
    public int selectAddToday(int status);

    /**
     * 长期未处理广告数量
     * @param status 1 初审长期未处理  2 复审长期未处理  3 处理长期未处理
     * @return 数量
     */
    public int selectLongTermUntreated(int status);

    /**
     * 各区域广告数量
     * @return 数量
     */
    @MapKey("district")
    public List<Map<String, Integer>> selectAdvertisementCountByDistrict();

    /**
     * 各审核状态广告数量
     * @return 数量
     */
    @MapKey("district")
    public List<Map<String, Object>> selectAdvertisementCountByAuditStatus();

    /**
     * 各区域违法广告数量
     * @return 数量
     */
    @MapKey("district")
    public List<Map<String, Integer>> selectIllegalAdvertisementCountByDistrict();

    /**
     * 根据行业分布统计广告数量
     * @return 行业分布统计结果
     */
    @MapKey("industryType")
    public List<Map<String, Object>> selectAdvertisementCountByIndustry();

    /**
     * 根据媒体类型统计广告数量
     * @return 媒体类型统计结果
     */
    @MapKey("mediumType")
    public List<Map<String, Object>> selectAdvertisementCountByMediumType();

    /**
     * 根据营利类型统计广告数据
     * @return 营利类型统计结果
     */
    @MapKey("profitabilityType")
    public List<Map<String, Object>> selectAdvertisementCountByProfitabilityType();

    /**
     * 最新违法广告Top20
     * @return
     */
    public List<Advertisement> selectAdvertisementByTime();
    /**
     * 统计查询采集者数据
     */
    public Map<String, Integer> getCollector(Long createBy);
}
