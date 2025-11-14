package com.rehabilitation.dto;

import lombok.Data;

import java.util.List;

/**
 * 评定计划保存DTO
 */
@Data
public class AssessmentPlanSaveDTO {

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
     * 评定项目列表
     */
    private List<AssessmentItemDTO> assessmentList;

    /**
     * 评定项目详情
     */
    @Data
    public static class AssessmentItemDTO {
        /**
         * 执行场景：机构/居家
         */
        private String executionType;

        /**
         * 量表名称
         */
        private String scaleName;

        /**
         * 执行频率，如：3次/周
         */
        private String executionFrequency;

        /**
         * 执行周期长度（周）
         */
        private Integer cycleDuration;

        /**
         * 总次数（次）
         */
        private Integer totalTimes;

        /**
         * 备注
         */
        private String remarks;
    }
}
