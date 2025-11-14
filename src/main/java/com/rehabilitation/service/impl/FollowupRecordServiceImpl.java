package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rehabilitation.dto.FollowupRecordDTO;
import com.rehabilitation.entity.FollowupPatientEnrollment;
import com.rehabilitation.entity.FollowupRecord;
import com.rehabilitation.mapper.FollowupPatientEnrollmentMapper;
import com.rehabilitation.mapper.FollowupRecordMapper;
import com.rehabilitation.service.FollowupRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 随访记录服务实现类
 */
@Service
public class FollowupRecordServiceImpl implements FollowupRecordService {

    @Autowired
    private FollowupRecordMapper followupRecordMapper;

    @Autowired
    private FollowupPatientEnrollmentMapper enrollmentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFollowupRecord(FollowupRecordDTO dto) {
        // 创建随访记录实体
        FollowupRecord record = new FollowupRecord();
        BeanUtils.copyProperties(dto, record);
        record.setCreatedTime(LocalDateTime.now());

        // 保存随访记录
        int result = followupRecordMapper.insert(record);

        // 更新患者入组状态为"随访中"（如果当前状态是"待入组"）
        if (result > 0 && dto.getEnrollmentId() != null) {
            UpdateWrapper<FollowupPatientEnrollment> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", dto.getEnrollmentId())
                        .eq("followup_status", "待入组")
                        .set("followup_status", "随访中")
                        .set("updated_time", LocalDateTime.now());
            enrollmentMapper.update(null, updateWrapper);
        }

        return result > 0;
    }

    @Override
    public List<FollowupRecord> getFollowupRecordsByEnrollmentId(Long enrollmentId) {
        QueryWrapper<FollowupRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enrollment_id", enrollmentId)
                   .orderByDesc("followup_date");
        return followupRecordMapper.selectList(queryWrapper);
    }
}
