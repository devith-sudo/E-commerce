// UserService.java
package com.ecommerce.service;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
}