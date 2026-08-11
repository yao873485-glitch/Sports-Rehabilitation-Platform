package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.dto.FollowupPlanDTO;
import com.rehabilitation.dto.FollowupPlanQueryDTO;
import com.rehabilitation.entity.FollowupPlan;
import com.rehabilitation.entity.FollowupProjectConfig;
import com.rehabilitation.mapper.FollowupPlanMapper;
import com.rehabilitation.service.FollowupPlanService;
import com.rehabilitation.service.FollowupProjectConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 随访计划服务实现类
 */
@Service
public class FollowupPlanServiceImpl extends ServiceImpl<FollowupPlanMapper, FollowupPlan>
        implements FollowupPlanService {

    @Autowired
    private FollowupProjectConfigService followupProjectConfigService;

    @Override
    public FollowupPlan savePlan(FollowupPlanDTO dto) {
        FollowupPlan plan = new FollowupPlan();

        // 获取关联的随访项目名称
        String linkedProjectName = "";
        if (dto.getLinkedProjectId() != null) {
            FollowupProjectConfig project = followupProjectConfigService.getById(dto.getLinkedProjectId());
            if (project != null) {
                linkedProjectName = project.getProjectName();
            }
        }

        // 生成计划名称：可用团队 + 项目名称
        String generatedPlanName = dto.getAvailableTeam() + linkedProjectName;

        // 设置字段值（避免Lombok问题）
        plan.setPlanName(generatedPlanName);
        plan.setPlanDescription(dto.getPlanDescription());
        plan.setLinkedProjectId(dto.getLinkedProjectId());
        plan.setLinkedProjectName(linkedProjectName);
        plan.setAvailableTeam(dto.getAvailableTeam());
        plan.setOwnerInstitution(dto.getOwnerInstitution());
        plan.setFirstFollowupBaseline(dto.getFirstFollowupBaseline());

        // 设置版本号
        plan.setVersionNumber("V1.0");

        // 设置版本备注为空
        plan.setVersionRemark("");

        // 设置状态：根据是否草稿
        if (dto.getIsDraft() != null && dto.getIsDraft()) {
            plan.setStatus("未发布");
            plan.setIsDraft(1);
        } else {
            plan.setStatus("已发布");
            plan.setIsDraft(0);
        }

        // 随访邀请二维码暂时为空
        plan.setInvitationQrCodeUrl("");

        // 设置操作人
        plan.setOperator(dto.getOperator() != null ? dto.getOperator() : "系统");

        // 手动设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        plan.setCreatedTime(now);
        plan.setUpdatedTime(now);

        // 保存到数据库
        this.save(plan);

        return plan;
    }

    @Override
    public Page<FollowupPlan> getPlanPage(FollowupPlanQueryDTO queryDTO) {
        System.out.println("===== FollowupPlanServiceImpl.getPlanPage 开始执行 =====");
        System.out.println("查询参数 - pageNum: " + queryDTO.getPageNum() + ", pageSize: " + queryDTO.getPageSize());

        // 创建分页对象
        Page<FollowupPlan> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        System.out.println("创建分页对象 - current: " + page.getCurrent() + ", size: " + page.getSize());

        // 构建查询条件
        QueryWrapper<FollowupPlan> queryWrapper = new QueryWrapper<>();

        // 计划名称模糊查询
        if (StringUtils.hasText(queryDTO.getPlanName())) {
            queryWrapper.like("plan_name", queryDTO.getPlanName());
            System.out.println("添加计划名称查询条件: " + queryDTO.getPlanName());
        }

        // 状态查询
        if (StringUtils.hasText(queryDTO.getStatus())) {
            queryWrapper.eq("status", queryDTO.getStatus());
            System.out.println("添加状态查询条件: " + queryDTO.getStatus());
        }

        // 按创建时间倒序排列
        queryWrapper.orderByDesc("created_time");

        System.out.println("执行数据库查询...");
        // 执行分页查询
        Page<FollowupPlan> result = this.page(page, queryWrapper);

        System.out.println("查询完成 - 总记录数: " + result.getTotal() + ", 当前页记录数: " + result.getRecords().size());
        if (result.getRecords().size() > 0) {
            System.out.println("第一条记录: " + result.getRecords().get(0).getPlanName());
        }

        return result;
    }
}
