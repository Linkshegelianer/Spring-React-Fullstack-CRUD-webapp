package hexlet.code.service;

import hexlet.code.domain.dto.LogInRequestDTO;
import hexlet.code.domain.model.User;
import hexlet.code.exception.SignInException;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JWTUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LogInService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JWTUtils jwtUtils;

    public LogInService(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public String authenticate(LogInRequestDTO logInDTO) {
        User existedUser = userRepository.findUserByEmailIgnoreCase(logInDTO.getEmail())
            .orElseThrow(() -> new SignInException("Sign in failed. User not found!"));

        String passwordToCheck = logInDTO.getPassword();
        if (!bCryptPasswordEncoder.matches(passwordToCheck, existedUser.getPassword())) {
            throw new SignInException("Sign in failed. Incorrect password!");
        }
        return jwtUtils.generateToken(existedUser);
    }
}
