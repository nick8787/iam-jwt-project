package spring.security.jwt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import spring.security.jwt.model.dto.post.PostDto;
import spring.security.jwt.model.entities.Post;
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
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    PostDto toDto(Post post);

    LinkedList<PostDto> toDtoList(LinkedList<Post> posts);

}
