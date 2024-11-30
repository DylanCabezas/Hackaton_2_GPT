package dbp.proyecto.gpt.Auth.domain;

import dbp.proyecto.gpt.Auth.dto.JwtAuthenticationResponseDto;
import dbp.proyecto.gpt.Auth.dto.LoginDto;
import dbp.proyecto.gpt.Auth.dto.RegisterDto;
import dbp.proyecto.gpt.Exceptions.EmailAlreadyExistsException;
import dbp.proyecto.gpt.config.JwtService;
import dbp.proyecto.gpt.user.domain.Role;
import dbp.proyecto.gpt.user.domain.User;
import dbp.proyecto.gpt.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationResponseDto login(LoginDto LoginRequestDto) {
        User user = userRepository.findByEmail(LoginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        if (!encoder.matches(LoginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong Password!");
        }

        JwtAuthenticationResponseDto response = new JwtAuthenticationResponseDto();
        response.setToken(jwtService.generateToken(user));
        return response;
    }

    public JwtAuthenticationResponseDto register(RegisterDto signinDto) {
        if (userRepository.findByEmail(signinDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exist");
        }

        User user = new User();
        user.setEmail(signinDto.getEmail());
        user.setPassword(passwordEncoder.encode(signinDto.getPassword()));
        user.setName(signinDto.getName());
        user.setLastName(signinDto.getLastname());

        if (signinDto.getIsAdmin()) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        userRepository.save(user);
        JwtAuthenticationResponseDto response = new JwtAuthenticationResponseDto();
        response.setToken(jwtService.generateToken(user));
        return response;
    }

}
