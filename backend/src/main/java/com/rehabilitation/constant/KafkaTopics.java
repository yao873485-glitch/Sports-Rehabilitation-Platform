package com.rehabilitation.constant;

/**
 * Kafka topics.
 */
public final class KafkaTopics {

    private KafkaTopics() {
    }

    public static final String CONTENT_CONFIG_LOG = "rehab.content-config.log";
    public static final String CHAT_EVENT = "rehab.chat.event";
    public static final String PRESCRIPTION_EVENT = "rehab.prescription.event";
    public static final String ASSESSMENT_EVENT = "rehab.assessment.event";
}
