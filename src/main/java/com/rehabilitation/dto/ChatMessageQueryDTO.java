package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 聊天消息查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatMessageQueryDTO extends PageQuery {

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 医生ID
     */
    private Long doctorId;
}
