package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运动处方查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PrescriptionQueryDTO extends PageQuery {

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 处方状态
     */
    private String prescriptionStatus;

    /**
     * 入组机构
     */
    private String enrollmentInstitution;

    /**
     * 执行机构
     */
    private String executionInstitution;

    /**
     * 搜索关键字（患者姓名、电话或档案号）
     */
    private String searchKeyword;
}
