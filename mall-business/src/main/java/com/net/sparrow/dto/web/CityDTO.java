package com.net.sparrow.dto.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 城市DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CityDTO {

    /**
     * ip
     */
    private String ip;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 所在城市
     */
    private String city;
}
