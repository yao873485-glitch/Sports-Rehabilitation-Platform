package com.rehabilitation.controller;

import com.rehabilitation.common.Result;
import com.rehabilitation.dto.AdminLoginDTO;
import com.rehabilitation.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员登录
 */
@Tag(name = "管理员认证", description = "管理员登录接口")
@RestController
@RequestMapping("/admin")
public class AdminAuthController {

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:142536}")
    private String adminPassword;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody AdminLoginDTO loginDTO) {
        if (!adminUsername.equals(loginDTO.getUsername()) || !adminPassword.equals(loginDTO.getPassword())) {
            return Result.error(401, "管理员账号或密码错误");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("token", jwtUtil.generateToken(0L, adminUsername));
        data.put("username", adminUsername);
        return Result.success("登录成功", data);
    }
}
