package rems;

import config.config;
import java.util.Scanner;

public class UserManager {

    private Scanner sc = new Scanner(System.in);
    private config cfg = new config();

    public void manageUsers() {
        while (true) {
            System.out.println("\n===== MANAGE USERS =====");
            System.out.println("1. View Users");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. Activate / Deactivate User");
            System.out.println("5. Approve / Reject User");
            System.out.println("6. Back");
            System.out.print("Choose option: ");

            int choice = sc.nextInt(); 
            sc.nextLine();

            switch (choice) {
                case 1: viewUsers(); break;
                case 2: updateUser(); break;
                case 3: deleteUser(); break;
                case 4: activateDeactivateUser(); break;
                case 5: approveRejectUser(); break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // VIEW USERS WITH STATUS INCLUDED
    private void viewUsers() {
        String sql = "SELECT user_id, username, email, role, first_name, last_name, status, is_active FROM users";
        String[] headers = {"ID", "Username", "Email", "Role", "First Name", "Last Name", "Status", "Active"};
        String[] cols    = {"user_id", "username", "email", "role", "first_name", "last_name", "status", "is_active"};
        cfg.viewRecords(sql, headers, cols);
    }

    // UPDATE USER INFORMATION
    private void updateUser() {
        System.out.print("Enter User ID to update: ");
        int userId = sc.nextInt(); 
        sc.nextLine();

        // Check if exists
        if (!cfg.recordExists("SELECT 1 FROM users WHERE user_id=?", userId)) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("New Username: ");
        String username = sc.nextLine();

        System.out.print("New Email: ");
        String email = sc.nextLine();

        System.out.print("New First Name: ");
        String firstName = sc.nextLine();

        System.out.print("New Last Name: ");
        String lastName = sc.nextLine();

        System.out.print("New Role (Admin/Agent/Client): ");
        String role = sc.nextLine();

        String sql = "UPDATE users SET username=?, email=?, first_name=?, last_name=?, role=? WHERE user_id=?";
        cfg.updateRecord(sql, username, email, firstName, lastName, role, userId);

        System.out.println("User updated successfully.");
    }

    // DELETE USER
    private void deleteUser() {
        System.out.print("Enter User ID to delete: ");
        int userId = sc.nextInt(); 
        sc.nextLine();

        if (!cfg.recordExists("SELECT 1 FROM users WHERE user_id=?", userId)) {
            System.out.println("User not found.");
            return;
        }

        String sql = "DELETE FROM users WHERE user_id=?";
        cfg.deleteRecord(sql, userId);

        System.out.println("User deleted successfully.");
    }

    // ACTIVATE / DEACTIVATE USER
    void activateDeactivateUser() {
        System.out.print("Enter User ID: ");
        int userId = sc.nextInt();

        if (!cfg.recordExists("SELECT 1 FROM users WHERE user_id=?", userId)) {
            System.out.println("User ID not found.");
            return;
        }

        System.out.print("Set Active? (1 = yes, 0 = no): ");
        int active = sc.nextInt();

        String sql = "UPDATE users SET is_active=? WHERE user_id=?";
        cfg.updateRecord(sql, active, userId);

        System.out.println("User status updated successfully.");
    }

    // APPROVE OR REJECT USER ACCOUNT
    private void approveRejectUser() {
        System.out.print("Enter User ID: ");
        int userId = sc.nextInt(); 
        sc.nextLine();

        if (!cfg.recordExists("SELECT 1 FROM users WHERE user_id=?", userId)) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Set account status:");
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Set Pending");
        System.out.print("Choose: ");
        int choice = sc.nextInt(); 
        sc.nextLine();

        String status;
        switch (choice) {
            case 1: status = "Approved"; break;
            case 2: status = "Rejected"; break;
            case 3: status = "Pending"; break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        String sql = "UPDATE users SET status=? WHERE user_id=?";
        cfg.updateRecord(sql, status, userId);

        System.out.println("User status set to: " + status);
    }

    void changeUserRole() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
