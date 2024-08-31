package spring.security.jwt.service;

import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.dto.comment.CommentDTO;
import spring.security.jwt.model.dto.post.PostDTO;
import spring.security.jwt.model.request.comment.CommentRequest;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;

public interface CommentService {

    IamResponse<CommentDTO> getCommentById(@NotNull Integer commentId);

    IamResponse<CommentDTO> createComment(@NotNull CommentRequest commentRequest, String username);

    IamResponse<CommentDTO> updateComment(@NotNull Integer commentId, @NotNull CommentRequest commentRequest, String username);

    IamResponse<PostDTO> softDelete(Integer userId, String username);

    IamResponse<LinkedList<CommentDTO>> findAllCommentsByUser();

    IamResponse<LinkedList<CommentDTO>> findAllComments();

}
