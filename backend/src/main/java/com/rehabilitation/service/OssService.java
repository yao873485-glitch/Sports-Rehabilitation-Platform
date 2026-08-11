package com.rehabilitation.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.rehabilitation.config.OssConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * OSS文件上传服务
 */
@Service
public class OssService {

    private static final Logger log = LoggerFactory.getLogger(OssService.class);

    @Autowired
    private OSS ossClient;

    @Autowired
    private OssConfig ossConfig;

    /**
     * 上传图片到OSS
     *
     * @param file 文件
     * @return 文件URL
     */
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只能上传图片文件");
        }

        // 验证文件大小 (限制为10MB)
        long maxSize = 10 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("图片大小不能超过10MB");
        }

        try {
            // 生成文件名: folder/YYYYMMDD/uuid.ext
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String dateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String fileName = ossConfig.getFolder() + dateFolder + "/" + UUID.randomUUID().toString() + extension;

            // 上传文件到OSS
            InputStream inputStream = file.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossConfig.getBucketName(),
                    fileName,
                    inputStream
            );

            ossClient.putObject(putObjectRequest);
            inputStream.close();

            // 返回文件URL
            String fileUrl = ossConfig.getBaseUrl() + "/" + fileName;
            log.info("文件上传成功: {}", fileUrl);

            return fileUrl;

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除OSS中的文件
     *
     * @param fileUrl 文件URL
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            // 从URL中提取文件key
            String baseUrl = ossConfig.getBaseUrl();
            if (fileUrl.startsWith(baseUrl)) {
                String fileKey = fileUrl.substring(baseUrl.length() + 1);
                ossClient.deleteObject(ossConfig.getBucketName(), fileKey);
                log.info("文件删除成功: {}", fileUrl);
            }
        } catch (Exception e) {
            log.error("文件删除失败: {}", fileUrl, e);
        }
    }

    /**
     * 验证文件是否存在
     *
     * @param fileUrl 文件URL
     * @return 是否存在
     */
    public boolean fileExists(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            String baseUrl = ossConfig.getBaseUrl();
            if (fileUrl.startsWith(baseUrl)) {
                String fileKey = fileUrl.substring(baseUrl.length() + 1);
                return ossClient.doesObjectExist(ossConfig.getBucketName(), fileKey);
            }
        } catch (Exception e) {
            log.error("检查文件是否存在失败: {}", fileUrl, e);
        }
        return false;
    }
}
