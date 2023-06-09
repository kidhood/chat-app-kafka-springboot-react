package com.kidhood.chat.kafka;

import com.kidhood.chat.constant.KafkaConstant;
import com.kidhood.chat.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaMessageProducer {
    private final KafkaTemplate<String, Message> kafkaTemplate;

    public KafkaMessageProducer(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String user, String channel, Message message) {
        String topic = KafkaConstant.KAFKA_TOPIC; // Replace with the actual Kafka topic

        // Build a key based on user and channel
        String key = user + "-" + channel;

        // Send the message to Kafka
        kafkaTemplate.send(topic, key, message);
    }
}
