package vn.devTool.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import vn.devTool.core.utils.JsonUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Gửi một object đến Kafka topic tương ứng.
     *
     * @param topic  tên topic Kafka
     * @param data   object cần gửi (sẽ được serialize)
     */
    public void sendMessage(String topic, Object data) {
        try {
            kafkaTemplate.send(topic, JsonUtils.toJson(data));
            log.info("Sent message to topic [{}]: {}", topic, data);
        } catch (Exception ex) {
            log.error("Failed to send message to topic [{}]: {}", topic, data, ex);
            throw new RuntimeException("Kafka send failed", ex);
        }
    }
}
