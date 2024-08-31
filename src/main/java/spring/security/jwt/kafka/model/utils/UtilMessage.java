package spring.security.jwt.kafka.model.utils;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UtilMessage implements Serializable {
    private Integer userId;
    private ActionType actionType;
    private PriorityType priorityType;
    private PostWaveService service;
    private String message;
}
