package com.ruoyi.advertisement.api;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.domain.AdvertisementBody;
import com.ruoyi.advertisement.service.IAdvertisementService;
import com.ruoyi.advertisement.service.IEnforcementService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>ClassName : APIAdvertisementController</p>
 * <p>Description : </p>
 *
 * @author 孙伟光
 * @version 1.0
 * @date 2025/7/28 9:30
 */
@RestController
@RequestMapping("/api/advertisement")
public class APIAdvertisementController  extends BaseController
{
    @Autowired
    private IAdvertisementService advertisementService;

    @Autowired
    private IEnforcementService enforcementService;

    @GetMapping("/list")
    public TableDataInfo list(AdvertisementBody advertisementBody)
    {
        startPage();
        List<Advertisement> list = advertisementService.selectAdvertisementList(advertisementBody);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult list(@PathVariable("id") Long id)
    {
        Advertisement advertisement = advertisementService.selectAdvertisementAndAuditAndEnforcementById(id);
        return success(advertisement);
    }

    @PostMapping("/advertisement")
    public AjaxResult addAdvertisement(@RequestBody Advertisement advertisement) {
        return toAjax(advertisementService.insertAdvertisementByCollect(advertisement));
    }

    /**
     * 新增行业类型到redis中
     */
    @PostMapping("/industry")
    public AjaxResult addIndustry(String industry)
    {
        return success(advertisementService.insertIndustry(industry));
    }

    /**
     * 从redis中获取行业类型列表
     */
    @GetMapping("/industry/list")
    public AjaxResult industryList()
    {
        List<String> list = advertisementService.selectIndustry();
        return success(list);
    }

    /**
     * 检测行业列表中是否存在该行业
     */
    @GetMapping(value = "/industry/check")
    public AjaxResult checkIndustry(String industry)
    {
        return success(advertisementService.selectIndustryByIndustry(industry));
    }

    /**
     * 新增媒体类型到redis中
     */
    @PostMapping("/medium")
    public AjaxResult addMedium(String medium)
    {
        return success(advertisementService.insertMedium(medium));
    }

    /**
     * 从redis中获取媒体类型列表
     */
    @GetMapping("/medium/list")
    public AjaxResult mediumList()
    {
        List<String> list = advertisementService.selectMedium();
        return success(list);
    }

    /**
     * 检测媒体类型列表中是否存在该媒体类型
     */
    @GetMapping(value = "/medium/check")
    public AjaxResult checkMedium(String medium)
    {
        return success(advertisementService.selectMediumByMedium(medium));
    }

    /**
     * 统计查询采集者数据
     */
    @GetMapping("/getCollector")
    public AjaxResult getCollector()
    {
        return success(advertisementService.getCollector());
    }

    /**
     * 统计查询采集者数据
     */
    @GetMapping("/getHandler")
    public AjaxResult getHandler()
    {
        return success(enforcementService.getHandler());
    }
}
