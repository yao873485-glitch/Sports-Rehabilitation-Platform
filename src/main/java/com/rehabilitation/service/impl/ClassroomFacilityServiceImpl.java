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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 教室设施信息服务实现类
 */
@Service
@RequiredArgsConstructor
public class ClassroomFacilityServiceImpl extends ServiceImpl<ClassroomFacilityMapper, ClassroomFacilityInfo> implements ClassroomFacilityService {

    private final ClassroomFacilityMapper classroomFacilityMapper;

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
        classroomFacility.setStatus("已上架");
        classroomFacility.setCreatedTime(LocalDateTime.now());
        classroomFacility.setLastOperationTime(LocalDateTime.now());
        save(classroomFacility);
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