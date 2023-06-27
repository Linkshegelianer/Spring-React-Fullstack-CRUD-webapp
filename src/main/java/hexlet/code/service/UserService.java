package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import jakarta.validation.Valid;


public interface UserService {

    User createNewUser(@Valid UserDto userDto);

    User updateUser(long id, UserDto userDto);

    String getCurrentUserName();

    User getCurrentUser();
}

