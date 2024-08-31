package spring.security.jwt.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.security.jwt.kafka.MessageProducer;
import spring.security.jwt.kafka.model.utils.ActionType;
import spring.security.jwt.kafka.model.utils.PriorityType;
import spring.security.jwt.kafka.model.utils.UtilMessage;
import spring.security.jwt.model.constants.ApiKafkaMessage;

@Service
@RequiredArgsConstructor
public class KafkaMessageService {
    private final MessageProducer messageProducer;

    /* UserServiceImpl messages */
    public void sendUserCreatedMessage(Integer userId, String username, String createdBy) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.CREATE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.USER_CREATED.getMessage(username, createdBy))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendUserUpdatedMessage(Integer userId, String username) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.UPDATE)
                .priorityType(PriorityType.MEDIUM)
                .message(ApiKafkaMessage.USER_UPDATED.getMessage(username, userId))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendUserDeletedMessage(Integer userId, String username) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.DELETE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.USER_DELETED.getMessage(username, userId))
                .build();
        messageProducer.sendLogs(message);
    }

    /* PostServiceImpl messages */
    public void sendPostCreatedMessage(Integer userId, Integer postId) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.CREATE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.POST_CREATED.getMessage(userId, postId))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendPostUpdatedMessage(Integer userId, Integer postId) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.UPDATE)
                .priorityType(PriorityType.MEDIUM)
                .message(ApiKafkaMessage.POST_UPDATED.getMessage(postId, userId))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendPostDeletedMessage(Integer userId, Integer postId, String username) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.DELETE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.POST_DELETED.getMessage(postId, username))
                .build();
        messageProducer.sendLogs(message);
    }

    /* PostServiceImpl messages */
    public void sendCommentCreatedMessage(Integer userId, Integer commentId) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.CREATE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.COMMENT_CREATED.getMessage(userId, commentId))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendCommentUpdatedMessage(Integer userId, Integer commentId, String commentMessage) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.UPDATE)
                .priorityType(PriorityType.MEDIUM)
                .message(ApiKafkaMessage.COMMENT_UPDATED.getMessage(commentMessage, userId, commentId))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendCommentDeletedMessage(Integer userId, Integer commentId, String commentMessage) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.DELETE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.COMMENT_DELETED.getMessage(commentMessage, userId, commentId))
                .build();
        messageProducer.sendLogs(message);
    }
}
