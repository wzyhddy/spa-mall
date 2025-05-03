package com.net.sparrow.service.upload;

import com.net.sparrow.dto.FileDTO;
import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.util.QiNiuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.net.sparrow.util.QiNiuUtil.FILE;
import static com.net.sparrow.util.QiNiuUtil.IMAGE;

/**
 * 文件上传service
 */
@Service
@Slf4j
public class UploadService {

    @Autowired
    private QiNiuUtil qiNiuUtil;

    /**
     * 上传文件
     *
     * @param file     文件
     * @param fileType 文件类型
     * @return 文件实体
     * @throws Exception
     */
    public FileDTO upload(MultipartFile file, String fileType,String fileContextType) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            log.error("传入的文件名不能为空");
            throw new BusinessException("传入的文件名不能为空");
        }
        if (!this.validateFileName(fileName)) {
            log.error("文件名应仅包含汉字、字母、数字、下划线和点号");
            throw new BusinessException("文件名应仅包含汉字、字母、数字、下划线和点号");
        }
        InputStream fileInputStream = file.getInputStream();
        String url = "";
        if (fileType.equals(IMAGE)) {
            url = qiNiuUtil.upload(fileInputStream, IMAGE, fileContextType);
        } else if (fileType.equals(FILE)) {
            url = qiNiuUtil.upload(fileInputStream, FILE, fileContextType);
        }
        FileDTO fileVO = new FileDTO();
        fileVO.setFileName(fileName);
        fileVO.setDownloadUrl(url);
        return fileVO;
    }

    /**
     * 验证文件名称：仅包含 汉字、字母、数字、下划线和点号
     *
     * @param fileName 文件名称
     * @return 返回true表示符合要求
     */
    private boolean validateFileName(String fileName) {
        String regex = "^[a-zA-Z0-9_\\u4e00-\\u9fa5_\\.]+$";
        return fileName.matches(regex);
    }
}
