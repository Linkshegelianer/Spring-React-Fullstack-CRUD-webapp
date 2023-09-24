package hexlet.code.service.impl;

import hexlet.code.dto.LogInDTO;
import hexlet.code.domain.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JWTUtils;
import hexlet.code.service.LogInService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LogInServiceImpl implements LogInService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JWTUtils jwtUtils;

    @Override
    public String authenticate(LogInDTO logInDTO) {
        User existedUser = userRepository.findUserByEmailIgnoreCase(logInDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Sign in failed. User not found!"));

        String passwordToCheck = logInDTO.getPassword();
        if (!bCryptPasswordEncoder.matches(passwordToCheck, existedUser.getPassword())) {
            throw new UsernameNotFoundException("Sign in failed. Incorrect password!");
        }
        return jwtUtils.generateToken(existedUser);
    }
}
