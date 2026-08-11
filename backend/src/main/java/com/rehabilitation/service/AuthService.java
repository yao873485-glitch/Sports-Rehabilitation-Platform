package com.rehabilitation.service;

import com.rehabilitation.dto.LoginDTO;
import com.rehabilitation.dto.RegisterDTO;

import java.util.Map;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果（包含token和用户信息）
     */
    Map<String, Object> login(LoginDTO loginDTO);

    /**
     * 用户注册申请
     *
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    Map<String, Object> register(RegisterDTO registerDTO);
}
