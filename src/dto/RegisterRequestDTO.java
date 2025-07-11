package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RegisterRequestDTO {
    // Setters (optional)
    // Getters
    private String name;
    private String email;
    private String password;

    // Constructor
//    public RegisterRequestDTO(String name, String email, String password) {
//        this.name = name;
//        this.email = email;
//        this.password = password;
//    }

    // Optional: toString() for debugging (avoid printing password)
    @Override
    public String toString() {
        return "name='" + name + '\'' + ", email='" + email + '\'' + ", password='[PROTECTED]";
    }
}
