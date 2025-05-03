package com.net.sparrow.controller.upload;

import com.net.sparrow.annotation.NoLogin;
import com.net.sparrow.dto.FileDTO;
import com.net.sparrow.service.upload.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.net.sparrow.util.QiNiuUtil.FILE;
import static com.net.sparrow.util.QiNiuUtil.IMAGE;

/**
 * 上传文件
 */
@Api(tags = "上传文件", description = "上传文件")
@RestController
@RequestMapping("/v1")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @NoLogin
    @ApiOperation(value = "上传文件接口")
    @PostMapping(value = "/file/upload")
    public FileDTO fileUpload(MultipartFile file) throws Exception {
        return uploadService.upload(file, FILE, null);
    }

    @NoLogin
    @ApiOperation(value = "上传图片接口")
    @PostMapping(value = "/image/upload")
    public FileDTO imageUpload(MultipartFile file) throws Exception {
        return uploadService.upload(file, IMAGE,null);
    }
}
