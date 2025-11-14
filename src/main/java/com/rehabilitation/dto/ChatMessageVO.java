package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息展示VO
 */
@Data
public class ChatMessageVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 发送人类型
     */
    private String senderType;

    /**
     * 发送人ID
     */
    private Long senderId;

    /**
     * 发送人姓名
     */
    private String senderName;

    /**
     * 接收人ID
     */
    private Long receiverId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 是否已读
     */
    private Integer isRead;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
}
