package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.EduContentApplicationQueryDTO;
import com.rehabilitation.dto.EduContentApplicationVO;
import com.rehabilitation.entity.EduContentApplication;
import com.rehabilitation.mapper.EduContentApplicationMapper;
import com.rehabilitation.service.EduContentApplicationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内容应用管理Service实现类
 */
@Service
public class EduContentApplicationServiceImpl extends ServiceImpl<EduContentApplicationMapper, EduContentApplication>
        implements EduContentApplicationService {

    @Resource
    private EduContentApplicationMapper contentApplicationMapper;

    @Override
    public PageResult<EduContentApplicationVO> getContentApplicationPage(EduContentApplicationQueryDTO queryDTO) {
        // 使用自定义的联合查询方法
        Page<EduContentApplicationVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        List<EduContentApplicationVO> voList = contentApplicationMapper.selectContentApplicationPage(
                page,
                queryDTO.getContentTitle(),
                queryDTO.getAuthor(),
                queryDTO.getResourceType(),
                queryDTO.getCategorySection(),
                queryDTO.getStatus(),
                queryDTO.getChannel(),
                queryDTO.getPinStatus(),
                queryDTO.getPublishStartTime(),
                queryDTO.getPublishEndTime()
        );

        page.setRecords(voList);

        return PageResult.build(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public EduContentApplicationVO getContentApplicationById(Long id) {
        // 注意：由于数据现在通过联合查询获取，此方法已不再使用
        // 如需根据ID查询，应该根据contentId和contentType查询素材表
        return null;
    }
}
