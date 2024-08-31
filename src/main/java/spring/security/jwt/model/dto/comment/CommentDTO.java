package spring.security.jwt.model.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.security.jwt.model.dto.post.OwnerDTO;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO implements Serializable {

    private Integer id;
    private String message;
    private OwnerDTO owner;
    private Integer postId;
    private LocalDateTime created;
    private LocalDateTime updated;

}

