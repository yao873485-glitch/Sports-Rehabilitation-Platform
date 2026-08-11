package com.rehabilitation.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rehabilitation.constant.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class BusinessEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public BusinessEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishContentConfigLog(String contentId, String contentType, String operationType,
                                        String operationContent, String operator) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contentId", contentId);
        payload.put("contentType", contentType);
        payload.put("operationType", operationType);
        payload.put("operationContent", operationContent);
        payload.put("operator", operator);
        payload.put("operationTime", LocalDateTime.now().toString());
        publish(KafkaTopics.CONTENT_CONFIG_LOG, contentId, payload);
    }

    public void publishChatEvent(String eventType, String conversationId, Long senderId, Long receiverId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", eventType);
        payload.put("conversationId", conversationId);
        payload.put("senderId", senderId);
        payload.put("receiverId", receiverId);
        payload.put("eventTime", LocalDateTime.now().toString());
        publish(KafkaTopics.CHAT_EVENT, String.valueOf(receiverId), payload);
    }

    public void publishPrescriptionEvent(String eventType, Integer prescriptionId, Integer patientId, String executor) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", eventType);
        payload.put("prescriptionId", prescriptionId);
        payload.put("patientId", patientId);
        payload.put("executor", executor);
        payload.put("eventTime", LocalDateTime.now().toString());
        publish(KafkaTopics.PRESCRIPTION_EVENT, String.valueOf(prescriptionId), payload);
    }

    public void publishAssessmentEvent(String eventType, Integer assessmentPlanId, Integer patientId, String executor) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", eventType);
        payload.put("assessmentPlanId", assessmentPlanId);
        payload.put("patientId", patientId);
        payload.put("executor", executor);
        payload.put("eventTime", LocalDateTime.now().toString());
        publish(KafkaTopics.ASSESSMENT_EVENT, String.valueOf(assessmentPlanId), payload);
    }

    private void publish(String topic, String key, Map<String, Object> payload) {
        try {
            kafkaTemplate.send(topic, key, objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化Kafka消息失败", e);
        }
    }
}
