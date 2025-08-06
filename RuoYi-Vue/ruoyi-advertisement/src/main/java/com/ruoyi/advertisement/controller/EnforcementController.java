package com.ruoyi.advertisement.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.advertisement.domain.Enforcement;
import com.ruoyi.advertisement.service.IEnforcementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 执行结果记录Controller
 * 
 * @author ruoyi
 * @date 2025-07-22
 */
@RestController
@RequestMapping("/advertisement/enforcement")
public class EnforcementController extends BaseController
{
    @Autowired
    private IEnforcementService enforcementService;

    /**
     * 查询执行结果记录列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:enforcement:list')")
    @GetMapping("/list")
    public TableDataInfo list(Enforcement enforcement)
    {
        startPage();
        List<Enforcement> list = enforcementService.selectEnforcementList(enforcement);
        return getDataTable(list);
    }

    /**
     * 导出执行结果记录列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:enforcement:export')")
    @Log(title = "执行结果记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Enforcement enforcement)
    {
        List<Enforcement> list = enforcementService.selectEnforcementList(enforcement);
        ExcelUtil<Enforcement> util = new ExcelUtil<Enforcement>(Enforcement.class);
        util.exportExcel(response, list, "执行结果记录数据");
    }

    /**
     * 获取执行结果记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('advertisement:enforcement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(enforcementService.selectEnforcementById(id));
    }

    /**
     * 新增执行结果记录
     */
    @PreAuthorize("@ss.hasPermi('advertisement:enforcement:add')")
    @Log(title = "执行结果记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Enforcement enforcement)
    {
        return toAjax(enforcementService.insertEnforcement(enforcement));
    }

    /**
     * 修改执行结果记录
     */
//    @PreAuthorize("@ss.hasPermi('advertisement:enforcement:edit')")
    @Log(title = "执行结果记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Enforcement enforcement)
    {
        return toAjax(enforcementService.updateEnforcement(enforcement));
    }

    /**
     * 删除执行结果记录
     */
    @PreAuthorize("@ss.hasPermi('advertisement:enforcement:remove')")
    @Log(title = "执行结果记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(enforcementService.deleteEnforcementByIds(ids));
    }

    /**
     * 查看pdf
     * @param adId
     * @return
     */
    @GetMapping("/selectReport/{adId}")
    public AjaxResult selectReport(@PathVariable Long adId)
    {
        return AjaxResult.success(enforcementService.selectReport(adId));
    }

    /**
     * wx端查询广告执行结果
     * @return
     */
    @GetMapping("/selectEnforcementAll")
    public TableDataInfo selectEnforcementAll(Enforcement enforcement)
        {
            startPage();
            List<Enforcement> enforcements = enforcementService.selectEnforcementAll(enforcement);
            return getDataTable(enforcements);
        }

}
