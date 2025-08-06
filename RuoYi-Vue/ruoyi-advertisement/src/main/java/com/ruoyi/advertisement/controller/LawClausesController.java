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
import com.ruoyi.advertisement.domain.LawClauses;
import com.ruoyi.advertisement.service.ILawClausesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 法律法规管理Controller
 * 
 * @author ruoyi
 * @date 2025-07-22
 */
@RestController
@RequestMapping("/advertisement/clauses")
public class LawClausesController extends BaseController
{
    @Autowired
    private ILawClausesService lawClausesService;

    /**
     * 查询法律法规管理列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:list')")
    @GetMapping("/list")
    public TableDataInfo list(LawClauses lawClauses)
    {
        startPage();
        List<LawClauses> list = lawClausesService.selectLawClausesList(lawClauses);
        return getDataTable(list);
    }

    @GetMapping("/listAll")
    public AjaxResult listAll(LawClauses lawClauses)
    {
        List<LawClauses> list = lawClausesService.selectLawClausesList(lawClauses);
        return success(list);
    }

    /**
     * 导出法律法规管理列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:export')")
    @Log(title = "法律法规管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LawClauses lawClauses)
    {
        List<LawClauses> list = lawClausesService.selectLawClausesList(lawClauses);
        ExcelUtil<LawClauses> util = new ExcelUtil<LawClauses>(LawClauses.class);
        util.exportExcel(response, list, "法律法规管理数据");
    }

    /**
     * 获取法律法规管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lawClausesService.selectLawClausesById(id));
    }

    /**
     * 新增法律法规管理
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:add')")
    @Log(title = "法律法规管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LawClauses lawClauses)
    {
        return toAjax(lawClausesService.insertLawClauses(lawClauses));
    }

    /**
     * 修改法律法规管理
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:edit')")
    @Log(title = "法律法规管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LawClauses lawClauses)
    {
        return toAjax(lawClausesService.updateLawClauses(lawClauses));
    }

    /**
     * 删除法律法规管理
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:remove')")
    @Log(title = "法律法规管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lawClausesService.deleteLawClausesByIds(ids));
    }
}
