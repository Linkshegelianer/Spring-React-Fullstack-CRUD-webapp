package hexlet.code.service.impl;

import hexlet.code.domain.dto.UserRequestDTO;
import hexlet.code.domain.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User createUser(UserRequestDTO dto) {
        if (userRepository.existsUserByEmailIgnoreCase(dto.getEmail())) {
            throw new RuntimeException("User already exists!");
        }
        User newUser = toUserModel(dto);
        String encodedPassword = bCryptPasswordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        return userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public User getUserById(Long id) {
        if (id != null) {
            return userRepository.findUserById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User with id='%d' not found!".formatted(id)));
        }
        return null;
    }

    public User getUserReferenceById(long id) {
        return userRepository.getReferenceById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email '%s' not found!".formatted(email)));
    }

    @Transactional
    public User updateUser(long id, UserRequestDTO dto, UserDetails authDetails) {
        User userToUpdate = getUserById(id);
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
        User existedUser = getUserById(id);
        validateOwnerByEmail(existedUser.getEmail(), authDetails);
        userRepository.delete(existedUser);
    }

    private void validateOwnerByEmail(String userEmail, UserDetails authDetails) {
        String authenticatedEmail = authDetails.getUsername();
        if (!authenticatedEmail.equalsIgnoreCase(userEmail)) {
            throw new AccessDeniedException("Access denied!");
        }
    }

    private User toUserModel(final UserRequestDTO dto) {
        return new User(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail().toLowerCase(),
                dto.getPassword()
        );
    }
}
