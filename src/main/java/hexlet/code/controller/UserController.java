package hexlet.code.controller;

import hexlet.code.domain.dto.UserRequestDTO;
import hexlet.code.domain.dto.UserResponseDTO;
import hexlet.code.domain.model.User;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("${base.url}" + "/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create new user")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "User registered"),
//            @ApiResponse(responseCode = "404", description = "User with that id not found"),
//            @ApiResponse(responseCode = "422", description = "User data is incorrect"),
//    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@RequestBody @Valid UserRequestDTO dto,
                                      BindingResult bindingResult) {
        User createdUser = userService.createUser(dto);
        return toUserResponseDTO(createdUser);
    }

    @Operation(summary = "Get list of all users")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "List of all users"),
//    })
    @GetMapping
    public List<UserResponseDTO> findAllUsers() {
        List<User> existedUsers = userService.getAllUsers();
        return existedUsers.stream()
            .map(this::toUserResponseDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Get specific user by his id")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "User found"),
//        @ApiResponse(responseCode = "404", description = "User with that id not found")
//    })
    @GetMapping(path = "/{id}")
    public UserResponseDTO findUserById(@PathVariable(name = "id") long id) {
        User existedUser = userService.getUserById(id);
        return toUserResponseDTO(existedUser);
    }

    @Operation(summary = "Update user by his id")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "User updated"),
//        @ApiResponse(responseCode = "401", description = "Unauthorized request"),
//        @ApiResponse(responseCode = "403", description = "Access denied for this user"),
//        @ApiResponse(responseCode = "404", description = "User with that id not found"),
//        @ApiResponse(responseCode = "422", description = "User data is incorrect")
//    })
    @PutMapping(path = "/{id}")
    public UserResponseDTO updateUser(@RequestBody @Valid UserRequestDTO dto,
                                      @PathVariable(name = "id") long id,
                                      @AuthenticationPrincipal UserDetails authDetails) {
        User updatedUser = userService.updateUser(id, dto, authDetails);
        return toUserResponseDTO(updatedUser);
    }

    @Operation(summary = "Delete user by his id")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "User deleted"),
//        @ApiResponse(responseCode = "401", description = "Unauthorized request"),
//        @ApiResponse(responseCode = "403", description = "Access denied for this user"),
//        @ApiResponse(responseCode = "404", description = "User with that id not found")
//    })
    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable(name = "id") long id,
                           @AuthenticationPrincipal UserDetails authDetails) {
        userService.deleteUser(id, authDetails);
    }

    private UserResponseDTO toUserResponseDTO(final User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt()
        );
    }
}
