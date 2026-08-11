package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.ChatMessageQueryDTO;
import com.rehabilitation.dto.ChatMessageVO;
import com.rehabilitation.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 聊天控制器
 */
@Tag(name = "医患沟通平台", description = "聊天消息管理接口")
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 发送消息
     */
    @Operation(summary = "发送消息")
    @PostMapping("/send")
    public Result<ChatMessageVO> sendMessage(@RequestParam Long receiverId,
                                           @RequestParam String content,
                                           @RequestParam(defaultValue = "text") String messageType) {
        ChatMessageVO message = chatService.sendMessage(receiverId, content, messageType);
        return Result.success(message);
    }

    /**
     * 获取聊天历史
     */
    @Operation(summary = "获取聊天历史")
    @GetMapping("/history")
    public Result<PageResult<ChatMessageVO>> getChatHistory(ChatMessageQueryDTO query) {
        PageResult<ChatMessageVO> result = chatService.getChatHistory(query);
        return Result.success(result);
    }

    /**
     * 标记消息为已读
     */
    @Operation(summary = "标记消息为已读")
    @PostMapping("/read")
    public Result<Void> markAsRead(@RequestParam String conversationId, 
                                  @RequestParam Long receiverId) {
        chatService.markAsRead(conversationId, receiverId);
        return Result.success();
    }
}
