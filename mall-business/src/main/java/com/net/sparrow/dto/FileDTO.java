package com.net.sparrow.dto;

import lombok.Data;

/**
 * 文件对象
 */
@Data
public class FileDTO {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 下载地址
     */
    private String downloadUrl;
}
