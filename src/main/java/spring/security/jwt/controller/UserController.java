package spring.security.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.security.jwt.model.dto.user.RegistrationUserDto;
import spring.security.jwt.model.dto.user.UserDto;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.LinkedList;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("${end.points.users}")
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    @Operation(summary = "Get user info", description = "Ready to use")
    public ResponseEntity<IamResponse<UserDto>> userData(Principal principal) {
        IamResponse<UserDto> response = userService.getUserInfo(principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("${end.points.id}")
    @Operation(summary = "Get user by Id", description = "Ready to use")
    public ResponseEntity<IamResponse<UserDto>> getUser(
            @PathVariable(name = "id") Integer userId) {
        IamResponse<UserDto> response = userService.getById(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.create}")
    @Operation(summary = "Create new user", description = "Ready to use")
    public ResponseEntity<IamResponse<UserDto>> addNewUser(
            @RequestBody @Valid RegistrationUserDto registrationUserDto) {
        IamResponse<UserDto> response = userService.saveNewUser(registrationUserDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("@accessSecurity.hasIamAccess(#authentication.principal)")
    @Operation(summary = "Get all users (only for ADMIN)")
    public ResponseEntity<IamResponse<LinkedList<UserDto>>> getAllUsers() {
        IamResponse<LinkedList<UserDto>> response = userService.findAllUsers();
        return ResponseEntity.ok(response);
    }


}
