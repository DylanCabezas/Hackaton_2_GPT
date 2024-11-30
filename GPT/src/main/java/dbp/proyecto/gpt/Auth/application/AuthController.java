package dbp.proyecto.gpt.Auth.application;

import dbp.proyecto.gpt.Auth.domain.AuthService;
import dbp.proyecto.gpt.Auth.dto.JwtAuthenticationResponseDto;
import dbp.proyecto.gpt.Auth.dto.LoginDto;
import dbp.proyecto.gpt.Auth.dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDto> login(@RequestBody LoginDto logInDTO) {
        return ResponseEntity.ok(authenticationService.login(logInDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponseDto> register(@RequestBody RegisterDto signInDTO) {
        return ResponseEntity.ok(authenticationService.register(signInDTO));
    }







}
