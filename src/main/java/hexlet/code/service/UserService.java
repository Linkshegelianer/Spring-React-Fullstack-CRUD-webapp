package hexlet.code.service;

import hexlet.code.domain.dto.UserRequestDTO;
import hexlet.code.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface UserService {

    @Transactional
    public User createUser(UserRequestDTO dto);

    public List<User> getAllUsers();

    public User getUserById(Long id);

    public User getUserReferenceById(long id);

    public User getUserByEmail(String email);

    @Transactional
    public User updateUser(long id, UserRequestDTO dto, UserDetails authDetails);

    @Transactional
    public void deleteUser(long id, UserDetails authDetails);
}
