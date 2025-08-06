package com.ruoyi.advertisement.controller;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.domain.Enforcement;
import com.ruoyi.advertisement.service.IAdvertisementService;
import com.ruoyi.advertisement.service.IEnforcementService;
import com.ruoyi.advertisement.service.PdfService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class PdfUtils {
    @Autowired
    private  PdfService pdfService;
    @Autowired
    private IAdvertisementService advertisementService;
    @Autowired
    private IEnforcementService enforcementService;

    /**
     * 测试环境使用放开
     * @param response
     * @param id
     * @throws IOException
     */
    public  void generatePdf(HttpServletResponse response, Long id) throws IOException {
        // 查询广告数据
        Advertisement advertisement = advertisementService.selectAdvertisementById(id);
        //  查询执行数据
        Enforcement enforcement = enforcementService.selectEnforcementByAdId(id);
        // 将广告数据转换为Map
        Map<String, Object> data = new HashMap<>();
        if (advertisement != null) {
            // 执行数据
            data.put("adCode", enforcement.getAdCode());
            data.put("handler", enforcement.getHandler());
            data.put("handleTime", enforcement.getHandleTime());
            data.put("handleResult", enforcement.getHandleResult());
            data.put("postHandleImage", enforcement.getPostHandleImage());
            // 广告数据
            data.put("adProfitabilityType", advertisement.getAdProfitabilityType());
            data.put("adIndustryType", advertisement.getAdIndustryType());
            data.put("adMediumType", advertisement.getAdMediumType());
            data.put("adDescription", advertisement.getAdDescription());
            data.put("adImages", advertisement.getAdImages());
            data.put("violationType", advertisement.getViolationType());
            data.put("province", advertisement.getProvince());
            data.put("city", advertisement.getCity());
            data.put("district", advertisement.getDistrict());
            data.put("street", advertisement.getStreet());
            data.put("address", advertisement.getAddress());
            data.put("advertiser", advertisement.getAdvertiser());
            data.put("surveyTime", advertisement.getSurveyTime());
            data.put("surveyor", advertisement.getSurveyor());
            data.put("remark", advertisement.getRemark());
        }

        byte[] pdfBytes = pdfService.generatePdf(data);

        response.setContentType("application/pdf");
        response.setContentLength(pdfBytes.length);
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");

        response.getOutputStream().write(pdfBytes);
    }

    /**
     * 生成PDF并保存
     * @return
     */
    public Map<String, Object> generateAndSavePdf(Long id) {
        try {
            // 查询广告数据
            Advertisement advertisement = advertisementService.selectAdvertisementById(id);
            //  查询执行数据
            Enforcement enforcement = enforcementService.selectEnforcementByAdId(id);
            // 将广告数据转换为Map
            Map<String, Object> data = new HashMap<>();
            if (advertisement != null) {
                // 执行数据
                data.put("adCode", enforcement.getAdCode());
                data.put("handler", enforcement.getHandler());
                data.put("handleTime", enforcement.getHandleTime());
                data.put("handleResult", enforcement.getHandleResult());
                data.put("postHandleImage", enforcement.getPostHandleImage());
                // 广告数据
                data.put("adProfitabilityType", advertisement.getAdProfitabilityType());
                data.put("adIndustryType", advertisement.getAdIndustryType());
                data.put("adMediumType", advertisement.getAdMediumType());
                data.put("adDescription", advertisement.getAdDescription());
                data.put("adImages", advertisement.getAdImages());
                data.put("violationType", advertisement.getViolationType());
                data.put("province", advertisement.getProvince());
                data.put("city", advertisement.getCity());
                data.put("district", advertisement.getDistrict());
                data.put("street", advertisement.getStreet());
                data.put("address", advertisement.getAddress());
                data.put("advertiser", advertisement.getAdvertiser());
                data.put("surveyTime", advertisement.getSurveyTime());
                data.put("surveyor", advertisement.getSurveyor());
                data.put("remark", advertisement.getRemark());
            }
            String originalFilename = "advertisement_" + System.currentTimeMillis() + ".pdf";
            Map<String, Object> result = pdfService.generateAndSavePdf(data, originalFilename);
            return result;
        } catch (Exception e) {
            return AjaxResult.error("生成PDF失败: " + e.getMessage());
        }
    }

}
