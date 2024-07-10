//package spring.security.jwt.kafka;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.annotation.Validated;
//import spring.security.jwt.kafka.model.out.IamOutMessage;
//import spring.security.jwt.kafka.model.utils.ICASAService;
//import spring.security.jwt.kafka.model.utils.UtilMessage;
//
//import javax.validation.Valid;
//import javax.validation.constraints.NotNull;
//
//@Slf4j
//@Component
//@Validated
//@RequiredArgsConstructor
//public class MessageProducer {
//    private final ObjectMapper objectMapper;
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    @Value(value = "${additional.kafka.topic.iam.service.out:iam_out}")
//    private String iamOut;
//
//    @Value(value = "${additional.kafka.topic.iam.service.logs}")
//    private String logsOutTopic;
//    @Value("${kafka.enabled:false}")
//    private boolean isKafkaEnabled;
//
//    public void sendToIamOut(@NotNull @Valid IamOutMessage message) {
//        if (!isKafkaEnabled) {
//            log.trace("Kafka is not enabled. Message will not be placed in iam_out topic [message={}] ", message);
//            return;
//        }
//        try {
//            String messageJson = objectMapper.writeValueAsString(message);
//            kafkaTemplate.send(iamOut, messageJson);
//            log.debug("Kafka {} message sent. Topic: 'iam_out', message='{}' ", message.getMessageType(), messageJson);
//        } catch (Exception cause) {
//            log.error("Kafka message didn't send. ", cause);
//        }
//    }
//
//    public void sendLogs(@NotNull @Valid UtilMessage message) {
//        if (!isKafkaEnabled) {
//            log.trace("Kafka is not enabled. Message will not be placed in iam_logs topic [message={}] ", message);
//            return;
//        }
//        try {
//            message.setService(ICASAService.IAM_SERVICE);
//            String messageJson = objectMapper.writeValueAsString(message);
//            kafkaTemplate.send(logsOutTopic, messageJson);
//            log.debug("Kafka {} message sent. Topic: 'iam_logs', message='{}' ", message.getActionType(), messageJson);
//        } catch (Exception cause) {
//            log.error("Kafka message didn't send. ", cause);
//        }
//    }
//
//}
