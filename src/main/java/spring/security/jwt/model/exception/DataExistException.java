package spring.security.jwt.model.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataExistException extends RuntimeException {

    public DataExistException(String message) {
        super(message);
    }

}
