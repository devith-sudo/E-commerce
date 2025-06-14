// AuthService.java
package com.ecommerce.service;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.PasswordHasher;

public class AuthService {
    private final UserDAO userDAO;
    private User currentUser;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean register(User user) {
        if (userDAO.getUserByUsername(user.getUserName()) != null) {
            return false;
        }
        user.setRole("CUSTOMER");
        userDAO.createUser(user);
        return true;
    }

    public boolean login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            return false;
        }

        // Password is stored as "hashedPassword:salt"
        String[] passwordParts = user.getPassword().split(":");
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

    public User getCurrentUser() {
        return currentUser;
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