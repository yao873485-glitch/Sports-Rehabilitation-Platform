package com.rehabilitation.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 注册DTO
 */
@Data
public class RegisterDTO {

    /**
     * 账户用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空")
    private String gender;

    /**
     * 出生日期
     */
    @NotNull(message = "出生日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 所在平台
     */
    private String platform = "体医融合平台";

    /**
     * 所在机构
     */
    @NotBlank(message = "所在机构不能为空")
    private String organization;

    /**
     * 申请角色
     */
    @NotBlank(message = "申请角色不能为空")
    private String applyRole;

    /**
     * 证件URL
     */
    @NotBlank(message = "证件不能为空")
    private String certificateUrl;
}
