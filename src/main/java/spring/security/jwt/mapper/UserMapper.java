package spring.security.jwt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import spring.security.jwt.model.request.user.RegistrationUserRequest;
import spring.security.jwt.model.dto.user.UserDTO;
import spring.security.jwt.model.entities.Role;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.enums.RegistrationStatus;
import spring.security.jwt.model.request.user.UpdateUserRequest;
import spring.security.jwt.model.response.UserProfileDto;
import spring.security.jwt.utils.DateTimeUtils;

import java.util.List;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {RegistrationStatus.class, Objects.class, DateTimeUtils.class}
)
public interface UserMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "lastLogin", expression = "java(java.time.LocalDateTime.now())")
    UserDTO toDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "registrationStatus", expression = "java(RegistrationStatus.ACTIVE)")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    @Mapping(target = "lastUpdate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "username")
    User fromDto(RegistrationUserRequest dto);

    @Mapping(target = "lastLogin", expression = "java(user.getLastLogin())")
    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(role -> new spring.security.jwt.model.enums.RoleDto(role.getId(), role.getName())).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "refreshToken", source = "refreshToken")
    UserProfileDto toUserProfileDto(User user, String token, String refreshToken);

    @Mapping(target = "token", source = "token")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(role -> new spring.security.jwt.model.enums.RoleDto(role.getId(), role.getName())).collect(java.util.stream.Collectors.toList()))")
    UserProfileDto map(User user, List<Role> roles, String token, String refreshToken);

    @Mapping(target = "lastUpdate", expression = "java(java.time.LocalDateTime.now())")
    User update(@MappingTarget User user, UpdateUserRequest request);
}
