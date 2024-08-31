package spring.security.jwt.mapper;

import org.mapstruct.*;
import spring.security.jwt.model.dto.post.PostDTO;
import spring.security.jwt.model.entities.Post;
import spring.security.jwt.model.request.post.PostRequest;
import spring.security.jwt.utils.DateTimeUtils;

import java.util.LinkedList;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {DateTimeUtils.class, Objects.class}
)
public interface PostMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "likes", target = "likes")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "user.id", target = "owner.id")
    @Mapping(source = "user.username", target = "owner.username")
    @Mapping(source = "user.email", target = "owner.email")
    @Mapping(source = "commentsCount", target = "commentsCount")
    PostDTO toDto(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Post create(PostRequest postRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Post update(@MappingTarget Post post, PostRequest request);

    LinkedList<PostDTO> toDtoList(LinkedList<Post> posts);
}

