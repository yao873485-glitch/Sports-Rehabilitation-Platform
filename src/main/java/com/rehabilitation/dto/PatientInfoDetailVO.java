package com.rehabilitation.dto;

import com.rehabilitation.entity.PatientDetail;
import com.rehabilitation.entity.PatientInfo;
import lombok.Data;

/**
 * 患者详细信息VO
 */
@Data
public class PatientInfoDetailVO {

    /**
     * 基本信息
     */
    private PatientInfo basicInfo;

    /**
     * 详细信息
     */
    private PatientDetail detailInfo;
}