package rems;

import config.config;
import java.util.Scanner;

public class PropertyManager {

    private static Scanner sc = new Scanner(System.in);
    private static config cfg = new config();

    public static void showMenu() {

        while (true) {
            System.out.println("\n===== MANAGE PROPERTIES =====");
            System.out.println("1. View All Properties");
            System.out.println("2. Delete Property");
            System.out.println("3. Update Property Status");
            System.out.println("4. Update Property Details");
            System.out.println("5. Back");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: viewAllProperties(); break;
                case 2: deleteProperty(); break;
                case 3: updatePropertyStatus(); break;
                case 4: updateProperty(); break;
                case 5: return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void viewAllProperties() {
        String sql = "SELECT * FROM properties WHERE status != 'sold'";
        String[] headers = {"ID","Title","Type","Price","Location","Bedrooms","Status"};
        String[] columns = {"property_id","title","type","price","location","bedrooms","status"};
        cfg.viewRecords(sql, headers, columns);
    }

    private static void deleteProperty() {
        try {
            System.out.print("Property ID to delete: ");
            int id = sc.nextInt(); sc.nextLine();
            String checkSql = "SELECT * FROM properties WHERE property_id=?";

            if (!cfg.recordExists(checkSql, id)) { 
                System.out.println("Property not found."); 
                return; 
            }

            System.out.print("Confirm delete? (y/n): ");
            String confirm = sc.nextLine();

            if(confirm.equalsIgnoreCase("y")) {
                String sql = "DELETE FROM properties WHERE property_id=?";
                cfg.deleteRecord(sql, id);
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting property: " + e.getMessage());
        }
    }

    public static void updatePropertyStatus() {
        System.out.print("Enter Property ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Enter New Status (available/sold/pending/reserved): ");
        String status = sc.nextLine();

        String sql = "UPDATE properties SET status=? WHERE property_id=?";
        cfg.updateRecord(sql, status, id);
        System.out.println("Property status updated!");
    }

    public static void updateProperty() {
        try {
            System.out.print("Property ID to update: ");
            int id = sc.nextInt(); sc.nextLine();
            String checkSql = "SELECT * FROM properties WHERE property_id=?";

            if (!cfg.recordExists(checkSql, id)) { 
                System.out.println("Property not found."); 
                return; 
            }

            System.out.print("New title: "); String title = sc.nextLine();
            System.out.print("New type: "); String type = sc.nextLine();
            System.out.print("New price: "); double price = sc.nextDouble(); sc.nextLine();
            System.out.print("New location: "); String location = sc.nextLine();
            System.out.print("New description: "); String description = sc.nextLine();

            String sql = "UPDATE properties SET title=?,type=?,price=?,location=?,description=? WHERE property_id=?";
            cfg.updateRecord(sql, title, type, price, location, description, id);
            System.out.println("Property updated successfully!");

        } catch (Exception e) {
            System.out.println("Error updating property: " + e.getMessage());
        }
    }
    
}
