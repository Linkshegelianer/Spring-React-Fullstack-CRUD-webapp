package hexlet.code.controllers;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static hexlet.code.controllers.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public static final String USER_CONTROLLER_PATH = "/api/users";
    public static final String ID = "/{id}";

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(ID)
    public User getUserById(@PathVariable final long id) {
        User existedUser = userService.findUserById(id);
        return existedUser;
    }


    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all users"),
    })
    @GetMapping
    public List<User> getAllUsers() {
        List<User> existedUsers = userService.findAllUsers();
        return existedUsers.stream()
                .collect(Collectors.toList());
    }

    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered"),
            @ApiResponse(responseCode = "404", description = "User with that id not found"),
            @ApiResponse(responseCode = "422", description = "User data is incorrect"),
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public User registerUser(@RequestBody @Valid final UserDto dto) {
        return userService.createNewUser(dto);
    }

    @Operation(summary = "Update user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "403", description = "Access denied for this user"),
            @ApiResponse(responseCode = "404", description = "User with that id not found"),
            @ApiResponse(responseCode = "422", description = "User data is incorrect")
    })
    @PutMapping(ID)
    public User updateUser(@PathVariable final long id, @RequestBody @Valid final UserDto dto) {
        return userService.updateUser(id, dto);
    }

    @Operation(summary = "Delete user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "403", description = "Access denied for this user"),
            @ApiResponse(responseCode = "404", description = "User with that id not found")
    })
    @DeleteMapping(ID)
    public void deleteUser(@PathVariable final long id, @AuthenticationPrincipal UserDetails authDetails) {
        userService.deleteUser(id, authDetails);
    }
}
