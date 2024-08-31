package spring.security.jwt.model.request.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @NotNull(message = "Post ID cannot be null")
    private Integer postId;

    @NotBlank(message = "Content cannot be empty")
    private String message;

}
