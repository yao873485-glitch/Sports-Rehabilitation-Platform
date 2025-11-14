package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.ClassroomFacilityQueryDTO;
import com.rehabilitation.dto.ClassroomFacilityVO;
import com.rehabilitation.entity.ClassroomFacilityInfo;

import java.util.List;

/**
 * 教室设施信息服务接口
 */
public interface ClassroomFacilityService extends IService<ClassroomFacilityInfo> {

    /**
     * 分页查询教室设施列表
     */
    PageResult<ClassroomFacilityVO> getClassroomFacilityPage(ClassroomFacilityQueryDTO query);

    /**
     * 获取教室设施详情
     */
    ClassroomFacilityVO getClassroomFacilityById(Integer id);

    /**
     * 新增教室设施
     */
    void addClassroomFacility(ClassroomFacilityInfo classroomFacility);

    /**
     * 更新教室设施
     */
    void updateClassroomFacility(ClassroomFacilityInfo classroomFacility);

    /**
     * 删除教室设施
     */
    void deleteClassroomFacility(Integer id);

    /**
     * 下架教室设施
     */
    void offlineClassroomFacility(Integer id);

    /**
     * 上架教室设施
     */
    void onlineClassroomFacility(Integer id);

    /**
     * 获取所有运动类型
     */
    List<String> getAllExerciseTypes();
}