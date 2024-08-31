package spring.security.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.jwt.kafka.MessageProducer;
import spring.security.jwt.kafka.service.KafkaMessageService;
import spring.security.jwt.mapper.PostMapper;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.dto.post.PostDTO;
import spring.security.jwt.model.request.post.PostRequest;
import spring.security.jwt.model.entities.Post;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.exception.DataExistException;
import spring.security.jwt.model.exception.NotFoundException;
import spring.security.jwt.repositories.PostRepository;
import spring.security.jwt.repositories.UserRepository;
import spring.security.jwt.security.validation.AccessValidator;
import spring.security.jwt.service.PostService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final AccessValidator accessValidator;
    private final MessageProducer messageProducer;
    private final KafkaMessageService kafkaMessageService;

    @Override
    @Transactional(readOnly = true)
    public IamResponse<PostDTO> getById(@NotNull Integer postId) {
        Post post = postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_POST_ID.getMessage(postId)));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails currentUser = loadUserByUsername(username);

        // Проверка прав на просмотр поста
        accessValidator.validateViewAccess(currentUser, post.getUser().getUsername(), post.getCreatedBy());

        PostDTO postDto = postMapper.toDto(post);
        return IamResponse.createSuccessful(postDto);
    }

    @Transactional
    public IamResponse<PostDTO> createPost(@NotNull PostRequest postRequest, String username) {
        if (postRepository.existsByTitle(postRequest.getTitle())) {
            throw new DataExistException(ApiErrorMessage.POST_ALREADY_EXISTS.getMessage(postRequest.getTitle()));
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ApiErrorMessage.NOT_FOUND_USER_NAME.getMessage()));

        Post post = postMapper.create(postRequest);
        post.setUser(user);
        post.setCreatedBy(username);
        post.setCreated(LocalDateTime.now());
        post = postRepository.save(post);

        // Отправка сообщения в Kafka
        kafkaMessageService.sendPostCreatedMessage(user.getId(), post.getId());

        PostDTO postDto = postMapper.toDto(post);
        return IamResponse.createSuccessful(postDto);
    }

    @Override
    @Transactional
    public IamResponse<PostDTO> updatePost(@NotNull Integer postId, @NotNull PostRequest request, String username) {
        Post post = postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_POST_ID.getMessage(postId)));

        UserDetails currentUser = loadUserByUsername(username);
        User postUser = post.getUser();

        accessValidator.validateAdminOrOwnerAccess(currentUser, postUser.getUsername(), post.getCreatedBy(), username,
                ApiErrorMessage.HAVE_NO_ACCESS_TO_UPDATE.getMessage(postId));

        postMapper.update(post, request);
        post.setUpdated(LocalDateTime.now());
        post = postRepository.save(post);

        // Отправка сообщения в Kafka
        kafkaMessageService.sendPostUpdatedMessage(postUser.getId(), post.getId());

        PostDTO postDto = postMapper.toDto(post);
        return IamResponse.createSuccessful(postDto);
    }

    @Override
    @Transactional
    public void softDelete(Integer postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_POST_ID.getMessage(postId)));

        UserDetails currentUser = loadUserByUsername(username);

        accessValidator.validateAdminOrOwnerAccess(currentUser, post.getUser().getUsername(), post.getCreatedBy(), username,
                ApiErrorMessage.HAVE_NO_ACCESS_TO_DELETE.getMessage(postId));

        post.setDeleted(true);
        post.getComments().clear();
        postRepository.save(post);

        // Отправка сообщения в Kafka
        kafkaMessageService.sendPostDeletedMessage(post.getUser().getId(), postId, username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserDetails(username, userRepository);
    }

    static UserDetails getUserDetails(String username, UserRepository userRepository) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ApiErrorMessage.NOT_FOUND_USER_NAME.getMessage(username)));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

    @Transactional(readOnly = true)
    public IamResponse<LinkedList<PostDTO>> findAllPostsByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_USER_NAME.getMessage(username)));

        LinkedList<Post> posts = postRepository.findAllByUserIdAndDeletedFalse(user.getId());
        LinkedList<PostDTO> postDto = postMapper.toDtoList(posts);
        return IamResponse.createSuccessful(postDto);
    }

    @Override
    @Transactional(readOnly = true)
    public IamResponse<LinkedList<PostDTO>> findAllPosts() {
        LinkedList<Post> posts = postRepository.findAllByDeletedFalse();
        LinkedList<PostDTO> postDto = postMapper.toDtoList(posts);
        return IamResponse.createSuccessful(postDto);
    }
}
