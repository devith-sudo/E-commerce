package controller;

import model.User;
import service.AuthService;
import view.AuthView;

public class AuthController {
    private final AuthService authService;
    private final AuthView authView;

    public AuthController(AuthService authService, AuthView authView) {
        this.authService = authService;
        this.authView = authView;
    }

    public void register() {
        UserDTO userDTO = authView.getRegisterDetails();
        try {
            User user = authService.register(userDTO);
            authView.showRegistrationSuccess(user.getUsername());
        } catch (Exception e) {
            authView.showError(e.getMessage());
        }
    }

    public void login() {
        LoginDTO loginDTO = authView.getLoginDetails();
        try {
            User user = authService.login(loginDTO);
            SessionService.setCurrentUser(user);
            authView.showLoginSuccess(user.getUsername());
        } catch (Exception e) {
            authView.showError(e.getMessage());
        }
    }

    public void logout() {
        SessionService.logout();
        authView.showLogoutSuccess();
    }
}
