package dbp.proyecto.gpt.Auth.dto;

import lombok.Data;

@Data
public class PasswordVerification {
    private Long userId;
    private String password;
}
