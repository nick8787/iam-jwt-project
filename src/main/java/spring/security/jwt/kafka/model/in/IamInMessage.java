package spring.security.jwt.kafka.model.in;

import lombok.Data;

import java.io.Serializable;

@Data
public class IamInMessage implements Serializable {
    private MessageInType messageType;

}
