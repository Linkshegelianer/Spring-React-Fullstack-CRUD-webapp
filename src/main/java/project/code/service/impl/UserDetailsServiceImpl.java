package project.code.service.impl;

import project.code.domain.User;
import project.code.repository.UserRepository;
import project.code.security.AppUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User existedUser = userRepository.findUserByEmailIgnoreCase(email)
            .orElseThrow(() -> new UsernameNotFoundException("User '%s' not found!".formatted(email)));
        return new AppUserDetails(existedUser);
    }
}
