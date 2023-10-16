package project.code.config;

import project.code.security.JWTAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final JWTAuthenticationFilter jwtFilter;

    private final RequestMatcher publicPaths = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/login", POST.name()),
            new AntPathRequestMatcher("/api/users", GET.name()),
            new AntPathRequestMatcher("/api/users", POST.name()),
            new AntPathRequestMatcher("/api/users/{id}", GET.name()),
            new AntPathRequestMatcher("/api/statuses", GET.name()),
            new AntPathRequestMatcher("/api/statuses/{id}", GET.name()),
            new AntPathRequestMatcher("/api/tasks", GET.name()),
            new AntPathRequestMatcher("/api/tasks/{id}", GET.name()),
            new NegatedRequestMatcher(new AntPathRequestMatcher("/api/**"))
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests()
                .requestMatchers(publicPaths).permitAll()
                .anyRequest().authenticated();

        jwtFilter.setIgnoredPaths(publicPaths);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
