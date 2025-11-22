package rems;

import config.config;
import java.util.Scanner;

public class ClientManager {

    private final config cfg;
    private final Scanner sc;

    public ClientManager(config cfg, Scanner sc) {
        this.cfg = cfg;
        this.sc = sc;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== MANAGE CLIENTS ===");
            System.out.println("1. View All Clients");
            System.out.println("2. Search Client");
            System.out.println("3. Update Client Info");
            System.out.println("4. Deactivate Client");
            System.out.println("5. Delete Client");
            System.out.println("6. Back to Agent Menu");
            System.out.print("Choose option: ");
            
            int choice = sc.nextInt(); sc.nextLine();
            
            switch (choice) {
                case 1: viewAllClients(); break;
                case 2: searchClient(); break;
                case 3: updateClientInfo(); break;
                case 4: deactivateClient(); break;
                case 5: deleteClient(); break;
                case 6: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private void viewAllClients() {
        String sql = "SELECT user_id, username, email, first_name, last_name, phone FROM users WHERE role='Client'";
        String[] headers = {"ID", "Username", "Email", "First Name", "Last Name", "Phone"};
        String[] columns = {"user_id", "username", "email", "first_name", "last_name", "phone"};
        cfg.viewRecords(sql, headers, columns);
    }

    private void searchClient() {
        System.out.print("Enter username, email, or phone to search: ");
        String query = sc.nextLine();
        String sql = "SELECT user_id, username, email, first_name, last_name, phone FROM users WHERE role='Client' AND (username LIKE ? OR email LIKE ? OR phone LIKE ?)";
        String[] headers = {"ID", "Username", "Email", "First Name", "Last Name", "Phone"};
        String[] columns = {"user_id", "username", "email", "first_name", "last_name", "phone"};
        cfg.viewRecords(sql, headers, columns, "%" + query + "%", "%" + query + "%", "%" + query + "%");
    }

    private void updateClientInfo() {
        System.out.print("Enter Client ID to update: ");
        int clientId = sc.nextInt(); sc.nextLine();

        System.out.print("Enter new first name (leave blank to keep current): ");
        String firstName = sc.nextLine();

        System.out.print("Enter new last name (leave blank to keep current): ");
        String lastName = sc.nextLine();

        System.out.print("Enter new phone (leave blank to keep current): ");
        String phone = sc.nextLine();

        String sql = "UPDATE users SET first_name=?, last_name=?, phone=? WHERE user_id=? AND role='Client'";
        cfg.updateRecord(sql, firstName.isEmpty() ? null : firstName,
                              lastName.isEmpty() ? null : lastName,
                              phone.isEmpty() ? null : phone,
                              clientId);
        System.out.println("Client info updated successfully!");
    }

    private void deactivateClient() {
        System.out.print("Enter Client ID to deactivate: ");
        int clientId = sc.nextInt(); sc.nextLine();
        String sql = "UPDATE users SET is_active=0 WHERE user_id=? AND role='Client'";
        cfg.updateRecord(sql, clientId);
        System.out.println("Client deactivated successfully!");
    }

    private void deleteClient() {
        System.out.print("Enter Client ID to delete: ");
        int clientId = sc.nextInt(); sc.nextLine();
        System.out.print("Are you sure you want to delete this client? (yes/no): ");
        String confirm = sc.nextLine();
        if(confirm.equalsIgnoreCase("yes")) {
            String sql = "DELETE FROM users WHERE user_id=? AND role='Client'";
            cfg.updateRecord(sql, clientId);
            System.out.println("Client deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}
