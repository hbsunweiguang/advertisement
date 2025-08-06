package com.ruoyi.advertisement.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.advertisement.domain.AdvertisementVo;
import com.ruoyi.advertisement.domain.Regional;
import com.ruoyi.advertisement.service.IRegionalService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.service.IAdvertisementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 广告列表Controller
 * 
 * @author ruoyi
 * @date 2025-07-21
 */
@RestController
@RequestMapping("/advertisement/advertisement")
public class AdvertisementController extends BaseController
{
    @Autowired
    private IAdvertisementService advertisementService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IRegionalService regionalService;

    /**
     * 查询广告列表列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:list')")
    @GetMapping("/list")
    public TableDataInfo list(Advertisement advertisement)
    {
        System.out.println("状态值:"+advertisement.getAuditStatuses());
        startPage();
        List<Advertisement> list = advertisementService.selectAdvertisementList(advertisement);
        return getDataTable(list);
    }

    /**
     * 导出广告列表列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:export')")
    @Log(title = "广告列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Advertisement advertisement)
    {
        List<Advertisement> list = advertisementService.selectAdvertisementList(advertisement);
        ExcelUtil<Advertisement> util = new ExcelUtil<Advertisement>(Advertisement.class);
        util.exportExcel(response, list, "广告列表数据");
    }

    /**
     * 获取广告列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(advertisementService.selectAdvertisementById(id));
    }

    /**
     * 新增广告列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:add')")
    @Log(title = "广告列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Advertisement advertisement)
    {
        return toAjax(advertisementService.insertAdvertisement(advertisement));
    }

    /**
     * 修改广告列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:edit')")
    @Log(title = "广告列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Advertisement advertisement)
    {
        return toAjax(advertisementService.updateAdvertisement(advertisement));
    }

    /**
     * 删除广告列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:remove')")
    @Log(title = "广告列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(advertisementService.deleteAdvertisementByIds(ids));
    }

    /**
     * 获取采集用户列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:list')")
    @GetMapping("/userList")
    public TableDataInfo list(SysUser user)
    {
        //startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 查询某一级的行政区域管理列表：根据parent父编码查询行政区域
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:list')")
    @GetMapping("/all")
    public TableDataInfo all(Regional regional)
    {
        //startPage();
        List<Regional> list = regionalService.selectRegionalList(regional);
        return getDataTable(list);
    }

    /**
     * 审批广告列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:add')")
    @Log(title = "审批广告", businessType = BusinessType.INSERT)
    @PostMapping("approve")
    public AjaxResult approve(@RequestBody AdvertisementVo vo)
    {
        return toAjax(advertisementService.approveAdvertisement(vo));
    }

    /**
     * 新增行业类型到redis中
     */
    @Log(title = "新增行业类型到redis中", businessType = BusinessType.INSERT)
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
     * 从redis中删除行业类型
     */
    @Log(title = "从redis中删除行业类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/industry")
    public AjaxResult removeIndustry(String industry)
    {
        return toAjax(advertisementService.deleteIndustry(industry));
    }

    /**
     * 新增媒体类型到redis中
     */
    @Log(title = "新增行业类型到redis中", businessType = BusinessType.INSERT)
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
     * 从redis中删除行业类型
     */
    @Log(title = "从redis中删除媒体类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/medium")
    public AjaxResult removeMedium(String medium)
    {
        return toAjax(advertisementService.deleteMedium(medium));
    }

    @GetMapping("/count")
    public AjaxResult count() {
        return success(advertisementService.selectCount());
    }
}
