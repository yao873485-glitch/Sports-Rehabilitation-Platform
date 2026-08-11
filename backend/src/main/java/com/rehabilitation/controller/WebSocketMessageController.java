package com.rehabilitation.controller;

import com.rehabilitation.dto.ChatMessageVO;
import com.rehabilitation.entity.ChatMessage;
import com.rehabilitation.mapper.ChatMessageMapper;
import com.rehabilitation.messaging.BusinessEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * WebSocket消息处理控制器
 */
@Controller
public class WebSocketMessageController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketMessageController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageMapper chatMessageMapper;
    @Autowired
    private BusinessEventPublisher businessEventPublisher;

    /**
     * 处理客户端发送的聊天消息
     * 客户端发送到 /app/chat
     */
    @MessageMapping("/chat")
    public void handleChatMessage(@Payload ChatMessageVO messageVO,
                                  SimpMessageHeaderAccessor headerAccessor) {

        // 从会话属性中获取发送者信息
        Long senderId = (Long) headerAccessor.getSessionAttributes().get("userId");
        String senderName = (String) headerAccessor.getSessionAttributes().get("username");

        log.info("收到消息 - 发送者: {}, 接收者: {}, 内容: {}",
                senderId, messageVO.getReceiverId(), messageVO.getContent());

        // 创建消息实体
        ChatMessage message = new ChatMessage();
        message.setConversationId(generateConversationId(senderId, messageVO.getReceiverId()));
        message.setSenderType(messageVO.getSenderType() != null ? messageVO.getSenderType() : "user");
        message.setSenderId(senderId);
        message.setSenderName(senderName);
        message.setReceiverId(messageVO.getReceiverId());
        message.setContent(messageVO.getContent());
        message.setMessageType(messageVO.getMessageType() != null ? messageVO.getMessageType() : "text");
        message.setIsRead(0);
        message.setSendTime(LocalDateTime.now());

        // 保存消息到数据库
        chatMessageMapper.insert(message);

        // 构建返回的VO对象
        ChatMessageVO responseVO = new ChatMessageVO();
        responseVO.setId(message.getId());
        responseVO.setConversationId(message.getConversationId());
        responseVO.setSenderType(message.getSenderType());
        responseVO.setSenderId(message.getSenderId());
        responseVO.setSenderName(message.getSenderName());
        responseVO.setReceiverId(message.getReceiverId());
        responseVO.setContent(message.getContent());
        responseVO.setMessageType(message.getMessageType());
        responseVO.setIsRead(message.getIsRead());
        responseVO.setSendTime(message.getSendTime());

        // 发送消息到接收者的专属队列
        // 接收者订阅: /queue/messages-{receiverId}
        String destination = "/queue/messages-" + messageVO.getReceiverId();
        messagingTemplate.convertAndSend(destination, responseVO);
        businessEventPublisher.publishChatEvent("WS_CHAT_SEND", message.getConversationId(), senderId, messageVO.getReceiverId());

        log.info("消息已发送到: {}", destination);
    }

    /**
     * 生成会话ID
     */
    private String generateConversationId(Long userId1, Long userId2) {
        // 确保会话ID的一致性，小的ID在前
        if (userId1 < userId2) {
            return "user_" + userId1 + "_user_" + userId2;
        } else {
            return "user_" + userId2 + "_user_" + userId1;
        }
    }
}
