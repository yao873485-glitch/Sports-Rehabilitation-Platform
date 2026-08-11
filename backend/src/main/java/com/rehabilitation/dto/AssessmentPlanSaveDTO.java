package com.rehabilitation.dto;

import java.util.List;

/**
 * 评定计划保存DTO
 */
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
     * 执行机构
     */
    private String executionInstitution;

    /**
     * 评定项目列表
     */
    private List<AssessmentItemDTO> assessmentList;

    // Getter and Setter methods
    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getExecutionInstitution() {
        return executionInstitution;
    }

    public void setExecutionInstitution(String executionInstitution) {
        this.executionInstitution = executionInstitution;
    }

    public List<AssessmentItemDTO> getAssessmentList() {
        return assessmentList;
    }

    public void setAssessmentList(List<AssessmentItemDTO> assessmentList) {
        this.assessmentList = assessmentList;
    }

    /**
     * 评定项目详情
     */
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

        // Getter and Setter methods
        public String getExecutionType() {
            return executionType;
        }

        public void setExecutionType(String executionType) {
            this.executionType = executionType;
        }

        public String getScaleName() {
            return scaleName;
        }

        public void setScaleName(String scaleName) {
            this.scaleName = scaleName;
        }

        public String getExecutionFrequency() {
            return executionFrequency;
        }

        public void setExecutionFrequency(String executionFrequency) {
            this.executionFrequency = executionFrequency;
        }

        public Integer getCycleDuration() {
            return cycleDuration;
        }

        public void setCycleDuration(Integer cycleDuration) {
            this.cycleDuration = cycleDuration;
        }

        public Integer getTotalTimes() {
            return totalTimes;
        }

        public void setTotalTimes(Integer totalTimes) {
            this.totalTimes = totalTimes;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
