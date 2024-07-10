package spring.security.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.entities.RefreshToken;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.exception.NotFoundException;
import spring.security.jwt.repositories.RefreshTokenRepository;
import spring.security.jwt.service.RefreshTokenService;
import spring.security.jwt.utils.ApiUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RefreshToken refreshToken(String requestRefreshToken) {
        log.debug("Attempting to find refresh token with value: {}", requestRefreshToken);
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByToken(requestRefreshToken).orElseThrow(
                () -> {
                    log.error("Refresh token not found for token: {}", requestRefreshToken);
                    return new NotFoundException(ApiErrorMessage.NOT_FOUND_REFRESH_TOKEN.getMessage());
                });
        refreshToken = createOrUpdate(refreshToken);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RefreshToken refreshToken(User user) {
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUser_Id(user.getId())
                .orElseGet(() -> {
                    RefreshToken newToken = new RefreshToken();
                    newToken.setUser(user);
                    return newToken;
                });
        refreshToken.setCreated(LocalDateTime.now());
        refreshToken.setToken(ApiUtils.generateUuidWithoutDash());
        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken createOrUpdate(RefreshToken refreshToken) {
        if (Objects.isNull(refreshToken)) {
            refreshToken = new RefreshToken();
        }
        refreshToken.setCreated(LocalDateTime.now());
        refreshToken.setToken(ApiUtils.generateUuidWithoutDash());
        return refreshToken;
    }

}
