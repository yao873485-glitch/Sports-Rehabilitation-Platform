package com.rehabilitation.service;

import com.rehabilitation.dto.FollowupRecordDTO;
import com.rehabilitation.entity.FollowupRecord;

import java.util.List;

/**
 * 随访记录服务接口
 */
public interface FollowupRecordService {

    /**
     * 保存随访记录
     * @param dto 随访记录DTO
     * @return 保存结果
     */
    boolean saveFollowupRecord(FollowupRecordDTO dto);

    /**
     * 根据入组记录ID查询随访记录列表
     * @param enrollmentId 入组记录ID
     * @return 随访记录列表
     */
    List<FollowupRecord> getFollowupRecordsByEnrollmentId(Long enrollmentId);
}
