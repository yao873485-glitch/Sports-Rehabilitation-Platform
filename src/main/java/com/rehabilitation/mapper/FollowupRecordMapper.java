package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rehabilitation.entity.FollowupRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 随访记录Mapper接口
 */
@Mapper
public interface FollowupRecordMapper extends BaseMapper<FollowupRecord> {
}
