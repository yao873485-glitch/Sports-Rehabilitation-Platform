package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.SharedFileQueryDTO;
import com.rehabilitation.dto.SharedFileVO;
import com.rehabilitation.entity.SharedFile;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.SharedFileMapper;
import com.rehabilitation.service.FileShareService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件共享服务实现
 */
@Service
public class FileShareServiceImpl
    extends ServiceImpl<SharedFileMapper, SharedFile>
    implements FileShareService {

    @Value("${rehabilitation.file.upload-path:/uploads/}")
    private String uploadPath;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @PostConstruct
    public void init() {
        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    public String uploadFile(MultipartFile file, Long patientId, String patientName) {
        if (file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        
        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // 创建上传目录
            String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String uploadDir = uploadPath + dateDir;
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存文件
            String filePath = uploadDir + "/" + filename;
            file.transferTo(new File(filePath));

            // 生成可访问的URL路径
            String fileUrl = "/uploads/" + dateDir + "/" + filename;

            // 保存文件信息到数据库
            SharedFile sharedFile = new SharedFile();
            sharedFile.setPatientId(patientId);
            sharedFile.setPatientName(patientName);
            sharedFile.setFileName(originalFilename);
            sharedFile.setFileType(getFileType(extension));
            sharedFile.setFileUrl(fileUrl);
            sharedFile.setUploadBy("医生"); // TODO: 获取当前用户
            sharedFile.setUploadTime(LocalDateTime.now());
            sharedFile.setDownloadCount(0);

            save(sharedFile);

            return fileUrl;
        } catch (IOException e) {
            throw new BusinessException("文件上传失败");
        }
    }

    @Override
    public PageResult<SharedFileVO> getFileList(SharedFileQueryDTO query) {
        Page<SharedFileVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<SharedFileVO> result = baseMapper.selectSharedFilePage(page, query);
        
        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public String downloadFile(Long fileId) {
        SharedFile file = getById(fileId);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        // 增加下载次数
        file.setDownloadCount(file.getDownloadCount() + 1);
        updateById(file);

        // 返回完整的可访问URL
        return contextPath + file.getFileUrl();
    }

    @Override
    public void deleteFile(Long fileId) {
        SharedFile file = getById(fileId);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        // 删除文件记录
        removeById(fileId);

        // 将URL转换为物理路径并删除物理文件
        String filePath = file.getFileUrl();
        if (filePath.startsWith("/uploads/")) {
            filePath = filePath.replace("/uploads/", uploadPath);
        }
        File physicalFile = new File(filePath);
        if (physicalFile.exists()) {
            physicalFile.delete();
        }
    }

    private String getFileType(String extension) {
        extension = extension.toLowerCase();
        if (extension.matches("\\.(jpg|jpeg|png|gif|bmp)")) {
            return "image";
        } else if (extension.equals(".pdf")) {
            return "pdf";
        } else if (extension.matches("\\.(doc|docx)")) {
            return "doc";
        } else if (extension.matches("\\.(xls|xlsx)")) {
            return "excel";
        } else {
            return "other";
        }
    }
}
