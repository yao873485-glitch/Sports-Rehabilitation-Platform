package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.ExercisePrescriptionQueryDTO;
import com.rehabilitation.entity.ExercisePrescription;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运动处方信息Mapper接口
 */
public interface ExercisePrescriptionMapper extends BaseMapper<ExercisePrescription> {

    /**
     * 分页查询运动处方列表
     */
    IPage<ExercisePrescription> selectExercisePrescriptionPage(Page<ExercisePrescription> page, @Param("query") ExercisePrescriptionQueryDTO query);

    /**
     * 查询所有病种类型
     */
    List<String> selectAllDiseaseTypes();

    /**
     * 批量更新运动处方状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);
}