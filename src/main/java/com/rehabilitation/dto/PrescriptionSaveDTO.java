package com.rehabilitation.dto;

import lombok.Data;

import java.util.List;

/**
 * 运动处方保存DTO
 */
@Data
public class PrescriptionSaveDTO {

    /**
     * 方案ID
     */
    private String schemeId;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 病种
     */
    private String diseaseType;

    /**
     * 运动项目列表
     */
    private List<ExerciseItemDTO> exerciseList;

    /**
     * 运动项目详情
     */
    @Data
    public static class ExerciseItemDTO {
        /**
         * 运动场景：机构/居家
         */
        private String exerciseScene;

        /**
         * 运动类型
         */
        private String exerciseType;

        /**
         * 运动方式
         */
        private String exerciseMethod;

        /**
         * 注意事项
         */
        private String precautions;

        /**
         * 单次运动时间（分钟）
         */
        private Integer durationMinutes;

        /**
         * 运动频率：如 1次/周, 3次/周, 7次/周 等
         */
        private String exerciseFrequency;

        /**
         * 运动强度：高强度、中等、低强度
         */
        private String exerciseIntensity;

        /**
         * 运动周期（周）
         */
        private Integer cycleWeeks;

        /**
         * 计划总次数（次）
         */
        private Integer totalSessions;

        /**
         * 每周建议累计运动时长（分钟）
         */
        private Integer weeklyTotalMinutes;

        /**
         * 运动进阶要求描述
         */
        private String progression;

        /**
         * 补充说明
         */
        private String remarks;

        /**
         * 运动图示图片路径
         */
        private String exerciseImage;
    }
}
