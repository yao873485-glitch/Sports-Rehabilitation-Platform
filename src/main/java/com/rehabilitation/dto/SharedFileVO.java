package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 共享文件展示VO
 */
@Data
public class SharedFileVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 上传人姓名
     */
    private String uploadBy;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 下载次数
     */
    private Integer downloadCount;
}
