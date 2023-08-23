package hexlet.code.service;

import hexlet.code.domain.mapper.UserModelMapper;
import hexlet.code.domain.dto.UserRequestDTO;
import hexlet.code.domain.model.User;
import hexlet.code.exception.NotTheOwnerException;
import hexlet.code.exception.NotFoundException;
import hexlet.code.exception.UserAlreadyExistException;
import hexlet.code.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface UserService {

    public List<User> findAllUsers();

    public User findUserById(Long id);

    public User getUserReferenceById(long id);

    public User findUserByEmail(String email);

    @Transactional
    public User createUser(UserRequestDTO dto);

    @Transactional
    public User updateUser(long id, UserRequestDTO dto, UserDetails authDetails);

    @Transactional
    public void deleteUser(long id, UserDetails authDetails);
}
