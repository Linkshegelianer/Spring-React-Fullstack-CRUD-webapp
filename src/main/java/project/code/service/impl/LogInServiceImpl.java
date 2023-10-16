package project.code.service.impl;

import project.code.dto.LogInDTO;
import project.code.domain.User;
import project.code.repository.UserRepository;
import project.code.security.JWTUtils;
import project.code.service.LogInService;
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
