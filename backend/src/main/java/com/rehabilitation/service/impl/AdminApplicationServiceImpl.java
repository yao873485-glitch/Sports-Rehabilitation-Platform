package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rehabilitation.dto.PendingUserApplicationVO;
import com.rehabilitation.entity.User;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.UserMapper;
import com.rehabilitation.service.AdminApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员审核服务实现
 */
@Service
public class AdminApplicationServiceImpl implements AdminApplicationService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<PendingUserApplicationVO> listPendingApplications() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getStatus, 0)
                .orderByDesc(User::getCreateTime);

        List<User> pendingUsers = userMapper.selectList(queryWrapper);
        return pendingUsers.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public void approveUser(Long userId) {
        User user = getPendingUser(userId);
        user.setStatus(1);
        userMapper.updateById(user);
    }

    @Override
    public void rejectUser(Long userId) {
        User user = getPendingUser(userId);
        user.setStatus(2);
        userMapper.updateById(user);
    }

    private User getPendingUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getStatus() == null || user.getStatus() != 0) {
            throw new BusinessException("该用户不在待审核状态");
        }
        return user;
    }

    private PendingUserApplicationVO convertToVO(User user) {
        PendingUserApplicationVO vo = new PendingUserApplicationVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setPhone(user.getPhone());
        vo.setGender(user.getGender());
        vo.setApplyRole(user.getRole());
        vo.setOrganization(user.getOrganization());
        vo.setCertificateUrl(user.getCertificateUrl());
        if (user.getCreateTime() != null) {
            vo.setApplyTime(user.getCreateTime().format(FORMATTER));
        }
        return vo;
    }
}
