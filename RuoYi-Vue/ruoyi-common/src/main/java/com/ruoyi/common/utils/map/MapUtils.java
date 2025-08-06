package com.ruoyi.common.utils.map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>ClassName : MapUtils</p>
 * <p>Description : 地图工具类</p>
 *
 * @author 孙伟光
 * @version 1.0
 * @date 2025/7/29 9:27
 */
@Component
public class MapUtils {

    @Value("${map.key}")
    private String MAP_API_KEY;

    public AddressComponent findAddressByLngLat(String lng, String lat) {
        String apiURL = "https://restapi.amap.com/v3/geocode/regeo";
        String parameters = "key=" + MAP_API_KEY + "&location=" + lng + "," + lat;

        try {
            // 发送HTTP请求获取地址信息
            String response = HttpUtils.sendGet(apiURL, parameters);

            // 解析返回的JSON数据
            JSONObject jsonObject = JSON.parseObject(response);
            //把jsonObject对象中的数据赋给AddressComponent对象
            if (jsonObject != null && "1".equals(jsonObject.getString("status"))) {
                // 获取regeocode对象
                JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                if (regeocode != null) {
                    JSONObject address = regeocode.getJSONObject("addressComponent");
                    if (address != null){
                        // 返回格式化地址
                        AddressComponent addressComponent = new AddressComponent();
                        addressComponent.setCountry(address.getString("country"));
                        addressComponent.setProvince(address.getString("province"));
                        if ("[]".equals(address.getString("city"))){
                            addressComponent.setCity("市辖区");
                        } else {
                            addressComponent.setCity(address.getString("city"));
                        }
                        addressComponent.setCitycode(address.getString("citycode"));
                        addressComponent.setDistrict(address.getString("district"));
                        addressComponent.setAdcode(address.getString("adcode"));
                        addressComponent.setTownship(address.getString("township"));
                        addressComponent.setTowncode(address.getString("towncode"));
                        return addressComponent;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            // 处理异常情况
            e.printStackTrace();
            return null;
        }
    }
}
