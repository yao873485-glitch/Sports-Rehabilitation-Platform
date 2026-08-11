package com.rehabilitation.config;

import com.rehabilitation.constant.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic contentConfigLogTopic() {
        return TopicBuilder.name(KafkaTopics.CONTENT_CONFIG_LOG).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic chatEventTopic() {
        return TopicBuilder.name(KafkaTopics.CHAT_EVENT).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic prescriptionEventTopic() {
        return TopicBuilder.name(KafkaTopics.PRESCRIPTION_EVENT).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic assessmentEventTopic() {
        return TopicBuilder.name(KafkaTopics.ASSESSMENT_EVENT).partitions(3).replicas(1).build();
    }
}
