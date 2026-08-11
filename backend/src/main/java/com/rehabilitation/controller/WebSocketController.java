package com.rehabilitation.controller;

import com.rehabilitation.dto.ChatMessageVO;
import com.rehabilitation.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * WebSocket消息控制器
 */
@Controller
public class WebSocketController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 处理聊天消息
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Map<String, Object> message) {
        try {
            Long receiverId = Long.valueOf(message.get("receiverId").toString());
            String content = message.get("content").toString();
            String messageType = message.get("messageType") != null ?
                message.get("messageType").toString() : "text";

            // 保存消息到数据库
            ChatMessageVO chatMessage = chatService.sendMessage(receiverId, content, messageType);

            // 推送给接收方
            messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/messages",
                chatMessage
            );

            log.info("消息发送成功: 发送方={}, 接收方={}, 内容={}",
                chatMessage.getSenderId(), receiverId, content);

        } catch (Exception e) {
            log.error("发送消息失败", e);
        }
    }

    /**
     * 处理输入状态
     */
    @MessageMapping("/chat.typing")
    public void handleTyping(@Payload Map<String, Object> typingData) {
        try {
            Long senderId = Long.valueOf(typingData.get("senderId").toString());
            Long receiverId = Long.valueOf(typingData.get("receiverId").toString());
            Boolean isTyping = Boolean.valueOf(typingData.get("isTyping").toString());

            // 推送给接收方
            messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/typing",
                Map.of(
                    "senderId", senderId,
                    "isTyping", isTyping
                )
            );

        } catch (Exception e) {
            log.error("处理输入状态失败", e);
        }
    }
}