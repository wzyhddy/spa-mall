package com.net.sparrow.util;

import com.google.gson.Gson;
import com.net.sparrow.config.QiNiuConfig;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;


/**
 * 七牛云上传工具
 */
@Component
public class QiNiuUtil {
    public static final String IMAGE = "image";
    public static final String FILE = "file";

    @Autowired
    private QiNiuConfig qiNiuConfig;

    /**
     * 将图片上传到七牛云
     */
    public String upload(InputStream file, String fileType, String fileContextType) throws Exception {
        Configuration cfg = new Configuration(Region.huadong());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        String upToken = null;
        String path = null;
        if (fileType.equals(IMAGE)) {
            upToken = auth.uploadToken(qiNiuConfig.getBucketPictureName());
            path = qiNiuConfig.getDomainFile();
        } else if (fileType.equals(FILE)) {
            upToken = auth.uploadToken(qiNiuConfig.getBucketFileName());
            path = qiNiuConfig.getDomainFile();
        }
        Response response = uploadManager.put(file, null, upToken, null, fileContextType);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return path + putRet.key;
    }
}