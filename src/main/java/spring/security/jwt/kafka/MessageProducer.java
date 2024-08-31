package spring.security.jwt.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import spring.security.jwt.kafka.model.utils.PostWaveService;
import spring.security.jwt.kafka.model.utils.UtilMessage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Component
@Validated
@RequiredArgsConstructor
public class MessageProducer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${additional.kafka.topic.iam.service.logs}")
    private String logsOutTopic;

    @Value("${kafka.enabled:false}")
    private boolean isKafkaEnabled;

    public void sendLogs(@NotNull @Valid UtilMessage message) {
        if (!isKafkaEnabled) {
            log.trace("Kafka is not enabled. Message will not be placed in iam_logs topic [message={}] ", message);
            return;
        }
        try {
            message.setService(PostWaveService.IAM_SERVICE);
            String messageJson = objectMapper.writeValueAsString(message);
            log.debug("Sending message to Kafka: {}", messageJson);
            kafkaTemplate.send(logsOutTopic, messageJson).get();
            log.debug("Kafka {} message sent. Topic: '{}', message='{}'", message.getActionType(), logsOutTopic, messageJson);
        } catch (Exception cause) {
            log.error("Kafka message didn't send. ", cause);
        }
    }
}
