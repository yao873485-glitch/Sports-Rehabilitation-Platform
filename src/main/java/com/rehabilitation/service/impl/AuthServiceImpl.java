package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rehabilitation.dto.LoginDTO;
import com.rehabilitation.dto.RegisterDTO;
import com.rehabilitation.entity.RegistrationApplication;
import com.rehabilitation.entity.User;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.RegistrationApplicationMapper;
import com.rehabilitation.mapper.UserMapper;
import com.rehabilitation.service.AuthService;
import com.rehabilitation.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final RegistrationApplicationMapper registrationApplicationMapper;
    private final JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        // 密码MD5加密
        String encryptedPassword = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));

        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, loginDTO.getPhone())
                   .eq(User::getPassword, encryptedPassword);

        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new BusinessException("手机号码或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("您的账号正在审核中，请耐心等待");
        }

        if (user.getStatus() == 2) {
            throw new BusinessException("您的账号审核未通过，请联系管理员");
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("phone", user.getPhone());
        result.put("role", user.getRole());
        result.put("organization", user.getOrganization());

        return result;
    }

    @Override
    public Map<String, Object> register(RegisterDTO registerDTO) {
        // 检查手机号是否已注册
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getPhone, registerDTO.getPhone());
        Long userCount = userMapper.selectCount(userQuery);

        if (userCount > 0) {
            throw new BusinessException("该手机号已被注册");
        }

        // 检查用户名是否已存在
        LambdaQueryWrapper<User> usernameQuery = new LambdaQueryWrapper<>();
        usernameQuery.eq(User::getUsername, registerDTO.getUsername());
        Long usernameCount = userMapper.selectCount(usernameQuery);

        if (usernameCount > 0) {
            throw new BusinessException("该用户名已被使用");
        }

        // 检查是否已提交过申请
        LambdaQueryWrapper<RegistrationApplication> appQuery = new LambdaQueryWrapper<>();
        appQuery.eq(RegistrationApplication::getPhone, registerDTO.getPhone())
               .eq(RegistrationApplication::getStatus, 0); // 待审核状态
        Long appCount = registrationApplicationMapper.selectCount(appQuery);

        if (appCount > 0) {
            throw new BusinessException("您已提交过注册申请，请耐心等待审核");
        }

        // 密码MD5加密
        String encryptedPassword = DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes(StandardCharsets.UTF_8));

        // 创建注册申请
        RegistrationApplication application = new RegistrationApplication();
        application.setUsername(registerDTO.getUsername());
        application.setPhone(registerDTO.getPhone());
        application.setGender(registerDTO.getGender());
        application.setBirthDate(registerDTO.getBirthDate());
        application.setPlatform(registerDTO.getPlatform());
        application.setOrganization(registerDTO.getOrganization());
        application.setApplyRole(registerDTO.getApplyRole());
        application.setCertificateUrl(registerDTO.getCertificateUrl());
        application.setPassword(encryptedPassword);
        application.setStatus(0); // 待审核

        registrationApplicationMapper.insert(application);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("message", "注册申请提交成功，请等待管理员审核");
        result.put("applicationId", application.getId());

        return result;
    }
}
