package spring.security.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.jwt.mapper.PostMapper;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.dto.post.PostRequest;
import spring.security.jwt.model.dto.post.PostDto;
import spring.security.jwt.model.entities.Post;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.exception.DataExistException;
import spring.security.jwt.repositories.PostRepository;
import spring.security.jwt.repositories.UserRepository;
import spring.security.jwt.service.PostService;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional(readOnly = true)
    public IamResponse<PostDto> getById(@NotNull Integer postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            PostDto postDto = postMapper.toDto(postOptional.get());
            return IamResponse.createSuccessful(postDto);
        } else {
            return IamResponse.createFailed(ApiErrorMessage.NOT_FOUND_POST_ID.getMessage(postId));
        }
    }

    @Transactional
    public IamResponse<PostDto> createPost(@NotNull PostRequest postRequest, String username) {
        if (postRepository.existsByTitle(postRequest.getTitle())) {
            throw new DataExistException(ApiErrorMessage.POST_ALREADY_EXISTS.getMessage(postRequest.getTitle()));
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ApiErrorMessage.NOT_FOUND_USER_NAME.getMessage()));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setLikes(postRequest.getLikes());
        post.setImage(postRequest.getImage());
        post.setUser(user);
        post = postRepository.save(post);

        PostDto postDto = postMapper.toDto(post);
        return IamResponse.createSuccessful(postDto);
    }

    @Transactional(readOnly = true)
    public IamResponse<LinkedList<PostDto>> findAllPostsByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ApiErrorMessage.NOT_FOUND_USER_NAME.getMessage(username)));

        LinkedList<Post> posts = postRepository.findAllByUserId(user.getId());
        LinkedList<PostDto> postDto = postMapper.toDtoList(posts);
        return IamResponse.createSuccessful(postDto);
    }


}
