package dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {
    // Setters (optional, depending on mutability requirements)
    // Getters
    private String email;
    private String password;

    // Constructor
    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Optional: toString() for logging/debugging
    @Override
    public String toString() {
        return "email='" + email + '\'' + ", password='[PROTECTED]";
    }
}
