package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.ClassroomFacilityQueryDTO;
import com.rehabilitation.dto.ClassroomFacilityVO;
import com.rehabilitation.entity.ClassroomFacilityInfo;
import com.rehabilitation.mapper.ClassroomFacilityMapper;
import com.rehabilitation.service.ClassroomFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 教室设施信息服务实现类
 */
@Service
public class ClassroomFacilityServiceImpl extends ServiceImpl<ClassroomFacilityMapper, ClassroomFacilityInfo> implements ClassroomFacilityService {

    @Autowired
    private ClassroomFacilityMapper classroomFacilityMapper;

    @Override
    public PageResult<ClassroomFacilityVO> getClassroomFacilityPage(ClassroomFacilityQueryDTO query) {
        Page<ClassroomFacilityVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<ClassroomFacilityVO> result = classroomFacilityMapper.selectClassroomFacilityPage(page, query);

        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public ClassroomFacilityVO getClassroomFacilityById(Integer id) {
        return classroomFacilityMapper.selectClassroomFacilityById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addClassroomFacility(ClassroomFacilityInfo classroomFacility) {
        // 如果没有设置状态,默认为"已下架"
        if (classroomFacility.getStatus() == null || classroomFacility.getStatus().isEmpty()) {
            classroomFacility.setStatus("已下架");
        }
        classroomFacility.setCreatedTime(LocalDateTime.now());
        classroomFacility.setLastOperationTime(LocalDateTime.now());

        // 先保存教室信息
        save(classroomFacility);

        // 保存成功后自动生成教室编码
        if (classroomFacility.getId() != null) {
            String classroomCode = generateClassroomCode(classroomFacility);
            classroomFacility.setClassroomCode(classroomCode);
            updateById(classroomFacility);
        }
    }

    /**
     * 生成教室编码
     * 格式: 运动方式首字母 + 教室ID (例如: YJ1, YG2)
     */
    private String generateClassroomCode(ClassroomFacilityInfo classroomFacility) {
        String exerciseType = classroomFacility.getExerciseType();
        if (exerciseType == null || exerciseType.isEmpty()) {
            return "CR" + classroomFacility.getId();
        }

        // 获取运动方式首字母拼音
        String prefix = getExerciseTypePrefix(exerciseType);
        return prefix + classroomFacility.getId();
    }

    /**
     * 获取运动方式的简称前缀
     */
    private String getExerciseTypePrefix(String exerciseType) {
        switch (exerciseType) {
            case "游泳":
                return "YY";
            case "瑜伽":
                return "YJ";
            case "普拉提":
                return "PLT";
            case "功率自行车":
                return "ZXC";
            case "跑步机":
                return "PBJ";
            case "力量训练":
                return "LLXL";
            case "有氧操":
                return "YYC";
            default:
                return "CR";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClassroomFacility(ClassroomFacilityInfo classroomFacility) {
        classroomFacility.setLastOperationTime(LocalDateTime.now());
        updateById(classroomFacility);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClassroomFacility(Integer id) {
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void offlineClassroomFacility(Integer id) {
        ClassroomFacilityInfo classroomFacility = getById(id);
        if (classroomFacility != null) {
            classroomFacility.setStatus("已下架");
            classroomFacility.setLastOperationTime(LocalDateTime.now());
            updateById(classroomFacility);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onlineClassroomFacility(Integer id) {
        ClassroomFacilityInfo classroomFacility = getById(id);
        if (classroomFacility != null) {
            classroomFacility.setStatus("已上架");
            classroomFacility.setLastOperationTime(LocalDateTime.now());
            updateById(classroomFacility);
        }
    }

    @Override
    public List<String> getAllExerciseTypes() {
        return lambdaQuery()
                .select(ClassroomFacilityInfo::getExerciseType)
                .groupBy(ClassroomFacilityInfo::getExerciseType)
                .list()
                .stream()
                .map(ClassroomFacilityInfo::getExerciseType)
                .collect(Collectors.toList());
    }
}