package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.AssessmentExecutionQueryDTO;
import com.rehabilitation.dto.AssessmentExecutionVO;
import com.rehabilitation.entity.AssessmentExecutionRecord;
import com.rehabilitation.mapper.AssessmentExecutionMapper;
import com.rehabilitation.service.AssessmentExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 评定执行Service实现类
 */
@Service
@RequiredArgsConstructor
public class AssessmentExecutionServiceImpl implements AssessmentExecutionService {

    private final AssessmentExecutionMapper assessmentExecutionMapper;

    @Override
    public IPage<AssessmentExecutionVO> getAssessmentExecutionPage(AssessmentExecutionQueryDTO query) {
        Page<AssessmentExecutionVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return assessmentExecutionMapper.selectAssessmentExecutionPage(page, query);
    }

    @Override
    public AssessmentExecutionVO getAssessmentExecutionById(Integer id) {
        return assessmentExecutionMapper.selectAssessmentExecutionById(id);
    }

    @Override
    public void endAssessmentExecution(Integer id) {
        AssessmentExecutionRecord record = new AssessmentExecutionRecord();
        record.setId(id);
        record.setStatus(4); // 4-已结束
        record.setUpdatedTime(LocalDateTime.now());
        assessmentExecutionMapper.updateById(record);
    }

    @Override
    public void executeAssessment(Integer id) {
        AssessmentExecutionRecord record = new AssessmentExecutionRecord();
        record.setId(id);
        record.setStatus(2); // 2-执行中
        record.setUpdatedTime(LocalDateTime.now());
        assessmentExecutionMapper.updateById(record);
    }

    @Override
    public List<String> getAllDiseaseTypes() {
        return Arrays.asList(
                "妊娠合并糖尿病",
                "肥胖症",
                "高血压",
                "糖尿病",
                "冠心病",
                "慢性阻塞性肺疾病",
                "骨关节炎",
                "颈椎病",
                "腰椎间盘突出",
                "脑卒中后遗症"
        );
    }

    @Override
    public List<String> getAllInstitutions() {
        return Arrays.asList(
                "西南医科大学附属医院",
                "江安县中医院",
                "泸州市人民医院",
                "合江县人民医院",
                "古蔺县人民医院",
                "叙永县人民医院"
        );
    }
}