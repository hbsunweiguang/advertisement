package com.ruoyi.advertisement.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.domain.AdvertisementBody;
import com.ruoyi.advertisement.domain.AdvertisementVo;
import com.ruoyi.advertisement.domain.AuditRecords;

/**
 * 广告列表Service接口
 * 
 * @author ruoyi
 * @date 2025-07-21
 */
public interface IAdvertisementService 
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
    public List<Advertisement> selectAdvertisementList(AdvertisementBody advertisement);

    /**
     * 新增广告列表
     * 
     * @param advertisement 广告列表
     * @return 结果
     */
    public int insertAdvertisement(Advertisement advertisement);

    /**
     * 采集者使用小程序新增广告
     *
     * @param advertisement 广告
     * @return 结果
     */
    public int insertAdvertisementByCollect(Advertisement advertisement);

    /**
     * 修改广告列表
     * 
     * @param advertisement 广告列表
     * @return 结果
     */
    public int updateAdvertisement(Advertisement advertisement);

    /**
     * 批量删除广告列表
     * 
     * @param ids 需要删除的广告列表主键集合
     * @return 结果
     */
    public int deleteAdvertisementByIds(Long[] ids);

    /**
     * 删除广告列表信息
     * 
     * @param id 广告列表主键
     * @return 结果
     */
    public int deleteAdvertisementById(Long id);

    /**
     * 审批广告
     * @param vo 审批记录
     * @return
     */
    public int approveAdvertisement(AdvertisementVo vo);

    /**
     * 查询行业和媒体
     * @return
     */
    public void selectIndustryAndMedium();

    /**
     * 添加行业类型到redis中
     * @param industry
     * @return
     */
    public String insertIndustry(String  industry);

    /**
     * 从redis中查询行业类型列表
     * @return
     */
    public List<String> selectIndustry();

    /**
     * 根据名称从redis中查询行业类型
     * @param industry
     * @return
     */
    public String selectIndustryByIndustry(String industry);

    /**
     * 从redis中删除行业类型
     * @param industry
     * @return
     */
    public int deleteIndustry(String industry);

    /**
     * 添加媒体类型到redis中
     * @param medium
     * @return
     */
    public String insertMedium(String  medium);

    /**
     * 从redis中查询媒体类型列表
     * @return
     */
    public List<String> selectMedium();

    /**
     * 根据名称从redis中查询媒体类型
     * @param medium
     * @return
     */
    public String selectMediumByMedium(String medium);

    /**
     * 从redis中删除媒体类型
     * @param medium
     * @return
     */
    public int deleteMedium(String medium);

    /**
     * 查询广告各状态数量
     * @return
     */
    public Map<String, Integer> selectCount();

    /**
     * 自动复审
     */
    public void automatedReview();
    /**
     * 统计查询采集者数据
     */
    public Map<String, Integer> getCollector();
}
