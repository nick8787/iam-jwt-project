package spring.security.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.request.post.PostRequest;
import spring.security.jwt.model.dto.post.PostDTO;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;

public interface PostService {

    IamResponse<PostDTO> getById(@NotNull Integer userId);

    IamResponse<PostDTO> createPost(@NotNull PostRequest postRequest, String username);

    IamResponse<PostDTO> updatePost(@NotNull Integer postId, @NotNull PostRequest postRequest, String username);

    void softDelete(Integer userId, String username);

    IamResponse<LinkedList<PostDTO>> findAllPostsByUser();

    UserDetails loadUserByUsername(String username);

    IamResponse<LinkedList<PostDTO>> findAllPosts();

}
