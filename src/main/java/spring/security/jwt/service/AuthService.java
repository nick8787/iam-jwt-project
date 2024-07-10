package spring.security.jwt.service;

import spring.security.jwt.model.dto.user.LoginRequest;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.response.UserProfileDto;

public interface AuthService {

    IamResponse<UserProfileDto> login(LoginRequest request);

    IamResponse<UserProfileDto> refreshTokens(String refreshToken);

}
