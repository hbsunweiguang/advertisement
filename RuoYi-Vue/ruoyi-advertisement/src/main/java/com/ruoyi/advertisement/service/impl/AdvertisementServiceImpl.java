package com.ruoyi.advertisement.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.advertisement.domain.*;
import com.ruoyi.advertisement.mapper.AuditRecordsMapper;
import com.ruoyi.advertisement.mapper.EnforcementMapper;
import com.ruoyi.advertisement.mapper.LawClausesMapper;
import com.ruoyi.advertisement.service.IAdvertisementService;
import com.ruoyi.advertisement.service.IEnforcementService;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.map.AddressComponent;
import com.ruoyi.common.utils.map.MapUtils;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.advertisement.mapper.AdvertisementMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * 广告列表Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-21
 */
@Service
public class AdvertisementServiceImpl implements IAdvertisementService
{
    @Autowired
    private AdvertisementMapper advertisementMapper;

    @Autowired
    private AuditRecordsMapper auditRecordsMapper;

    @Autowired
    private LawClausesMapper lawClausesMapper;
    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private SysUserMapper userMapper;



    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MapUtils mapUtils;

    @Autowired
    private EnforcementMapper enforcementMapper;

    /**
     * 查询广告列表
     * 
     * @param id 广告列表主键
     * @return 广告列表
     */
    @Override
    public Advertisement selectAdvertisementById(Long id)
    {
        return advertisementMapper.selectAdvertisementById(id);
    }

    /**
     * 根据广告ID查询广告详情及关联的审批记录和处理结果
     *
     * @param id 广告ID，作为查询主键
     * @return Advertisement 包含广告基本信息、审批记录列表和处理结果的复合对象
     *         返回null表示未找到对应广告
     */
    @Override
    public Advertisement selectAdvertisementAndAuditAndEnforcementById(Long id) {
        // 通过广告ID查询广告基本信息及关联的审批记录、处理结果
        Advertisement advertisement = advertisementMapper.selectAdvertisementAndAuditAndEnforcementById(id);

        // 检查广告对象是否存在
        if(advertisement != null) {
            // 检查广告是否关联了审批记录
            if (advertisement.getAuditRecordsList() != null && advertisement.getAuditRecordsList().size() > 0){
                // 遍历每条审批记录
                for (AuditRecords auditRecords : advertisement.getAuditRecordsList()){
                    // 检查审批记录是否关联了法规条款
                    if (auditRecords.getClauseCode() != null) {
                        // 将逗号分隔的法规ID字符串拆分为数组
                        String [] lawIds = auditRecords.getClauseCode().split(",");
                        // 检查是否存在法规ID
                        if (lawIds.length > 0) {
                            // 根据法规ID数组查询具体的法规条款列表
                            List<LawClauses> lawClausesList = lawClausesMapper.selectLawClausesByIds(lawIds);
                            // 初始化法规名称和条款编号字符串
                            String lawName = "";
                            String clauseNumber = "";
                            // 遍历法规条款列表，拼接名称和编号
                            for (LawClauses lawClauses : lawClausesList){
                                lawName += lawClauses.getLawName() + ",";
                                clauseNumber += lawClauses.getClauseNumber() + ",";
                            }
                            // 去掉最后的逗号
                            if (lawName.length() > 0) {
                                lawName = lawName.substring(0, lawName.length() - 1);
                            }
                            if (clauseNumber.length() > 0) {
                                clauseNumber = clauseNumber.substring(0, clauseNumber.length() - 1);
                            }
                            // 将拼接好的法规信息设置到审批记录中
                            auditRecords.setLawName(lawName);
                            auditRecords.setClauseNumber(clauseNumber);
                        }
                    }
                }
            }
        }
        return advertisement;
    }
    /**
     * 查询广告列表列表
     * 
     * @param advertisement 广告列表
     * @return 广告列表
     */
    @Override
    public List<Advertisement> selectAdvertisementList(Advertisement advertisement)
    {
        return advertisementMapper.selectAdvertisementList(advertisement);
    }

    /**
     * 小程序查询广告列表
     *
     * @param advertisement 广告列表
     * @return 广告列表集合
     */
    @Override
    public List<Advertisement> selectAdvertisementList(AdvertisementBody advertisement) {
        return advertisementMapper.selectAdvertisements(advertisement);
    }

    /**
     * 新增广告列表
     * 
     * @param advertisement 广告列表
     * @return 结果
     */
    @Override
    public int insertAdvertisement(Advertisement advertisement)
    {
        //逻辑删除
        advertisement.setIsDeleted(0);
        //查看状态
        advertisement.setCheckStatus("0");
        //处理状态
        advertisement.setHandleStatus("0");
        //审核状态
        advertisement.setAuditStatus("0");
        //当前登录的用户信息
        advertisement.setCreateBy(Long.toString(SecurityUtils.getLoginUser().getUserId()));
        //创建时间
        advertisement.setCreateTime(DateUtils.getNowDate());

        return advertisementMapper.insertAdvertisement(advertisement);
    }

    @Override
    public int insertAdvertisementByCollect(Advertisement advertisement) {
        //调用高德地图根据经纬度获取地址详细信息
        AddressComponent addressComponent = mapUtils.findAddressByLngLat(advertisement.getLongitude(), advertisement.getLatitude());
        advertisement.setProvince(addressComponent.getProvince());
        advertisement.setCity(addressComponent.getCity());
        advertisement.setDistrict(addressComponent.getDistrict());
        advertisement.setStreet(addressComponent.getTownship());
        //采集时间
        advertisement.setSurveyTime(DateUtils.getNowDate());
        //逻辑删除
        advertisement.setIsDeleted(0);
        //查看状态
        advertisement.setCheckStatus("0");
        //处理状态
        advertisement.setHandleStatus("0");
        //审核状态
        advertisement.setAuditStatus("0");
        //创建时间
        advertisement.setCreateTime(DateUtils.getNowDate());
        //当前登录的用户信息
        advertisement.setCreateBy(Integer.toString(advertisement.getSurveyorId()));
        return advertisementMapper.insertAdvertisement(advertisement);
    }

    /**
     * 修改广告列表
     * 
     * @param advertisement 广告列表
     * @return 结果
     */
    @Override
    public int updateAdvertisement(Advertisement advertisement)
    {
        //当前登录的用户信息
        advertisement.setUpdateBy(Long.toString(SecurityUtils.getLoginUser().getUserId()));
        advertisement.setUpdateTime(DateUtils.getNowDate());
        return advertisementMapper.updateAdvertisement(advertisement);
    }

    /**
     * 批量删除广告列表
     * 
     * @param ids 需要删除的广告列表主键
     * @return 结果
     */
    @Override
    public int deleteAdvertisementByIds(Long[] ids)
    {
        return advertisementMapper.deleteAdvertisementByIds(ids);
    }

    /**
     * 删除广告列表信息
     * 
     * @param id 广告列表主键
     * @return 结果
     */
    @Override
    public int deleteAdvertisementById(Long id)
    {
        return advertisementMapper.deleteAdvertisementById(id);
    }

    /**
     * 审批广告
     * @param vo 审批记录
     * @return
     */
    @Override
    @Transactional
    public int approveAdvertisement(AdvertisementVo vo) {
        //更新广告的审批状态
        Advertisement advertisement = advertisementMapper.selectAdvertisementById(vo.getAdId());
        //advertisement.setId(vo.getAdId()); //广告id
        advertisement.setAuditStatus(vo.getAuditStatus());//审批类型
        advertisement.setAdDescription(vo.getAdDescription());//违法描述
        advertisement.setViolationType(vo.getViolationType());//违法类型
        //当前登录的用户信息
        advertisement.setUpdateBy(Long.toString(SecurityUtils.getLoginUser().getUserId()));
        advertisement.setUpdateTime(DateUtils.getNowDate());
        //执行更新操作
        advertisementMapper.updateAdvertisement(advertisement);
        //审批状态为6(复审违法)或8（自动复审违法）时候，需要新增处理记录
        if(vo.getAuditStatus().equals("6") || vo.getAuditStatus().equals("8")){
            //新增广告执行
            insertEnforcementByReview(advertisement);
        }

        //审批记录新增数据
        AuditRecords records = new AuditRecords();
        records.setAdId(vo.getAdId()); //广告id
        records.setClauseCode(vo.getClauseCode());//法规编号
        records.setAuditComments(vo.getAuditComments());//审核意见
        records.setAuditStatus(vo.getAuditStatus()); //审核状态
        records.setAuditor(Long.toString(SecurityUtils.getLoginUser().getUserId()));
        records.setCreateTime(DateUtils.getNowDate());
        records.setCreateBy(Long.toString(SecurityUtils.getLoginUser().getUserId()));
        records.setAuditTime(DateUtils.getNowDate());
        //审批记录
        auditRecordsMapper.insertAuditRecords(records);

        return 1;
    }
    /**
     * 审批状态为6(复审违法)或8（自动复审违法）时候，需要新增处理记录
     * @param advertisement 广告信息
     */
    private void insertEnforcementByReview(Advertisement advertisement) {
        //获取广告信息
        //Advertisement advertisement = advertisementMapper.selectAdvertisementById(adId);

        //1、查询数据处理部门id
        SysDept dept = new SysDept();
        dept.setDeptName("数据处理部门");
        List<SysDept> sysDepts = deptMapper.selectDeptList(dept);
        //2、在数据处理的基础上，查询某个区县的数据处理部门id，例如桥西区的id
        dept.setDeptName(advertisement.getDistrict());
        dept.setParentId(sysDepts.get(0).getDeptId());
        sysDepts = deptMapper.selectDeptList(dept);
        //3、查询某个区县的数据处理部门的人员：根据部门id查询
        SysUser user = new SysUser();
        user.setDeptId(sysDepts.get(0).getDeptId());
        List<SysUser> sysUsers = userMapper.selectUserList(user);
        List<Long> userIds =new ArrayList<>();
        for (SysUser sysUser : sysUsers) {
            userIds.add(sysUser.getUserId());
        }
        //4、根据用户ids，查询处理任务最少的一位处理人员
        Enforcement enforcement = enforcementMapper.selectEnforcementByUserIds(userIds);
        //没有查询到数据：指导第一个为处理人员
        if (enforcement == null){
            //根据id查询用户信息
            SysUser sysUser = userMapper.selectUserById(userIds.get(0));
            //创建处理对象
            enforcement = new Enforcement();
            // 设置处理人
            enforcement.setHandler(sysUser.getNickName());
            // 设置创建人
            enforcement.setCreateBy(sysUser.getUserId().toString());
        }
        //根据id查询用户信息
        SysUser sysUser = userMapper.selectUserByCreateBy(Long.parseLong(enforcement.getCreateBy()));
        // 设置处理人
        enforcement.setHandler(sysUser.getNickName());
        // 设置创建人
        enforcement.setCreateBy(sysUser.getUserId().toString());
        enforcement.setAdId(advertisement.getId());//设置广告id
        enforcement.setHandleResult("0"); //设置处理状态：未处理
        enforcement.setAdCode(EnforcementServiceImpl.generateCode()); //设置单据编号
        enforcement.setCreateTime(DateUtils.getNowDate());//设置新建时间

        // 插入执法记录
        enforcementMapper.insertEnforcement(enforcement);
    }



    /**
     * 项目启动时，初始化行业和媒体到缓存
     */
    @PostConstruct
    public void init()
    {
        selectIndustryAndMedium();
    }

    /**
     * 查询行业和媒体
     * @return
     */
    @Override
    public void selectIndustryAndMedium() {
        List<Advertisement> list = advertisementMapper.selectIndustryAndMedium();

        // 使用LinkedHashMap保持插入顺序，并记录出现次数
        Map<String, Integer> industryMap = new LinkedHashMap<>();
        Map<String, Integer> mediumMap = new LinkedHashMap<>();

        // 初始化常见行业数据
        industryMap.put("餐饮行业", 0);
        industryMap.put("房地产行业", 0);
        industryMap.put("教育培训行业", 0);
        industryMap.put("金融服务行业", 0);
        industryMap.put("医疗健康行业", 0);
        industryMap.put("汽车行业", 0);
        industryMap.put("电子产品行业", 0);
        industryMap.put("服装服饰行业", 0);
        industryMap.put("旅游出行行业", 0);
        industryMap.put("娱乐文化行业", 0);

        // 初始化常见媒体数据
        mediumMap.put("户外广告", 0);
        mediumMap.put("公共交通广告", 0);
        mediumMap.put("电子屏广告", 0);
        mediumMap.put("电梯广告", 0);

        for (Advertisement advertisement : list) {
            // 处理行业字段，支持 "xx行业,xx行业" 或 "xx行业" 格式
            String industry = advertisement.getAdIndustryType();
            if (industry != null && !industry.isEmpty()) {
                String[] industryArray = industry.split(",");
                for (String ind : industryArray) {
                    String trimmedInd = ind.trim();
                    if (!trimmedInd.isEmpty()) {
                        industryMap.put(trimmedInd, industryMap.getOrDefault(trimmedInd, 0) + 1);
                    }
                }
            }

            // 同样处理媒体类型
            String medium = advertisement.getAdMediumType();
            if (medium != null && !medium.isEmpty()) {
                String[] mediumArray = medium.split(",");
                for (String med : mediumArray) {
                    String trimmedMed = med.trim();
                    if (!trimmedMed.isEmpty()) {
                        mediumMap.put(trimmedMed, mediumMap.getOrDefault(trimmedMed, 0) + 1);
                    }
                }
            }
        }

        // 按照出现频率排序，频率高的在前
        List<String> sortedIndustrys = industryMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<String> sortedMediums = mediumMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 转换为数组
        String[] industrys = sortedIndustrys.toArray(new String[0]);
        String[] mediums = sortedMediums.toArray(new String[0]);

        // 存入Redis缓存
        redisCache.setCacheObject(CacheConstants.INDUSTRY_KEY + "*", industrys);
        redisCache.setCacheObject(CacheConstants.MEDIUM_KEY + "*", mediums);
    }

    /**
     * 添加行业到redis中
     * @param industry
     * @return
     */
    @Override
    public String insertIndustry(String industry) {
        try {
            // 参数校验
            if (industry == null || industry.trim().isEmpty()) {
                return "";
            }
            // 去除前后空格
            industry = industry.trim();
            // 先检查行业是否已存在
            String existingIndustry = selectIndustryByIndustry(industry);
            if (!existingIndustry.isEmpty()) {
                // 如果已存在，直接返回成功
                return "";
            }
            // 从Redis中获取当前行业列表
            Object cacheObject = redisCache.getCacheObject(CacheConstants.INDUSTRY_KEY + "*");
            List<String> industryList = new ArrayList<>();
            // 处理不同类型的数据
            if (cacheObject instanceof com.alibaba.fastjson2.JSONArray) {
                com.alibaba.fastjson2.JSONArray jsonArray = (com.alibaba.fastjson2.JSONArray) cacheObject;
                industryList = jsonArray.toJavaList(String.class);
            } else if (cacheObject instanceof String[]) {
                industryList = Arrays.asList((String[]) cacheObject);
            }
            // 添加新行业到列表
            industryList.add(industry);
            // 转换为数组并存入Redis
            String[] newIndustrys = industryList.toArray(new String[0]);
            redisCache.setCacheObject(CacheConstants.INDUSTRY_KEY + "*", newIndustrys);
            return industry;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 从redis中查询行业列表
     * @return
     */
    @Override
    public List<String> selectIndustry() {
        try {
            // 从Redis中获取行业数组
            Object cacheObject = redisCache.getCacheObject(CacheConstants.INDUSTRY_KEY + "*");
            // 如果Redis中没有数据，返回空列表
            if (cacheObject == null) {
                return new ArrayList<>();
            }
            // 使用FastJSON的to方法进行转换
            if (cacheObject instanceof com.alibaba.fastjson2.JSONArray) {
                com.alibaba.fastjson2.JSONArray jsonArray = (com.alibaba.fastjson2.JSONArray) cacheObject;
                return jsonArray.toJavaList(String.class);
            }
            // 如果缓存对象已经是String数组
            else if (cacheObject instanceof String[]) {
                return Arrays.asList((String[]) cacheObject);
            }
            // 其他情况返回空列表
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 根据名称从redis中查询行业列表
     * @param industry
     * @return
     */
    @Override
    public String selectIndustryByIndustry(String industry) {
        try {
            // 从Redis中获取行业数组
            Object cacheObject = redisCache.getCacheObject(CacheConstants.INDUSTRY_KEY + "*");
            // 如果Redis中存在数据，则遍历查找
            if (cacheObject != null) {
                List<String> industryList = new ArrayList<>();
                // 如果缓存对象是JSONArray类型
                if (cacheObject instanceof com.alibaba.fastjson2.JSONArray) {
                    com.alibaba.fastjson2.JSONArray jsonArray = (com.alibaba.fastjson2.JSONArray) cacheObject;
                    industryList = jsonArray.toJavaList(String.class);
                }
                // 如果缓存对象是String数组
                else if (cacheObject instanceof String[]) {
                    industryList = Arrays.asList((String[]) cacheObject);
                }
                // 其他情况返回空字符串
                else {
                    return "";
                }
                // 遍历查找
                for (String ind : industryList) {
                    if (ind.equals(industry)) {
                        return industry;
                    }
                }
            }
            // 如果未找到，返回空字符串
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 从redis中删除行业
     * @param industry
     * @return
     */
    @Override
    public int deleteIndustry(String industry) {
        try {
            // 参数校验
            if (industry == null || industry.trim().isEmpty()) {
                return 0;
            }
            // 去除前后空格并保存到新变量中，确保在lambda表达式中使用的是final或effectively final变量
            final String trimmedIndustry = industry.trim();
            // 从Redis中获取当前行业列表
            Object cacheObject = redisCache.getCacheObject(CacheConstants.INDUSTRY_KEY + "*");
            // 如果Redis中没有数据，返回失败
            if (cacheObject == null) {
                return 0;
            }
            List<String> industryList = new ArrayList<>();
            // 处理不同类型的数据
            if (cacheObject instanceof com.alibaba.fastjson2.JSONArray) {
                com.alibaba.fastjson2.JSONArray jsonArray = (com.alibaba.fastjson2.JSONArray) cacheObject;
                industryList = jsonArray.toJavaList(String.class);
            } else if (cacheObject instanceof String[]) {
                industryList = Arrays.asList((String[]) cacheObject);
            } else {
                return 0;
            }
            // 如果列表为空，返回失败
            if (industryList.isEmpty()) {
                return 0;
            }
            // 查找并删除指定行业
            boolean removed = industryList.removeIf(ind -> ind.equals(trimmedIndustry));
            // 如果未找到要删除的行业，返回失败
            if (!removed) {
                return 0;
            }
            // 转换为数组并存入Redis
            String[] newIndustrys = industryList.toArray(new String[0]);
            redisCache.setCacheObject(CacheConstants.INDUSTRY_KEY + "*", newIndustrys);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 添加媒体类型到redis中
     * @param medium
     * @return
     */
    @Override
    public String insertMedium(String medium) {
        try {
            // 参数校验
            if (medium == null || medium.trim().isEmpty()) {
                return "";
            }
            // 去除前后空格
            medium = medium.trim();
            // 先检查行业是否已存在
            String existingMedium = selectMediumByMedium(medium);
            if (!existingMedium.isEmpty()) {
                // 如果已存在，直接返回成功
                return "";
            }
            // 从Redis中获取当前行业列表
            Object cacheObject = redisCache.getCacheObject(CacheConstants.MEDIUM_KEY + "*");
            List<String> mediumList = new ArrayList<>();
            // 处理不同类型的数据
            if (cacheObject instanceof com.alibaba.fastjson2.JSONArray) {
                com.alibaba.fastjson2.JSONArray jsonArray = (com.alibaba.fastjson2.JSONArray) cacheObject;
                mediumList = jsonArray.toJavaList(String.class);
            } else if (cacheObject instanceof String[]) {
                mediumList = Arrays.asList((String[]) cacheObject);
            }
            // 添加新行业到列表
            mediumList.add(medium);
            // 转换为数组并存入Redis
            String[] newMediums = mediumList.toArray(new String[0]);
            redisCache.setCacheObject(CacheConstants.MEDIUM_KEY + "*", newMediums);
            return medium;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 从redis中查询媒体类型列表
     * @return
     */
    @Override
    public List<String> selectMedium() {
        try {
            // 从Redis中获取行业数组
            Object cacheObject = redisCache.getCacheObject(CacheConstants.MEDIUM_KEY + "*");
            // 如果Redis中没有数据，返回空列表
            if (cacheObject == null) {
                return new ArrayList<>();
            }
            // 使用FastJSON的to方法进行转换
            if (cacheObject instanceof com.alibaba.fastjson2.JSONArray) {
                com.alibaba.fastjson2.JSONArray jsonArray = (com.alibaba.fastjson2.JSONArray) cacheObject;
                return jsonArray.toJavaList(String.class);
            }
            // 如果缓存对象已经是String数组
            else if (cacheObject instanceof String[]) {
                return Arrays.asList((String[]) cacheObject);
            }
            // 其他情况返回空列表
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 根据名称从redis中查询媒体类型
     * @param medium
     * @return
     */
    @Override
    public String selectMediumByMedium(String medium) {
        try {
            // 从Redis中获取行业数组
            Object cacheObject = redisCache.getCacheObject(CacheConstants.MEDIUM_KEY + "*");
            // 如果Redis中存在数据，则遍历查找
            if (cacheObject != null) {
                List<String> mediumList = new ArrayList<>();
                // 如果缓存对象是JSONArray类型
                if (cacheObject instanceof com.alibaba.fastjson2.JSONArray) {
                    com.alibaba.fastjson2.JSONArray jsonArray = (com.alibaba.fastjson2.JSONArray) cacheObject;
                    mediumList = jsonArray.toJavaList(String.class);
                }
                // 如果缓存对象是String数组
                else if (cacheObject instanceof String[]) {
                    mediumList = Arrays.asList((String[]) cacheObject);
                }
                // 其他情况返回空字符串
                else {
                    return "";
                }
                // 遍历查找
                for (String med : mediumList) {
                    if (med.equals(medium)) {
                        return medium;
                    }
                }
            }
            // 如果未找到，返回空字符串
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 从redis中删除媒体类型
     * @param medium
     * @return
     */
    @Override
    public int deleteMedium(String medium) {
        try {
            // 参数校验
            if (medium == null || medium.trim().isEmpty()) {
                return 0;
            }
            // 去除前后空格并保存到新变量中，确保在lambda表达式中使用的是final或effectively final变量
            final String trimmedMedium = medium.trim();
            // 从Redis中获取当前媒体类型列表
            Object cacheObject = redisCache.getCacheObject(CacheConstants.MEDIUM_KEY + "*");
            // 如果Redis中没有数据，返回失败
            if (cacheObject == null) {
                return 0;
            }
            List<String> mediumList = new ArrayList<>();
            // 处理不同类型的数据
            if (cacheObject instanceof com.alibaba.fastjson2.JSONArray) {
                com.alibaba.fastjson2.JSONArray jsonArray = (com.alibaba.fastjson2.JSONArray) cacheObject;
                mediumList = jsonArray.toJavaList(String.class);
            } else if (cacheObject instanceof String[]) {
                mediumList = Arrays.asList((String[]) cacheObject);
            } else {
                return 0;
            }
            // 如果列表为空，返回失败
            if (mediumList.isEmpty()) {
                return 0;
            }
            // 查找并删除指定行业
            boolean removed = mediumList.removeIf(ind -> ind.equals(trimmedMedium));
            // 如果未找到要删除的行业，返回失败
            if (!removed) {
                return 0;
            }
            // 转换为数组并存入Redis
            String[] newMediums = mediumList.toArray(new String[0]);
            redisCache.setCacheObject(CacheConstants.MEDIUM_KEY + "*", newMediums);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Map<String, Integer> selectCount() {
        Map<String, Integer> map = new HashMap<>();
        //初审代办事项
        map.put("checkCount", advertisementMapper.selectCheckCount());
        int count = advertisementMapper.selectApproveCount("0");
        int count5 = advertisementMapper.selectApproveCount("5");
        map.put("approve1Count", count + count5);
        map.put("addToday1Count", advertisementMapper.selectAddToday(1));
        map.put("longTermUntreated1Count", advertisementMapper.selectLongTermUntreated(1));
        //复审代办事项
        map.put("addToday2Count", advertisementMapper.selectAddToday(2));
        int count1 = advertisementMapper.selectApproveCount("1");
        int count2 = advertisementMapper.selectApproveCount("2");
        int count3 = advertisementMapper.selectApproveCount("3");
        map.put("approve2Count", count1 + count2 + count3);
        map.put("suspiciousCount", count2);
        map.put("longTermUntreated2Count", advertisementMapper.selectLongTermUntreated(2));
        //处理代办事项
        map.put("addToday3Count", enforcementMapper.selectAddTodayByHandle());
        int count6 = enforcementMapper.selectHandleCount();
        map.put("approve3Count", count6);
        map.put("longTermUntreated3Count", enforcementMapper.selectLongTermUntreatedByHandle());
        return map;
    }

    /**
     *  自动复审
     */
    @Override
    @Transactional
    public void automatedReview() {
        //System.out.println("执行业务层方法："+advertisementMapper);
        //查询初审合法、初审违法的数据（根据状态查询）
        Advertisement advertisement = new Advertisement();
        advertisement.setAuditStatuses(Arrays.asList("1","3"));
        List<Advertisement> advertisements = advertisementMapper.selectAdvertisementList(advertisement);
        //遍历
        advertisements.forEach(ad->{
            //创建审核记录对象
            AuditRecords auditRecords = new AuditRecords();
            //设置广告id
            auditRecords.setAdId(ad.getId());
            //设置广告状态
            auditRecords.setAuditStatus(ad.getAuditStatus());
            //根据广告id和状态查询审批记录
            List<AuditRecords> recordsList = auditRecordsMapper.selectAuditRecordsList(auditRecords);
            //获取审批时间
            Date date = recordsList.get(0).getAuditTime();
            //判断是否已经超过2天48小时：2 * 24 * 60 * 60 * 1000
            if ((System.currentTimeMillis() - date.getTime()) >  2 * 24 * 60 * 60 * 1000 ) {
                //自动进行复审
                if (ad.getAuditStatus().equals("1")){
                    //自动复审合法
                    ad.setAuditStatus("7");
                    recordsList.get(0).setAuditStatus("7");
                }else if (ad.getAuditStatus().equals("3")){
                    //自动复审违法
                    ad.setAuditStatus("8");
                    recordsList.get(0).setAuditStatus("8");

                    //审批状态为6(复审违法)或8（自动复审违法）时候，需要新增处理记录
                    insertEnforcementByReview(ad);
                }
                //更新广告
                advertisementMapper.updateAdvertisement(ad);
                //新增审批记录
                recordsList.get(0).setAuditTime(new Date());
                recordsList.get(0).setCreateTime(new Date());
                recordsList.get(0).setCreateBy("1");
                auditRecordsMapper.insertAuditRecords(recordsList.get(0));
            }
        });
    }

    /**
     * 统计查询采集者数据
     */
    @Override
    public  Map<String, Integer> getCollector(){

        return advertisementMapper.getCollector(SecurityUtils.getUserId());
    }

}
