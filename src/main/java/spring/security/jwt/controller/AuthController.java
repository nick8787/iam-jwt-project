package spring.security.jwt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.constants.ApiLogMessage;
import spring.security.jwt.model.request.user.RegistrationUserRequest;
import spring.security.jwt.model.request.user.LoginRequest;
import spring.security.jwt.model.response.UserProfileDto;
import spring.security.jwt.service.AuthService;
import spring.security.jwt.utils.ApiUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<UserProfileDto> result = authService.login(request);
        Cookie authorizationCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(authorizationCookie);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationUserRequest request,
            HttpServletResponse response
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<UserProfileDto> result = authService.registerUser(request);
        Cookie authorizationCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(authorizationCookie);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<IamResponse<UserProfileDto>> refreshToken(
            @RequestParam(name = "token") String refreshToken,
            HttpServletResponse response
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        IamResponse<UserProfileDto> result = authService.refreshTokens(refreshToken);
        Cookie authorizationCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(authorizationCookie);

        return ResponseEntity.ok(result);
    }

}
