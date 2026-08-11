package com.rehabilitation.service;

import com.rehabilitation.dto.PendingUserApplicationVO;

import java.util.List;

/**
 * 管理员审核服务
 */
public interface AdminApplicationService {

    /**
     * 查询所有待审核用户
     */
    List<PendingUserApplicationVO> listPendingApplications();

    /**
     * 审核通过
     */
    void approveUser(Long userId);

    /**
     * 审核拒绝
     */
    void rejectUser(Long userId);
}
