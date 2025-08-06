package com.ruoyi.advertisement.api;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.domain.AdvertisementBody;
import com.ruoyi.advertisement.service.IAdvertisementService;
import com.ruoyi.advertisement.service.IStatisticsService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>ClassName : APIStatisticsController</p>
 * <p>Description : </p>
 *
 * @author 孙伟光
 * @version 1.0
 * @date 2025/8/4 9:52
 */
@RestController
@RequestMapping("/api/statistics")
public class APIStatisticsController  extends BaseController{

    @Autowired
    private IStatisticsService statisticsService;

    @GetMapping("/list")
    public Map<String, Object> list()
    {
        Map<String, Object> map = statisticsService.list();
        return success(map);
    }
}