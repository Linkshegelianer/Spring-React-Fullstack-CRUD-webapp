package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface UserService {

    User createNewUser(@Valid UserDto userDto);

    User updateUser(long id, UserDto userDto);

    String getCurrentUserName();

    User getCurrentUser();

    User findUserById(long id);

    List<User> findAllUsers();

    void deleteUser(long id, UserDetails authDetails);
}

