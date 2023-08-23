package hexlet.code.service;

import hexlet.code.domain.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.AppUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Spring will use this implementation by default
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User existedUser = userRepository.findUserByEmailIgnoreCase(email)
            .orElseThrow(() -> new UsernameNotFoundException("User '%s' not found!".formatted(email)));
        return new AppUserDetails(existedUser);
    }
}
