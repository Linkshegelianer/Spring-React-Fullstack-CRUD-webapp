package project.code.service;

import project.code.dto.UserRequestDTO;
import project.code.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface UserService {

    @Transactional
    User createUser(UserRequestDTO dto);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserReferenceById(long id);

    User getUserByEmail(String email);

    @Transactional
    User updateUser(long id, UserRequestDTO dto, UserDetails authDetails);

    @Transactional
    void deleteUser(long id, UserDetails authDetails);
}
