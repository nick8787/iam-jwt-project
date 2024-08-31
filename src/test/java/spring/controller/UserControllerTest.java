package spring.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import spring.security.jwt.IamServiceApplication;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.request.user.RegistrationUserRequest;
import spring.security.jwt.model.dto.user.UserDTO;
import spring.security.jwt.model.enums.RegistrationStatus;
import spring.security.jwt.service.UserService;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(classes = IamServiceApplication.class)
@ActiveProfiles("test")
public class UserControllerTest {

    // интеграции MockMvc для тестирования веб-слоя без необходимости запуска всего приложения
    @Autowired
    private MockMvc mockMvc;

    // создание мока сервиса UserService
    @MockBean
    private UserService userService;

    @Test
    @SneakyThrows
    // имитация реального юзера
    @WithMockUser(username = "user", authorities = {"USER"})
    public void testGetAllUsers_AsUser_ShouldReturn_notOK_403() {
        // имитация HTTP запросов
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetAllUsers_AsAdmin_ShouldReturn_OK_200() {
        LinkedList<UserDTO> users = new LinkedList<>();
        IamResponse<LinkedList<UserDTO>> response = IamResponse.createSuccessful(users);
        Mockito.when(userService.findAllUsers()).thenReturn(response);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "super_admin", authorities = {"SUPER_ADMIN"})
    public void testGetAllUsers_AsSuperAdmin_ShouldReturn_OK_200() {
        LinkedList<UserDTO> users = new LinkedList<>();
        IamResponse<LinkedList<UserDTO>> response = IamResponse.createSuccessful(users);
        Mockito.when(userService.findAllUsers()).thenReturn(response);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    public void testAddNewUser_ShouldReturn_200_OK() throws Exception {
        UserDTO userDto = new UserDTO(1, "new_user", "new_user@example.com",
                "1234567890", LocalDateTime.now(), RegistrationStatus.ACTIVE, List.of());
        IamResponse<UserDTO> response = IamResponse.createSuccessful(userDto);
        Mockito.when(userService.saveNewUser(Mockito.any(RegistrationUserRequest.class), Mockito.anyString())).thenReturn(response);

        mockMvc.perform(post("/users/create")
                        .contentType("application/json")
                        .content("{\"username\": \"new_user\", \"password\": \"password\"," +
                                " \"confirmPassword\": \"password\", \"email\": \"new_user@example.com\", " +
                                "\"phoneNumber\": \"1234567890\"}"))
                .andExpect(status().isOk());
    }

}
