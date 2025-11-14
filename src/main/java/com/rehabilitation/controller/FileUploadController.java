package com.rehabilitation.controller;

import com.rehabilitation.common.Result;
import com.rehabilitation.service.OssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@Slf4j
@Tag(name = "文件管理", description = "文件上传接口")
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Value("${file.upload.path:E:/sport_xunlian/data/uploads}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:/uploads}")
    private String urlPrefix;

    @Autowired
    private OssService ossService;

    /**
     * 上传证件文件
     */
    @Operation(summary = "上传证件文件")
    @PostMapping("/upload/certificate")
    public Result<Map<String, String>> uploadCertificate(@RequestParam("file") MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        // 检查文件大小（不超过500KB）
        if (file.getSize() > 500 * 1024) {
            return Result.error("文件大小不能超过500KB");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            return Result.error("只能上传jpg或png格式的图片");
        }

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            return Result.error("文件名无效");
        }

        // 生成新文件名（UUID + 原始扩展名）
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;

        // 创建上传目录
        File uploadDir = new File(uploadPath + "/certificates");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 保存文件
        File destFile = new File(uploadDir, newFilename);
        try {
            file.transferTo(destFile);
            log.info("文件上传成功: {}", destFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败");
        }

        // 返回文件访问URL
        String fileUrl = urlPrefix + "/certificates/" + newFilename;
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("filename", newFilename);

        return Result.success(result);
    }

    /**
     * 上传运动图片到OSS
     */
    @Operation(summary = "上传运动图片到OSS")
    @PostMapping("/upload/exercise-image")
    public Result<Map<String, String>> uploadExerciseImage(@RequestParam("file") MultipartFile file) {
        try {
            log.info("开始上传运动图片到OSS，文件名: {}, 大小: {} bytes", file.getOriginalFilename(), file.getSize());

            String fileUrl = ossService.uploadImage(file);

            Map<String, String> data = new HashMap<>();
            data.put("url", fileUrl);
            data.put("name", file.getOriginalFilename());

            return Result.success("上传成功", data);
        } catch (IllegalArgumentException e) {
            log.error("运动图片上传失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("运动图片上传异常", e);
            return Result.error("图片上传失败，请稍后重试");
        }
    }

    /**
     * 删除OSS中的运动图片
     */
    @Operation(summary = "删除OSS中的运动图片")
    @DeleteMapping("/delete/exercise-image")
    public Result<Void> deleteExerciseImage(@RequestParam("url") String fileUrl) {
        try {
            log.info("开始删除运动图片: {}", fileUrl);
            ossService.deleteFile(fileUrl);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            log.error("运动图片删除异常", e);
            return Result.error("图片删除失败，请稍后重试");
        }
    }
}
