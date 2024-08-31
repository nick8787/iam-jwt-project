package spring.security.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.security.jwt.model.constants.ApiLogMessage;
import spring.security.jwt.model.request.user.RegistrationUserRequest;
import spring.security.jwt.model.dto.user.UserDTO;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.request.user.UpdateUserRequest;
import spring.security.jwt.service.UserService;
import spring.security.jwt.utils.ApiUtils;

import javax.validation.Valid;
import java.security.Principal;
import java.util.LinkedList;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("${end.points.users}")
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    @Operation(summary = "Get user info", description = "Ready to use")
    public ResponseEntity<IamResponse<UserDTO>> userData(Principal principal) {
        IamResponse<UserDTO> response = userService.getUserInfo(principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("${end.points.id}")
    @Operation(summary = "Get user by Id", description = "Ready to use")
    public ResponseEntity<IamResponse<UserDTO>> getUserById(
            @PathVariable(name = "id") Integer userId) {

        IamResponse<UserDTO> response = userService.getById(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.create}")
    @Operation(summary = "Create new user", description = "Ready to use")
    public ResponseEntity<IamResponse<UserDTO>> addNewUser(
            @RequestBody @Valid RegistrationUserRequest registrationUserRequest, Principal principal) {

        String creatorUsername = principal.getName();
        IamResponse<UserDTO> response = userService.saveNewUser(registrationUserRequest, creatorUsername);
        return ResponseEntity.ok(response);
    }

    @PutMapping("${end.points.id}")
    @Operation(summary = "Update user by Id", description = "Ready to use")
    public ResponseEntity<IamResponse<UserDTO>> updateUser(
            @PathVariable(name = "id") Integer userId,
            @RequestBody @Valid UpdateUserRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        IamResponse<UserDTO> response = userService.updateUser(userId, request, username);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("${end.points.id}")
    @Operation(summary = "Delete user by id", description = "Ready to use")
    public ResponseEntity<Void> softDeleteUser(
            @PathVariable(name = "id") Integer userId, Principal principal
    ) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        userService.softDelete(userId, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Get list of users [only for ADMIN]")
    public ResponseEntity<IamResponse<LinkedList<UserDTO>>> getAllUsers() {
        IamResponse<LinkedList<UserDTO>> response = userService.findAllUsers();
        return ResponseEntity.ok(response);
    }

}
