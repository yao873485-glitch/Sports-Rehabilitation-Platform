package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 医患共享文件实体
 */
@Data
@TableName("shared_file")
public class SharedFile {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 患者ID
     */
    @TableField("patient_id")
    private Long patientId;

    /**
     * 患者姓名
     */
    @TableField("patient_name")
    private String patientName;

    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件类型：image-图片，pdf-PDF，doc-文档，excel-表格，other-其他
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 文件在OSS/S3中的URL
     */
    @TableField("file_url")
    private String fileUrl;

    /**
     * 上传人姓名
     */
    @TableField("upload_by")
    private String uploadBy;

    /**
     * 上传时间
     */
    @TableField(value = "upload_time", fill = FieldFill.INSERT)
    private LocalDateTime uploadTime;

    /**
     * 下载次数
     */
    @TableField("download_count")
    private Integer downloadCount;
}
