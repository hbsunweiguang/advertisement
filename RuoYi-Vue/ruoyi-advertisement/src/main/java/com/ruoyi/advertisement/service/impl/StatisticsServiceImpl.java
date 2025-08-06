package com.ruoyi.advertisement.service.impl;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.mapper.AdvertisementMapper;
import com.ruoyi.advertisement.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName : StatisticsServiceImpl</p>
 * <p>Description : </p>
 *
 * @author 孙伟光
 * @version 1.0
 * @date 2025/8/4 9:55
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Autowired
    private AdvertisementMapper advertisementMapper;

    @Override
    public Map<String, Object> list() {
        //定义统计Map
        Map<String, Object> map = new HashMap<>();
        // 查询地区当月采集广告总量
        List<Map<String, Integer>> districtCountMap =  advertisementMapper.selectAdvertisementCountByDistrict();

        // 查询行业当月采集广告违法率
        List<Map<String, Object>> auditStatusCountMap =  advertisementMapper.selectAdvertisementCountByAuditStatus();
        // 转换数据结构，按地区分组
        List<Map<String, Object>> groupedAuditStatusMap = groupByDistrict(auditStatusCountMap);

        // 查询行业采集广告地区分布热力图
        List<Map<String, Integer>> districtIllegalCountMap = advertisementMapper.selectIllegalAdvertisementCountByDistrict();

        // 查询当月采集广告行业分布
        List<Map<String, Object> > industryCountMap = advertisementMapper.selectAdvertisementCountByIndustry();

        // 查询采集广告媒体类型
        List<Map<String, Object> > mediumCountMap = advertisementMapper.selectAdvertisementCountByMediumType();

        // 查询采集广告公益/商业占比
        List<Map<String, Object>> profitabilityCountMap = advertisementMapper.selectAdvertisementCountByProfitabilityType();
        // 转换数据结构，按营利类型分组
        List<Map<String, Object>> groupedProfitabilityCountMap = groupByProfitabilityType(profitabilityCountMap);

        // 最新违法广告Top20
        List<Advertisement> latestIllegalAdvertisementList = advertisementMapper.selectAdvertisementByTime();

        map.put("districtCountMap", districtCountMap);
        map.put("auditStatusCountMap", groupedAuditStatusMap);
        map.put("districtIllegalCountMap", districtIllegalCountMap);
        map.put("industryCountMap", industryCountMap);
        map.put("mediumCountMap", mediumCountMap);
        map.put("profitabilityCountMap", groupedProfitabilityCountMap);
        map.put("latestIllegalAdvertisementList", latestIllegalAdvertisementList);
        return map;
    }

    /**
     * 按地区分组统计数据
     * @param rawData 原始数据
     * @return 按地区分组后的数据
     */
    private List<Map<String, Object>> groupByDistrict(List<Map<String, Object>> rawData) {
        // 使用Map来临时存储按地区分组的数据
        Map<String, List<Map<String, Object>>> districtGroupMap = new HashMap<>();

        // 按district字段分组
        for (Map<String, Object> item : rawData) {
            String district = (String) item.get("district");

            // 如果该地区还没有在分组Map中，则创建一个新的列表
            if (!districtGroupMap.containsKey(district)) {
                districtGroupMap.put(district, new ArrayList<>());
            }
            // 创建新的status对象，只包含count和audit_status字段
            Map<String, Object> statusItem = new HashMap<>();
            statusItem.put("count", item.get("count"));
            statusItem.put("audit_status", item.get("audit_status"));

            // 将status对象添加到对应地区的列表中
            districtGroupMap.get(district).add(statusItem);
        }

        // 构造最终结果
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : districtGroupMap.entrySet()) {
            Map<String, Object> districtItem = new HashMap<>();
            districtItem.put("district", entry.getKey());
            districtItem.put("status", entry.getValue());
            result.add(districtItem);
        }

        return result;
    }

    /**
     * 按营利类型分组统计数据
     * @param rawData 原始数据
     * @return 按营利类型分组后的数据
     */
    private List<Map<String, Object>> groupByProfitabilityType(List<Map<String, Object>> rawData) {
        // 使用Map来临时存储按营利类型分组的数据
        Map<String, List<Map<String, Object>>> profitabilityGroupMap = new HashMap<>();

        // 按profitability_type字段分组
        for (Map<String, Object> item : rawData) {
            String profitabilityType = (String) item.get("profitability_type");

            // 如果该营利类型还没有在分组Map中，则创建一个新的列表
            if (!profitabilityGroupMap.containsKey(profitabilityType)) {
                profitabilityGroupMap.put(profitabilityType, new ArrayList<>());
            }
            // 创建新的time对象，只包含month和count字段
            Map<String, Object> timeItem = new HashMap<>();
            timeItem.put("month", item.get("month"));
            timeItem.put("count", item.get("count"));

            // 将time对象添加到对应营利类型的列表中
            profitabilityGroupMap.get(profitabilityType).add(timeItem);
        }

        // 构造最终结果
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : profitabilityGroupMap.entrySet()) {
            Map<String, Object> profitabilityItem = new HashMap<>();
            profitabilityItem.put("profitability_type", entry.getKey());
            profitabilityItem.put("time", entry.getValue());
            result.add(profitabilityItem);
        }

        return result;
    }
}
