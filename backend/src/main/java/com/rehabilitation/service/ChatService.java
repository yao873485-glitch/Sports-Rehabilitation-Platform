package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.ChatMessageQueryDTO;
import com.rehabilitation.dto.ChatMessageVO;
import com.rehabilitation.entity.ChatMessage;

/**
 * 聊天服务接口
 */
public interface ChatService extends IService<ChatMessage> {

    /**
     * 发送消息
     */
    ChatMessageVO sendMessage(Long receiverId, String content, String messageType);

    /**
     * 获取聊天历史
     */
    PageResult<ChatMessageVO> getChatHistory(ChatMessageQueryDTO query);

    /**
     * 标记消息为已读
     */
    void markAsRead(String conversationId, Long receiverId);

    /**
     * 获取会话ID
     */
    String getConversationId(Long doctorId, Long patientId);
}
