package spring.security.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import spring.security.jwt.model.request.user.RegistrationUserRequest;
import spring.security.jwt.model.dto.user.UserDTO;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.request.user.UpdateUserRequest;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;

public interface UserService extends UserDetailsService{

    IamResponse<UserDTO> saveNewUser(@NotNull RegistrationUserRequest request, String creatorUsername);

    IamResponse<UserDTO> getById(@NotNull Integer userId);

    IamResponse<UserDTO> updateUser(@NotNull Integer userId, @NotNull UpdateUserRequest request, String currentUsername);

    void softDelete(Integer userId, String username);

    UserDetails loadUserByUsername(String username);

    IamResponse<UserDTO> getUserInfo(String username);

    IamResponse<LinkedList<UserDTO>> findAllUsers();

}
