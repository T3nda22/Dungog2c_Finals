package rems;

import config.config;
import java.util.Scanner;

public class DocumentManager {

    private static Scanner sc = new Scanner(System.in);
    private static config cfg = new config();
    public static int currentUserId = 0;

    public static void showMenu() {
        while (true) {
            System.out.println("\n=== DOCUMENT MANAGEMENT ===");
            System.out.println("1. Upload Document");
            System.out.println("2. View Documents");
            System.out.println("3. Search Documents");
            System.out.println("4. Delete Document");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: uploadDocument(); break;
                case 2: viewDocuments(); break;
                case 3: searchDocuments(); break;
                case 4: deleteDocument(); break;
                case 5: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private static void uploadDocument() {
        try {
            System.out.print("Enter Property ID (0 if not property-specific): ");
            int propertyId = sc.nextInt(); sc.nextLine();

            System.out.print("Enter document type (contract, id_proof, ownership, invoice, other): ");
            String docType = sc.nextLine();

            System.out.print("Enter file name: ");
            String fileName = sc.nextLine();

            System.out.print("Enter file path/URL: ");
            String filePath = sc.nextLine();

            System.out.print("Enter file size in KB (optional, press enter to skip): ");
            String sizeInput = sc.nextLine();
            Integer fileSize = null;
            if (!sizeInput.isEmpty()) {
                try {
                    fileSize = Integer.parseInt(sizeInput);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid file size, skipping.");
                }
            }

            System.out.print("Enter description (optional): ");
            String description = sc.nextLine();

            String sql = "INSERT INTO documents (property_id, document_type, file_name, file_path, file_size, uploaded_by, description) VALUES (?,?,?,?,?,?,?)";
            cfg.addRecord(sql, propertyId, docType, fileName, filePath, fileSize, currentUserId, description);

            System.out.println("Document uploaded successfully!");
        } catch (Exception e) {
            System.out.println("Error uploading document: " + e.getMessage());
        }
    }

    private static void viewDocuments() {
        String sql = "SELECT d.document_id, p.title as property_title, d.document_type, d.file_name, " +
                     "u.username as uploaded_by, d.uploaded_at " +
                     "FROM documents d " +
                     "LEFT JOIN properties p ON d.property_id = p.property_id " +
                     "JOIN users u ON d.uploaded_by = u.user_id " +
                     "ORDER BY d.uploaded_at DESC";

        String[] headers = {"Doc ID", "Property", "Type", "File Name", "Uploaded By", "Date"};
        String[] columns = {"document_id", "property_title", "document_type", "file_name", "uploaded_by", "uploaded_at"};
        cfg.viewRecords(sql, headers, columns);
    }

    private static void searchDocuments() {
        System.out.println("Search by: 1) Property 2) Document Type 3) Uploader");
        int searchType = sc.nextInt(); sc.nextLine();

        String sql;
        String[] headers = {"Doc ID", "Property", "Type", "File Name", "Uploaded By", "Date"};
        String[] columns = {"document_id", "property_title", "document_type", "file_name", "uploaded_by", "uploaded_at"};

        switch (searchType) {
            case 1:
                System.out.print("Enter Property ID: ");
                int propertyId = sc.nextInt(); sc.nextLine();
                sql = "SELECT d.document_id, p.title as property_title, d.document_type, d.file_name, " +
                      "u.username as uploaded_by, d.uploaded_at " +
                      "FROM documents d " +
                      "LEFT JOIN properties p ON d.property_id = p.property_id " +
                      "JOIN users u ON d.uploaded_by = u.user_id " +
                      "WHERE d.property_id = ? ORDER BY d.uploaded_at DESC";
                cfg.viewRecords(sql, headers, columns, propertyId);
                break;
            case 2:
                System.out.print("Enter Document Type: ");
                String docType = sc.nextLine();
                sql = "SELECT d.document_id, p.title as property_title, d.document_type, d.file_name, " +
                      "u.username as uploaded_by, d.uploaded_at " +
                      "FROM documents d " +
                      "LEFT JOIN properties p ON d.property_id = p.property_id " +
                      "JOIN users u ON d.uploaded_by = u.user_id " +
                      "WHERE d.document_type LIKE ? ORDER BY d.uploaded_at DESC";
                cfg.viewRecords(sql, headers, columns, "%" + docType + "%");
                break;
            case 3:
                System.out.print("Enter Uploader Username: ");
                String uploader = sc.nextLine();
                sql = "SELECT d.document_id, p.title as property_title, d.document_type, d.file_name, " +
                      "u.username as uploaded_by, d.uploaded_at " +
                      "FROM documents d " +
                      "LEFT JOIN properties p ON d.property_id = p.property_id " +
                      "JOIN users u ON d.uploaded_by = u.user_id " +
                      "WHERE u.username LIKE ? ORDER BY d.uploaded_at DESC";
                cfg.viewRecords(sql, headers, columns, "%" + uploader + "%");
                break;
            default:
                System.out.println("Invalid search option!");
        }
    }

    private static void deleteDocument() {
        try {
            System.out.print("Enter Document ID to delete: ");
            int docId = sc.nextInt(); sc.nextLine();

            String checkSql = "SELECT * FROM documents WHERE document_id=?";
            if (!cfg.recordExists(checkSql, docId)) {
                System.out.println("Document not found!");
                return;
            }

            System.out.print("Are you sure you want to delete this document? (y/n): ");
            String confirm = sc.nextLine();

            if (confirm.equalsIgnoreCase("y")) {
                String sql = "DELETE FROM documents WHERE document_id=?";
                cfg.deleteRecord(sql, docId);
                System.out.println("Document deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting document: " + e.getMessage());
        }
    }

    void documentManagement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
