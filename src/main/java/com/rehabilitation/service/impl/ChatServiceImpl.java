package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.ChatMessageQueryDTO;
import com.rehabilitation.dto.ChatMessageVO;
import com.rehabilitation.entity.ChatMessage;
import com.rehabilitation.mapper.ChatMessageMapper;
import com.rehabilitation.service.ChatService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 聊天服务实现
 */
@Service
public class ChatServiceImpl 
    extends ServiceImpl<ChatMessageMapper, ChatMessage> 
    implements ChatService {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public ChatMessageVO sendMessage(Long receiverId, String content, String messageType) {
        // TODO: 获取当前用户信息
        Long senderId = 1L; // 临时写死
        String senderName = "医生"; // 临时写死
        String senderType = "doctor"; // 临时写死

        String conversationId = getConversationId(senderId, receiverId);

        ChatMessage message = new ChatMessage();
        message.setConversationId(conversationId);
        message.setSenderType(senderType);
        message.setSenderId(senderId);
        message.setSenderName(senderName);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setMessageType(messageType);
        message.setIsRead(0);
        message.setSendTime(LocalDateTime.now());

        save(message);

        // 转换为VO对象
        ChatMessageVO messageVO = new ChatMessageVO();
        messageVO.setId(message.getId());
        messageVO.setConversationId(message.getConversationId());
        messageVO.setSenderType(message.getSenderType());
        messageVO.setSenderId(message.getSenderId());
        messageVO.setSenderName(message.getSenderName());
        messageVO.setReceiverId(message.getReceiverId());
        messageVO.setContent(message.getContent());
        messageVO.setMessageType(message.getMessageType());
        messageVO.setIsRead(message.getIsRead());
        messageVO.setSendTime(message.getSendTime());

        // 实时推送消息到接收者
        String destination = "/queue/messages-" + receiverId;
        messagingTemplate.convertAndSend(destination, messageVO);

        return messageVO;
    }

    @Override
    public PageResult<ChatMessageVO> getChatHistory(ChatMessageQueryDTO query) {
        Page<ChatMessageVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<ChatMessageVO> result = baseMapper.selectChatMessagePage(page, query);
        
        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public void markAsRead(String conversationId, Long receiverId) {
        LambdaUpdateWrapper<ChatMessage> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ChatMessage::getConversationId, conversationId)
                    .eq(ChatMessage::getReceiverId, receiverId)
                    .eq(ChatMessage::getIsRead, 0)
                    .set(ChatMessage::getIsRead, 1);
        
        update(updateWrapper);
    }

    @Override
    public String getConversationId(Long doctorId, Long patientId) {
        return "doctor_" + doctorId + "_patient_" + patientId;
    }
}
