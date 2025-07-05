// AuthService.java
package com.ecommerce.service;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.PasswordHasher;
import lombok.Getter;

@Getter
public class AuthService {
    private final UserDAO userDAO;
    private User currentUser;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Registers user with hashed + salted password
    public boolean register(User user) {
        if (userDAO.getUserByUsername(user.getUserName()) != null) {
            return false; // username exists
        }

        // Hash and salt the password before saving
        String salt = PasswordHasher.generateSalt();
        String hashedPassword = PasswordHasher.hashPassword(user.getPassword(), salt);
        user.setPassword(hashedPassword + ":" + salt);
        user.setRole("CUSTOMER");

        userDAO.createUser(user);
        return true;
    }

    // Login by verifying hashed password
    public boolean login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            System.out.println("Username not found");
            return false;
        }

        String[] passwordParts = user.getPassword().split(":");
        if (passwordParts.length != 2) {
            System.out.println("Stored password format is invalid.");
            return false;
        }

        String storedHash = passwordParts[0];
        String salt = passwordParts[1];

        if (PasswordHasher.verifyPassword(password, salt, storedHash)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }

    public boolean isStaff() {
        return currentUser != null && ("STAFF".equals(currentUser.getRole()) || "ADMIN".equals(currentUser.getRole()));
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
