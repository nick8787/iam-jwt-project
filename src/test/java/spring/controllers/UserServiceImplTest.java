package spring.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.security.jwt.mapper.UserMapper;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.enums.RoleDto;
import spring.security.jwt.model.dto.user.UserDto;
import spring.security.jwt.model.entities.Role;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.enums.RegistrationStatus;
import spring.security.jwt.repositories.UserRepository;
import spring.security.jwt.service.impl.UserServiceImpl;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void getUserInfo_UserExists_ReturnsUserDto() {
        // Подготовка
        Role roleUser = new Role();
        roleUser.setId(1);
        roleUser.setName("ROLE_USER");

        User user = new User();
        user.setId(1);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setRoles(Collections.singleton(roleUser));

        RoleDto roleDto = new RoleDto(1, "ROLE_USER");
        UserDto userDto = new UserDto(1, "user1", "user1@example.com", null, RegistrationStatus.ACTIVE, Collections.singletonList(roleDto));

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Вызов
        IamResponse<UserDto> result = userService.getUserInfo("user1");

        // Проверки
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals("user1", result.getPayload().getUsername());
        assertEquals("user1@example.com", result.getPayload().getEmail());
        assertNotNull(result.getPayload().getRoles());
        assertEquals(1, result.getPayload().getRoles().size());
        assertEquals("ROLE_USER", result.getPayload().getRoles().get(0).getName());

        // Валидация вызовов
        verify(userRepository).findByUsername("user1");
        verify(userMapper).toDto(user);
    }

    @Test
    public void getUserInfo_UserDoesNotExist_ReturnsFailedResponse() {
        String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        IamResponse<UserDto> result = userService.getUserInfo(username);

        assertNotNull(result);
        assertFalse(result.isSuccess()); // Проверяем, что ответ не успешен
        assertNull(result.getPayload()); // Проверяем, что нет полезной нагрузки
        assertEquals(ApiErrorMessage.NOT_FOUND_USER_ID.getMessage(username), result.getMessage()); // Проверяем сообщение об ошибке

        // Валидация вызовов
        verify(userRepository).findByUsername(username);
        verifyNoInteractions(userMapper); // Убеждаемся, что маппер не вызывался, так как пользователя нет
    }
}
