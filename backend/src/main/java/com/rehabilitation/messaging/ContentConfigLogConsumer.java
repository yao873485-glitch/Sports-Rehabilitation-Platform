package com.rehabilitation.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rehabilitation.constant.KafkaTopics;
import com.rehabilitation.entity.ContentConfigOperationLog;
import com.rehabilitation.mapper.ContentConfigOperationLogMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Component
public class ContentConfigLogConsumer {

    private final ObjectMapper objectMapper;
    private final ContentConfigOperationLogMapper logMapper;

    public ContentConfigLogConsumer(ObjectMapper objectMapper, ContentConfigOperationLogMapper logMapper) {
        this.objectMapper = objectMapper;
        this.logMapper = logMapper;
    }

    @KafkaListener(topics = KafkaTopics.CONTENT_CONFIG_LOG, groupId = "rehab-content-config-log-group")
    public void consume(String message) {
        try {
            Map<String, Object> payload = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() { });
            ContentConfigOperationLog log = new ContentConfigOperationLog();
            log.setContentId(String.valueOf(payload.get("contentId")));
            log.setContentType(String.valueOf(payload.get("contentType")));
            log.setOperationType(String.valueOf(payload.get("operationType")));
            log.setOperationContent(String.valueOf(payload.get("operationContent")));
            log.setOperator(String.valueOf(payload.get("operator")));
            log.setOperationTime(parseTime(payload.get("operationTime")));
            logMapper.insert(log);
        } catch (Exception e) {
            throw new RuntimeException("消费内容配置日志消息失败", e);
        }
    }

    private LocalDateTime parseTime(Object value) {
        if (value == null) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(String.valueOf(value));
        } catch (DateTimeParseException e) {
            return LocalDateTime.now();
        }
    }
}
