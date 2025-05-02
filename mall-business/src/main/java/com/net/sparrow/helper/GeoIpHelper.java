package com.net.sparrow.helper;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.net.sparrow.dto.web.CityDTO;
import com.net.sparrow.entity.third.TaoboCityEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.InetAddress;
import java.util.Objects;

/**
 * GeoIp 工具类
 */
@Slf4j
@Component
public class GeoIpHelper {

    private static final String GEO_IP_FILE_NAME = "GeoLite2-City.mmdb";
    private static final String NOT_FOUND_CITY_NAME = "未知";

    @Value("${mall.mgt.geoIpFilePath:}")
    private String geoIpFilePath;
    @Value("${mall.mgt.taobaoIpUrl:}")
    private String taobaoIpUrl;
    @Value("${mall.mgt.taobaoIpRequestOff:false}")
    private Boolean taobaoIpRequestOff;
    @Autowired
    private HttpHelper httpHelper;


    /**
     * 根据ip获取所在城市
     *
     * @param ip ip地址
     * @return 城市
     */
    public CityDTO getCity(String ip) {
        CityDTO cityFromGeoIp = getCityFromGeoIp(ip);
        if (Objects.nonNull(cityFromGeoIp) && Objects.nonNull(cityFromGeoIp.getCity())) {
            return cityFromGeoIp;
        }
        if (taobaoIpRequestOff) {
            return getNotFoundCity();
        }
        CityDTO cityFromApi = getCityFromApi(ip);
        if (Objects.nonNull(cityFromApi)) {
            return cityFromApi;
        }
        return getNotFoundCity();
    }


    private CityDTO getNotFoundCity() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCity(NOT_FOUND_CITY_NAME);
        return cityDTO;
    }

    private CityDTO getCityFromGeoIp(String ip) {
        String fileUrl = getFileUrl();
        File file = new File(fileUrl);
        if (!file.exists()) {
            log.warn(String.format("%s文件不存在", fileUrl));
            return null;
        }

        try {
            DatabaseReader reader = new DatabaseReader.Builder(file).build();
            //解析IP地址
            InetAddress ipAddress = InetAddress.getByName(ip);
            // 获取查询结果
            CityResponse response = reader.city(ipAddress);
            if (response == null) {
                return null;
            }

            // 国家
            String country = response.getCountry().getNames().get("zh-CN");
            // 省份
            String province = response.getMostSpecificSubdivision().getNames().get("zh-CN");
            //城市
            String city = response.getCity().getNames().get("zh-CN");
            return new CityDTO(ip, country, province, city);
        } catch (Exception e) {
            log.error("从GeoIp库中获取城市失败，原因：", e);
        }
        return null;
    }

    private CityDTO getCityFromApi(String ip) {
        String url = String.format(taobaoIpUrl, ip);
        TaoboCityEntity taoboCityEntity = httpHelper.doGet(url, TaoboCityEntity.class);
        if (Objects.nonNull(taoboCityEntity)) {
            TaoboCityEntity.TaoboAreaEntity data = taoboCityEntity.getData();
            if (Objects.nonNull(data)) {
                return new CityDTO(ip, data.getCountry(), data.getRegion(), data.getCity());
            }
        }
        return null;
    }


    private String getFileUrl() {
        return geoIpFilePath + "/" + GEO_IP_FILE_NAME;
    }
}
