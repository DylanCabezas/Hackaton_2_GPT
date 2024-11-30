package dbp.proyecto.gpt.Auth.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String name;

    private String lastname;

    private String email;

    private String password;

    private Boolean isAdmin = false;
}
