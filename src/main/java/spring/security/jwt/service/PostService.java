package spring.security.jwt.service;

import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.dto.post.PostRequest;
import spring.security.jwt.model.dto.post.PostDto;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;

public interface PostService {

    IamResponse<PostDto> getById(@NotNull Integer userId);

    IamResponse<PostDto> createPost(@NotNull PostRequest postRequest, String username);

    IamResponse<LinkedList<PostDto>> findAllPostsByUser();

}
