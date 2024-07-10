package spring.security.jwt.kafka.model.out;

import lombok.Data;

import java.io.Serializable;

@Data
public class IamOutMessage implements Serializable {
    private MessageOutType messageType;

}
