package com.ruoyi.common.utils.map;

import org.springframework.context.annotation.Bean;

/**
 * <p>ClassName : AddressComponent</p>
 * <p>Description : </p>
 *
 * @author 孙伟光
 * @version 1.0
 * @date 2025/7/29 9:49
 */
public class AddressComponent {
    private String country;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    private String citycode;
    /**
     * 区
     */
    private String district;
    private String adcode;
    /**
     * 街道
     */
    private String township;
    private String towncode;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public String getTowncode() {
        return towncode;
    }

    public void setTowncode(String towncode) {
        this.towncode = towncode;
    }

    @Override
    public String toString() {
        return "AddressComponent{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", citycode='" + citycode + '\'' +
                ", district='" + district + '\'' +
                ", adcode='" + adcode + '\'' +
                ", township='" + township + '\'' +
                ", towncode='" + towncode + '\'' +
                '}';
    }
}
