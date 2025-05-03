package com.net.sparrow.util;

import com.net.sparrow.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件工具
 */
@Slf4j
public abstract class FileUtil {

    /**
     * 将 File 转换成 MultipartFile
     *
     * @param fieldName 文件名称
     * @param file      文件
     * @return MultipartFile
     * @throws Exception
     */
    public static MultipartFile toMultipartFile(String fieldName, File file) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        String contentType = new MimetypesFileTypeMap().getContentType(file);
        FileItem fileItem = diskFileItemFactory.createItem(fieldName, contentType, false, file.getName());
        try (
                InputStream inputStream = new FileInputStream(file);
                OutputStream outputStream = fileItem.getOutputStream()
        ) {
            FileCopyUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.error("File转换MultipartFile失败，原因：", e);
            throw new BusinessException("File转换MultipartFile失败");
        }
        return new CommonsMultipartFile(fileItem);
    }
}