package com.rehabilitation.dto;

import java.util.List;

/**
 * 运动处方保存DTO
 */
public class PrescriptionSaveDTO {

    /**
     * 处方ID（编辑时使用）
     */
    private Integer prescriptionId;

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
     * 运动项目列表
     */
    private List<ExerciseItemDTO> exerciseList;

    // ========== 手动添加 Getter 和 Setter 方法（解决 Lombok 编译问题）==========

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

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

    public List<ExerciseItemDTO> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(List<ExerciseItemDTO> exerciseList) {
        this.exerciseList = exerciseList;
    }

    /**
     * 运动项目详情
     */
    public static class ExerciseItemDTO {
        /**
         * 运动项目ID（编辑时使用）
         */
        private Integer id;

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

        // ========== 手动添加 Getter 方法（解决 Lombok 编译问题）==========

        public Integer getId() {
            return id;
        }

        public String getExerciseScene() {
            return exerciseScene;
        }

        public String getExerciseType() {
            return exerciseType;
        }

        public String getExerciseMethod() {
            return exerciseMethod;
        }

        public String getPrecautions() {
            return precautions;
        }

        public Integer getDurationMinutes() {
            return durationMinutes;
        }

        public String getExerciseFrequency() {
            return exerciseFrequency;
        }

        public String getExerciseIntensity() {
            return exerciseIntensity;
        }

        public Integer getCycleWeeks() {
            return cycleWeeks;
        }

        public Integer getTotalSessions() {
            return totalSessions;
        }

        public Integer getWeeklyTotalMinutes() {
            return weeklyTotalMinutes;
        }

        public String getProgression() {
            return progression;
        }

        public String getRemarks() {
            return remarks;
        }

        public String getExerciseImage() {
            return exerciseImage;
        }

        // ========== 手动添加 Setter 方法（解决 Lombok 编译问题）==========

        public void setId(Integer id) {
            this.id = id;
        }

        public void setExerciseScene(String exerciseScene) {
            this.exerciseScene = exerciseScene;
        }

        public void setExerciseType(String exerciseType) {
            this.exerciseType = exerciseType;
        }

        public void setExerciseMethod(String exerciseMethod) {
            this.exerciseMethod = exerciseMethod;
        }

        public void setPrecautions(String precautions) {
            this.precautions = precautions;
        }

        public void setDurationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
        }

        public void setExerciseFrequency(String exerciseFrequency) {
            this.exerciseFrequency = exerciseFrequency;
        }

        public void setExerciseIntensity(String exerciseIntensity) {
            this.exerciseIntensity = exerciseIntensity;
        }

        public void setCycleWeeks(Integer cycleWeeks) {
            this.cycleWeeks = cycleWeeks;
        }

        public void setTotalSessions(Integer totalSessions) {
            this.totalSessions = totalSessions;
        }

        public void setWeeklyTotalMinutes(Integer weeklyTotalMinutes) {
            this.weeklyTotalMinutes = weeklyTotalMinutes;
        }

        public void setProgression(String progression) {
            this.progression = progression;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public void setExerciseImage(String exerciseImage) {
            this.exerciseImage = exerciseImage;
        }
    }
}
