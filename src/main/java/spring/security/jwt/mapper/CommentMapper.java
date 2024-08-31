package spring.security.jwt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import spring.security.jwt.model.dto.comment.CommentDTO;
import spring.security.jwt.model.entities.Comment;
import spring.security.jwt.model.request.comment.CommentRequest;
import spring.security.jwt.utils.DateTimeUtils;

import java.util.LinkedList;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {DateTimeUtils.class, Objects.class},
        uses = {DateTimeUtils.class, UserMapper.class}
)
public interface CommentMapper {

    @Mapping(source = "user.id", target = "owner.id")
    @Mapping(source = "user.username", target = "owner.username")
    @Mapping(source = "user.email", target = "owner.email")
    @Mapping(source = "post.id", target = "postId")
    CommentDTO toDto(Comment comment);

    LinkedList<CommentDTO> toDtoList(LinkedList<Comment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Comment create(CommentRequest commentRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Comment update(@MappingTarget Comment comment, CommentRequest commentRequest);

}
