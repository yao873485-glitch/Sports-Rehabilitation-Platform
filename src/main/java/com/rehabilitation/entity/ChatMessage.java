package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息实体
 */
@Data
@TableName("chat_message")
public class ChatMessage {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID，格式：doctor_{did}_patient_{pid}
     */
    @TableField("conversation_id")
    private String conversationId;

    /**
     * 发送人类型：doctor-医生，patient-患者
     */
    @TableField("sender_type")
    private String senderType;

    /**
     * 发送人ID
     */
    @TableField("sender_id")
    private Long senderId;

    /**
     * 发送人姓名
     */
    @TableField("sender_name")
    private String senderName;

    /**
     * 接收人ID
     */
    @TableField("receiver_id")
    private Long receiverId;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 消息类型：text-文本，image-图片，file-文件，voice-语音
     */
    @TableField("message_type")
    private String messageType;

    /**
     * 是否已读：0-未读，1-已读
     */
    @TableField("is_read")
    private Integer isRead;

    /**
     * 发送时间
     */
    @TableField(value = "send_time", fill = FieldFill.INSERT)
    private LocalDateTime sendTime;
}
