package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.FollowupEnrollmentDTO;
import com.rehabilitation.dto.FollowupEnrollmentVO;
import com.rehabilitation.dto.FollowupQueryDTO;
import com.rehabilitation.entity.FollowupPatientEnrollment;

/**
 * 随访患者入组记录服务接口
 */
public interface FollowupService extends IService<FollowupPatientEnrollment> {

    /**
     * 分页查询随访患者列表
     */
    PageResult<FollowupEnrollmentVO> getFollowupPage(FollowupQueryDTO query);

    /**
     * 根据ID获取随访患者详情
     */
    FollowupEnrollmentVO getFollowupDetail(Long id);

    /**
     * 移除项目
     */
    void removeFromProject(Long id);

    /**
     * 患者入组（创建随访记录）
     * @param dto 入组信息
     * @return 入组记录ID
     */
    Long enrollPatient(FollowupEnrollmentDTO dto);
}
