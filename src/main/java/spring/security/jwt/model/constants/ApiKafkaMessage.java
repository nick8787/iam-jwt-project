package spring.security.jwt.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiKafkaMessage {

    USER_CREATED("User '%s' created by: '%s'"),
    USER_UPDATED("User '%s' was updated by userId: %s"),
    USER_DELETED("User '%s 'deleted by userId: %s"),
    POST_CREATED("Post created by userId: %s, postId: %s"),
    POST_UPDATED("Post updated with postId: %s, by userId: %s"),
    POST_DELETED("Post %s was deleted by: '%s'"),
    COMMENT_CREATED("Comment created by userId: %s, commentId: %s"),
    COMMENT_UPDATED("Comment: '%s' was updated by userId: %s, commentId: %s"),
    COMMENT_DELETED("Comment: '%s' was deleted by userId: %s, commentId: %s"),
    ;

    private final String value;

    public String getMessage(Object... args) {
        return String.format(value, args);
    }
}
