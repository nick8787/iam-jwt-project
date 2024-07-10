package spring.security.jwt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.constants.ApiLogMessage;
import spring.security.jwt.model.dto.user.LoginRequest;
import spring.security.jwt.model.response.UserProfileDto;
import spring.security.jwt.service.AuthService;
import spring.security.jwt.utils.ApiUtils;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        try {
            IamResponse<UserProfileDto> response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiErrorMessage.INVALID_USER_OR_PASSWORD.getMessage());
        }
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<IamResponse<UserProfileDto>> refreshToken(
            @RequestParam(name = "token") String refreshToken
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<UserProfileDto> result = authService.refreshTokens(refreshToken);
        return ResponseEntity.ok(result);
    }

}
