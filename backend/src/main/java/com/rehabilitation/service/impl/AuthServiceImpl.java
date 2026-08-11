package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rehabilitation.dto.LoginDTO;
import com.rehabilitation.dto.RegisterDTO;
import com.rehabilitation.entity.User;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.UserMapper;
import com.rehabilitation.service.AuthService;
import com.rehabilitation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        String encryptedPassword = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, loginDTO.getPhone())
                .eq(User::getPassword, encryptedPassword);

        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("手机号或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new BusinessException("您的账号尚未通过审核，请联系管理员。");
        }

        if (user.getStatus() == 2) {
            throw new BusinessException("您的账号审核未通过，请联系管理员。");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

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
        User existingUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, registerDTO.getPhone()));

        if (existingUser != null) {
            if (existingUser.getStatus() == 1) {
                throw new BusinessException("该手机号已被注册");
            }
            if (existingUser.getStatus() == 0) {
                throw new BusinessException("您已提交过注册申请，请耐心等待审核");
            }
        }

        LambdaQueryWrapper<User> usernameQuery = new LambdaQueryWrapper<>();
        usernameQuery.eq(User::getUsername, registerDTO.getUsername());
        User usernameUser = userMapper.selectOne(usernameQuery);
        if (usernameUser != null && (existingUser == null || !usernameUser.getId().equals(existingUser.getId()))) {
            throw new BusinessException("该用户名已被使用");
        }

        String encryptedPassword = DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes(StandardCharsets.UTF_8));

        if (existingUser != null && existingUser.getStatus() == 2) {
            existingUser.setUsername(registerDTO.getUsername());
            existingUser.setGender(registerDTO.getGender());
            existingUser.setBirthDate(registerDTO.getBirthDate());
            existingUser.setPlatform(registerDTO.getPlatform());
            existingUser.setOrganization(registerDTO.getOrganization());
            existingUser.setRole(registerDTO.getApplyRole());
            existingUser.setCertificateUrl(registerDTO.getCertificateUrl());
            existingUser.setPassword(encryptedPassword);
            existingUser.setStatus(0);
            existingUser.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(existingUser);
        } else {
            User user = new User();
            user.setUsername(registerDTO.getUsername());
            user.setPhone(registerDTO.getPhone());
            user.setGender(registerDTO.getGender());
            user.setBirthDate(registerDTO.getBirthDate());
            user.setPlatform(registerDTO.getPlatform());
            user.setOrganization(registerDTO.getOrganization());
            user.setRole(registerDTO.getApplyRole());
            user.setCertificateUrl(registerDTO.getCertificateUrl());
            user.setPassword(encryptedPassword);
            user.setStatus(0);
            LocalDateTime now = LocalDateTime.now();
            user.setCreateTime(now);
            user.setUpdateTime(now);
            userMapper.insert(user);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("message", "注册申请提交成功，请等待管理员审核");
        return result;
    }
}
