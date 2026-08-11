package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.SharedFileQueryDTO;
import com.rehabilitation.dto.SharedFileVO;
import com.rehabilitation.entity.SharedFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件共享服务接口
 */
public interface FileShareService extends IService<SharedFile> {

    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file, Long patientId, String patientName);

    /**
     * 获取文件列表
     */
    PageResult<SharedFileVO> getFileList(SharedFileQueryDTO query);

    /**
     * 下载文件
     */
    String downloadFile(Long fileId);

    /**
     * 删除文件
     */
    void deleteFile(Long fileId);
}
