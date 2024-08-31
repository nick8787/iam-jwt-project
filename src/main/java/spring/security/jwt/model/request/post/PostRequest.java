package spring.security.jwt.model.request.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest implements Serializable {

    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Content cannot be empty")
    private String content;
    @NotNull(message = "Specify the number of likes")
    private Integer likes;
//    @NotBlank(message = "Add image URL")
    private String image;

}
