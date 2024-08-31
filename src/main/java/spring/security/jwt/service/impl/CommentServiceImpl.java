package spring.security.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.jwt.kafka.MessageProducer;
import spring.security.jwt.kafka.service.KafkaMessageService;
import spring.security.jwt.mapper.CommentMapper;
import spring.security.jwt.mapper.PostMapper;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.dto.comment.CommentDTO;
import spring.security.jwt.model.dto.post.PostDTO;
import spring.security.jwt.model.entities.Comment;
import spring.security.jwt.model.entities.Post;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.exception.NotFoundException;
import spring.security.jwt.model.request.comment.CommentRequest;
import spring.security.jwt.repositories.CommentRepository;
import spring.security.jwt.repositories.PostRepository;
import spring.security.jwt.repositories.UserRepository;
import spring.security.jwt.security.validation.AccessValidator;
import spring.security.jwt.service.CommentService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedList;

import static spring.security.jwt.service.impl.PostServiceImpl.getUserDetails;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final AccessValidator accessValidator;
    private final PostMapper postMapper;
    private final MessageProducer messageProducer;
    private final KafkaMessageService kafkaMessageService;

    @Override
    @Transactional(readOnly = true)
    public IamResponse<CommentDTO> getCommentById(@NotNull Integer commentId) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_COMMENT_ID.getMessage(commentId)));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails currentUser = loadUserByUsername(username);

        accessValidator.validateAdminOrOwnerAccess(currentUser, comment.getUser().getUsername(), comment.getUser().getCreatedBy(), username,
                ApiErrorMessage.HAVE_NO_ACCESS_TO_VIEW_COMMENT.getMessage(commentId));

        CommentDTO commentDto = commentMapper.toDto(comment);
        return IamResponse.createSuccessful(commentDto);
    }

    @Transactional
    public IamResponse<CommentDTO> createComment(@NotNull CommentRequest commentRequest, @NotNull String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_USER_NAME.getMessage(username)));

        Post post = postRepository.findByIdAndDeletedFalse(commentRequest.getPostId())
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_POST_ID.getMessage(commentRequest.getPostId())));

        accessValidator.validateCommentCreationAccess(userDetailsService.loadUserByUsername(username), post.getUser().getUsername(),
                ApiErrorMessage.HAVE_NO_ACCESS_TO_CREATE_COMMENT.getMessage(commentRequest.getPostId()));

        Comment comment = new Comment();
        comment.setMessage(commentRequest.getMessage());
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreated(LocalDateTime.now());
        comment.setUpdated(LocalDateTime.now());
        comment.setCreatedBy(username);
        comment = commentRepository.save(comment);

        post.incrementCommentsCount();
        postRepository.save(post);

        // Отправка сообщения в Kafka
        kafkaMessageService.sendCommentCreatedMessage(user.getId(), comment.getId());

        CommentDTO commentDto = commentMapper.toDto(comment);
        return IamResponse.createSuccessful(commentDto);
    }

    @Override
    @Transactional
    public IamResponse<CommentDTO> updateComment(@NotNull Integer commentId, @NotNull CommentRequest commentRequest, String username) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_COMMENT_ID.getMessage(commentId)));

        accessValidator.validateAdminOrOwnerAccess(loadUserByUsername(username), comment.getUser().getUsername(), comment.getUser().getCreatedBy(), username,
                ApiErrorMessage.HAVE_NO_ACCESS_TO_UPDATE_COMMENT.getMessage(commentRequest.getPostId()));

        if (commentRequest.getPostId() != null) {
            Post post = postRepository.findByIdAndDeletedFalse(commentRequest.getPostId())
                    .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_POST_ID.getMessage(commentRequest.getPostId())));
            comment.setPost(post);
        }

        commentMapper.update(comment, commentRequest);
        comment.setUpdated(LocalDateTime.now());
        comment = commentRepository.save(comment);

        // Отправка сообщения в Kafka
        kafkaMessageService.sendCommentUpdatedMessage(comment.getUser().getId(), comment.getId(), comment.getMessage());

        return IamResponse.createSuccessful(commentMapper.toDto(comment));
    }

    @Override
    @Transactional
    public IamResponse<PostDTO> softDelete(@NotNull Integer commentId, @NotNull String username) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_COMMENT_ID.getMessage(commentId)));

        UserDetails currentUser = userDetailsService.loadUserByUsername(username);

        accessValidator.validateAdminOrOwnerAccess(currentUser, comment.getUser().getUsername(), comment.getCreatedBy(), username,
                ApiErrorMessage.HAVE_NO_ACCESS_TO_DELETE.getMessage(commentId));

        comment.setDeleted(true);
        commentRepository.save(comment);

        Post post = comment.getPost();
        post.decrementCommentsCount();
        postRepository.save(post);

        // Отправка сообщения в Kafka
        kafkaMessageService.sendCommentDeletedMessage(comment.getUser().getId(), comment.getId(), comment.getMessage());

        PostDTO postDto = postMapper.toDto(post);
        return IamResponse.createSuccessful(postDto);
    }

    @Transactional(readOnly = true)
    public IamResponse<LinkedList<CommentDTO>> findAllCommentsByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_USER_NAME.getMessage(username)));

        LinkedList<Comment> comments = commentRepository.findAllByUserIdAndDeletedFalse(user.getId());
        LinkedList<CommentDTO> commentDto = commentMapper.toDtoList(comments);
        return IamResponse.createSuccessful(commentDto);
    }

    @Override
    @Transactional(readOnly = true)
    public IamResponse<LinkedList<CommentDTO>> findAllComments() {
        LinkedList<Comment> comments = commentRepository.findAllByDeletedFalse();
        LinkedList<CommentDTO> commentDtoList = commentMapper.toDtoList(comments);
        return IamResponse.createSuccessful(commentDtoList);
    }

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserDetails(username, userRepository);
    }
}
