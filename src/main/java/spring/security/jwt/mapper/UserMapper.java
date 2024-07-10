package spring.security.jwt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import spring.security.jwt.model.dto.user.UserDto;
import spring.security.jwt.model.entities.Role;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.enums.RegistrationStatus;
import spring.security.jwt.model.response.UserProfileDto;
import spring.security.jwt.utils.DateTimeUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {RegistrationStatus.class, Objects.class, DateTimeUtils.class}
)
public interface UserMapper {

    @Mapping(target = "email", source = "email")
    UserDto toDto(User user);

    @Mapping(target = "lastLogin", expression = "java(user.getLastLogin())")
    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(role -> new spring.security.jwt.model.enums.RoleDto(role.getId(), role.getName())).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "name", source = "user.username")
    @Mapping(target = "refreshToken", source = "refreshToken")
    UserProfileDto toUserProfileDto(User user, String token, String refreshToken);

    @Mapping(target = "token", source = "token")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(role -> new spring.security.jwt.model.enums.RoleDto(role.getId(), role.getName())).collect(java.util.stream.Collectors.toList()))")
    UserProfileDto map(User user, List<Role> roles, String token, String refreshToken);

    LinkedList<UserDto> toDtoList(LinkedList<User> users);

}
