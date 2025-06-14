//package controller;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.UUID;
//
//public class AdminController {
//    private final AdminService adminService;
//    private final AdminView adminView;
//
//    public AdminController(AdminService adminService, AdminView adminView) {
//        this.adminService = adminService;
//        this.adminView = adminView;
//    }
//
//    public void listAllUsers() {
//        if (!SessionService.isAdmin()) {
//            adminView.displayError("Unauthorized access");
//            return;
//        }
//
//        try {
//            List<User> users = adminService.getAllUsers();
//            adminView.displayUsers(users);
//        } catch (SQLException e) {
//            adminView.displayError("Error fetching users: " + e.getMessage());
//        }
//    }
//
//    public void updateUserRole() {
//        if (!SessionService.isAdmin()) {
//            adminView.displayError("Unauthorized access");
//            return;
//        }
//
//        try {
//            UUID userId = adminView.promptForUserId();
//            String newRole = adminView.promptForNewRole();
//
//            boolean success = adminService.updateUserRole(userId, newRole);
//            if (success) {
//                adminView.displayUserUpdateSuccess();
//            } else {
//                adminView.displayError("Failed to update user role");
//            }
//        } catch (SQLException e) {
//            adminView.displayError("Error updating user: " + e.getMessage());
//        }
//    }
//
//    public void deleteUser() {
//        if (!SessionService.isAdmin()) {
//            adminView.displayError("Unauthorized access");
//            return;
//        }
//
//        try {
//            UUID userId = adminView.promptForUserId();
//            boolean success = adminService.deleteUser(userId);
//            if (success) {
//                adminView.displayUserDeletionSuccess();
//            } else {
//                adminView.displayError("Failed to delete user");
//            }
//        } catch (SQLException e) {
//            adminView.displayError("Error deleting user: " + e.getMessage());
//        }
//    }
//}
