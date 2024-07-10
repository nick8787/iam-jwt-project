package spring.security.jwt.service;

import spring.security.jwt.model.entities.RefreshToken;
import spring.security.jwt.model.entities.User;

public interface RefreshTokenService {

    RefreshToken refreshToken(String refreshToken);

    RefreshToken refreshToken(User user);

}
