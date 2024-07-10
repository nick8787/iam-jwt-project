//package spring.security.jwt.kafka;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.security.core.token.TokenService;
//import org.springframework.stereotype.Component;
//import spring.security.jwt.kafka.model.in.IamInMessage;
//
//import static spring.security.jwt.kafka.model.in.MessageInType.ROAMING_TOKEN;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class MessageConsumer {
//    private final ObjectMapper objectMapper;
//
//
//    @KafkaListener(topics = "${additional.kafka.topic.ocpi.out}", groupId = "${spring.kafka.consumer.group-id}")
//    public void consumeMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
//        String value = null;
//        try {
//            value = consumerRecord.value();
//            IamInMessage message = objectMapper.readValue(value, IamInMessage.class);
//            switch (message.getMessageType()) {
//                case ROAMING_TOKEN:
//                    break;
//                default:
//                    log.warn("UNDEFINED central message type. Message: {}", value);
//                    break;
//            }
//        }catch (Exception e) {
//            log.warn("Iam consumer has undefined error. Message '{}'", value, e);
//        }
//
//        acknowledgment.acknowledge();
//    }
//
//}
