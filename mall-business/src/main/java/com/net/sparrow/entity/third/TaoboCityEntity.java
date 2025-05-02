package com.net.sparrow.entity.third;

import lombok.Data;

/**
 * 淘宝城市实体
 */
@Data
public class TaoboCityEntity {

    /**
     * 返回码
     */
    private int code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 地区
     */
    private TaoboAreaEntity data;


    @Data
    public static class TaoboAreaEntity {

        /**
         * 国家
         */
        private String country;

        /**
         * 省份
         */
        private String region;

        /**
         * 城市
         */
        private String city;

        /**
         * 查询ip
         */
        private String queryIp;
    }
}
