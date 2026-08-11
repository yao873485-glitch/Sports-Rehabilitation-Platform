package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.ChatMessageQueryDTO;
import com.rehabilitation.dto.ChatMessageVO;
import com.rehabilitation.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 聊天消息Mapper
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    /**
     * 分页查询聊天消息列表
     */
    IPage<ChatMessageVO> selectChatMessagePage(Page<ChatMessageVO> page, 
                                             @Param("query") ChatMessageQueryDTO query);
}
