package hexlet.code.service;

import hexlet.code.domain.dto.UserRequestDTO;
import hexlet.code.domain.mapper.UserModelMapper;
import hexlet.code.domain.model.User;
import hexlet.code.exception.NotFoundException;
import hexlet.code.exception.NotTheOwnerException;
import hexlet.code.exception.UserAlreadyExistException;
import hexlet.code.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserModelMapper userMapper;
    private final PasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                       UserModelMapper userMapper,
                       PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> findAllUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public User findUserById(Long id) {
        if (id != null) {
            return userRepository.findUserById(id)
                    .orElseThrow(() -> new NotFoundException("User with id='%d' not found!".formatted(id)));
        }
        return null;
    }

    public User getUserReferenceById(long id) {
        return userRepository.getReferenceById(id);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User with email '%s' not found!".formatted(email)));
    }

    @Transactional
    public User createUser(UserRequestDTO dto) {
        if (userRepository.existsUserByEmailIgnoreCase(dto.getEmail())) {
            throw new UserAlreadyExistException("User already exists!");
        }
        User newUser = userMapper.toUserModel(dto);
        String encodedPassword = bCryptPasswordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        return userRepository.save(newUser);
    }

    @Transactional
    public User updateUser(long id, UserRequestDTO dto, UserDetails authDetails) {
        User userToUpdate = findUserById(id);
        validateOwnerByEmail(userToUpdate.getEmail(), authDetails);
        userToUpdate.setFirstName(dto.getFirstName());
        userToUpdate.setLastName(dto.getLastName());
        userToUpdate.setEmail(dto.getEmail());
        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        userToUpdate.setPassword(encodedPassword);
        return userToUpdate;
    }

    @Transactional
    public void deleteUser(long id, UserDetails authDetails) {
        User existedUser = findUserById(id);
        validateOwnerByEmail(existedUser.getEmail(), authDetails);
        userRepository.delete(existedUser);
    }

    private void validateOwnerByEmail(String userEmail, UserDetails authDetails) {
        String authenticatedEmail = authDetails.getUsername();
        if (!authenticatedEmail.equalsIgnoreCase(userEmail)) {
            throw new NotTheOwnerException("Access denied. For owner only!");
        }
    }
}
