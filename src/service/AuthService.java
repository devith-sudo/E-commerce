package service;

import dto.LoginRequestDTO;
import dto.RegisterRequestDTO;
import model.User;
import util.InputValidator;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User login(LoginRequestDTO dto) throws Exception {
        if (!InputValidator.isValidEmail(dto.getEmail())) {
            throw new Exception("Invalid email format.");
        }

        User user = userDAO.getUserByEmail(dto.getEmail());
        if (user == null || !user.getPassword().equals(dto.getPassword())) {
            throw new Exception("Incorrect email or password.");
        }

        return user;
    }

    public User register(RegisterRequestDTO dto) throws Exception {
        if (!InputValidator.isNonEmpty(dto.getName())) {
            throw new Exception("Name cannot be empty.");
        }

        if (!InputValidator.isValidEmail(dto.getEmail())) {
            throw new Exception("Invalid email format.");
        }

        if (!InputValidator.isValidPassword(dto.getPassword())) {
            throw new Exception("Password must be at least 6 characters.");
        }

        if (userDAO.existsByEmail(dto.getEmail())) {
            throw new Exception("User with this email already exists.");
        }

        User user = new User(0, dto.getName(), dto.getEmail(), dto.getPassword());
        userDAO.createUser(user);
        return user;
    }
}
