package spring.security.jwt.service;

import spring.security.jwt.model.request.user.RegistrationUserRequest;
import spring.security.jwt.model.request.user.LoginRequest;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.response.UserProfileDto;

public interface AuthService {

    IamResponse<UserProfileDto> login(LoginRequest request);

    IamResponse<UserProfileDto> refreshTokens(String refreshToken);

    IamResponse<UserProfileDto> registerUser(RegistrationUserRequest registrationUserRequest);

}
