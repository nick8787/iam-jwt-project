package spring.security.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import spring.security.jwt.model.dto.user.RegistrationUserDto;
import spring.security.jwt.model.dto.user.UserDto;
import spring.security.jwt.model.IamResponse;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;

public interface UserService extends UserDetailsService{

    IamResponse<UserDto> saveNewUser(@NotNull RegistrationUserDto request);

    IamResponse<UserDto> getById(@NotNull Integer userId);

    UserDetails loadUserByUsername(String username);

    IamResponse<UserDto> getUserInfo(String username);

    IamResponse<LinkedList<UserDto>> findAllUsers();

}
