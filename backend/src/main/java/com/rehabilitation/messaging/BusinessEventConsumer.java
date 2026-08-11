package com.rehabilitation.messaging;

import com.rehabilitation.constant.KafkaTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BusinessEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(BusinessEventConsumer.class);

    @KafkaListener(topics = KafkaTopics.CHAT_EVENT, groupId = "rehab-chat-event-group")
    public void consumeChatEvent(String message) {
        log.info("[Kafka][CHAT_EVENT] {}", message);
    }

    @KafkaListener(topics = KafkaTopics.PRESCRIPTION_EVENT, groupId = "rehab-prescription-event-group")
    public void consumePrescriptionEvent(String message) {
        log.info("[Kafka][PRESCRIPTION_EVENT] {}", message);
    }

    @KafkaListener(topics = KafkaTopics.ASSESSMENT_EVENT, groupId = "rehab-assessment-event-group")
    public void consumeAssessmentEvent(String message) {
        log.info("[Kafka][ASSESSMENT_EVENT] {}", message);
    }
}
