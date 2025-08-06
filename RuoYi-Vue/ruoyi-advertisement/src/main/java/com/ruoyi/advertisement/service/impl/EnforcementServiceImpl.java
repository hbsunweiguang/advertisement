package com.ruoyi.advertisement.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ruoyi.advertisement.controller.PdfUtils;
import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.service.IAdvertisementService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.advertisement.mapper.EnforcementMapper;
import com.ruoyi.advertisement.domain.Enforcement;
import com.ruoyi.advertisement.service.IEnforcementService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 执行结果记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-22
 */
@Service
public class EnforcementServiceImpl implements IEnforcementService
{
    @Autowired
    private EnforcementMapper enforcementMapper;
    @Autowired
    private IAdvertisementService advertisementService;
    @Autowired
    private PdfUtils pdfUtils;

    /**
     * 查询执行结果记录
     * 
     * @param id 执行结果记录主键
     * @return 执行结果记录
     */
    @Override
    public Enforcement selectEnforcementById(Long id)
    {
        return enforcementMapper.selectEnforcementById(id);
    }

    /**
     * 查询执行结果记录列表
     * 
     * @param enforcement 执行结果记录
     * @return 执行结果记录
     */
    @Override
    public List<Enforcement> selectEnforcementList(Enforcement enforcement)
    {
        return enforcementMapper.selectEnforcementList(enforcement);
    }

    /**
     * 新增执行结果记录
     * 
     * @param enforcement 执行结果记录
     * @return 结果
     */

    @Override
    public int insertEnforcement(Enforcement enforcement) {
     return    enforcementMapper.insertEnforcement(enforcement);
    }

    /**
     * 修改执行结果记录
     * 
     * @param enforcement 执行结果记录
     * @return 结果
     */
//    @Transactional
    @Override
    public int updateEnforcement(Enforcement enforcement)
    {
        if(enforcement.getAdId() == null || selectEnforcementByAdId(enforcement.getAdId()) == null){
            throw new RuntimeException("未找到对应的广告信息，广告ID: " + enforcement.getAdId());
        }else{
            // 设置单据编号
            enforcement.setAdCode(generateCode());
            // 设置处理人
            enforcement.setHandler(SecurityUtils.getUsername());
            // 设置创建人
            enforcement.setCreateBy(Long.toString(SecurityUtils.getUserId()));
            enforcement.setCreateTime(DateUtils.getNowDate());

            // 设置处理时间
            enforcement.setHandleTime(DateUtils.getNowDate());
            // 更新执法记录
            int insertResult = enforcementMapper.updateEnforcement(enforcement);
            if (insertResult <= 0) {
                throw new RuntimeException("执法记录插入失败");
            }

            try {
                updateAdvertisementStatus(enforcement);
            } catch (Exception e) {
                // 可在此处添加日志记录
                throw new RuntimeException("更新广告状态时发生错误", e);
            }

            return insertResult;
        }

    }
    private void updateAdvertisementStatus(Enforcement enforcement) {
        Advertisement ad = advertisementService.selectAdvertisementById(enforcement.getAdId());
        if (ad == null) {
            throw new RuntimeException("未找到对应的广告信息，广告ID: " + enforcement.getAdId());
        }

        ad.setHandleStatus(enforcement.getHandleResult());

        // 自动生成pdf报告
        Map<String, Object> resultMap = pdfUtils.generateAndSavePdf(enforcement.getAdId());
        String fileNameKey = "fileName"; // 定义为常量防止拼写错误
        if (resultMap != null && resultMap.containsKey(fileNameKey)) {
            ad.setReport(resultMap.get(fileNameKey).toString());
        } else {
            throw new RuntimeException("PDF生成失败，未获取到文件名");
        }

        int updateResult = advertisementService.updateAdvertisement(ad);
        if (updateResult <= 0) {
            throw new RuntimeException("广告状态更新失败");
        }
    }
    /**
     * 批量删除执行结果记录
     * 
     * @param ids 需要删除的执行结果记录主键
     * @return 结果
     */
    @Override
    public int deleteEnforcementByIds(Long[] ids)
    {
        return enforcementMapper.deleteEnforcementByIds(ids);
    }

    /**
     * 删除执行结果记录信息
     * 
     * @param id 执行结果记录主键
     * @return 结果
     */
    @Override
    public int deleteEnforcementById(Long id)
    {
        return enforcementMapper.deleteEnforcementById(id);
    }
    /**
     * 生成单据编码
     * 格式：DB + 年月日 + 6位随机数
     *
     * @return 生成的调拨单编码
     */
    public static String generateCode() {
        // 获取当前日期并格式化为yyyyMMdd格式
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 生成6位随机数，不足前面补0
        String randomPart = String.format("%06d", new Random().nextInt(999999));
        // 拼接前缀、日期和随机数部分
        return String.join("","RS" , datePart, randomPart);
    }
    @Override
    public Enforcement selectEnforcementByAdId(Long adId){
        return enforcementMapper.selectEnforcementByAdId(adId);
    }

    /**
     * 查看pdf
     * @param adId
     * @return
     */
    public String selectReport(Long adId){
        return advertisementService.selectAdvertisementById(adId).getReport();
    }

    @Override
    public List<Enforcement> selectEnforcementAll(Enforcement enforcement) {
        return enforcementMapper.selectEnforcementAll(enforcement);
    }

    @Override
    public Map<String, Integer> getHandler() {
        Map<String, Integer> map = enforcementMapper.getHandler(SecurityUtils.getUserId());
        return map;
    }
}
