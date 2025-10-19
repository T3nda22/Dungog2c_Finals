package rems;

import config.config;
import java.util.*;

public class Main {
    private static config cfg = new config();
    private static Scanner sc = new Scanner(System.in);
    private static boolean loggedIn = false;
    private static String currentUser = "";
    private static String currentRole = "";
    private static int currentUserId = 0;

    public static void main(String[] args) {
        initializeDatabase();
        
        while (true) {
            if (!loggedIn) {
                showWelcomeMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private static void initializeDatabase() {
        System.out.println("Database initialized successfully!");
    }
    
    private static void showWelcomeMenu() {
        System.out.println("\n=== Welcome to Real Estate Management System ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose option: ");
        
        try {
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number!");
            sc.nextLine();
        }
    }
    
    private static void showMainMenu() {
        try {
            System.out.println("\n--- MAIN MENU (" + currentRole + ") ---");
            System.out.println("Welcome, " + currentUser);

            if (currentRole.equalsIgnoreCase("Admin")) {
                showAdminMenu();
            } else if (currentRole.equalsIgnoreCase("Agent")) {
                showAgentMenu();
            } else {
                showClientMenu();
            }
        } catch (Exception e) {
            System.out.println("Error in menu: " + e.getMessage());
        }
    }
    
    private static void showAdminMenu() {
    System.out.println("1. Manage Properties");
    System.out.println("2. Manage Users");
    System.out.println("3. View Sales Reports");
    System.out.println("4. View Commissions");
    System.out.println("5. Document Management"); // NEW
    System.out.println("6. Communication System"); // NEW
    System.out.println("7. Reporting & Analytics"); // NEW
    System.out.println("8. System Reports");
    System.out.println("9. Logout");
    System.out.print("Choose option: ");
    
    int choice = sc.nextInt(); sc.nextLine();
    switch (choice) {
        case 1: manageProperties(); break;
        case 2: manageUsers(); break;
        case 3: viewSalesReports(); break;
        case 4: viewCommissions(); break;
        case 5: documentManagement(); break; // NEW
        case 6: communicationSystem(); break; // NEW
        case 7: reportingAnalytics(); break; // NEW
        case 8: generateReports(); break;
        case 9: logout(); break;
        default: System.out.println("Invalid choice!");
    }
}
    
    private static void showAgentMenu() {
        System.out.println("1. Add Property");
        System.out.println("2. View My Properties");
        System.out.println("3. Search Properties");
        System.out.println("4. Update Property");
        System.out.println("5. Record Sale");
        System.out.println("6. Schedule Appointment");
        System.out.println("7. View My Appointments");
        System.out.println("8. View My Commissions");
        System.out.println("9. Manage Clients");
        System.out.println("10. Document Management"); // NEW
        System.out.println("11. Communication System"); // NEW
        System.out.println("12. Logout");
        System.out.print("Choose option: ");
        
        int choice = sc.nextInt(); sc.nextLine();
        switch (choice) {
            case 1: addProperty(); break;
            case 2: viewMyProperties(); break;
            case 3: searchProperty(); break;
            case 4: updateProperty(); break;
            case 5: recordSale(); break;
            case 6: scheduleAppointment(); break;
            case 7: viewMyAppointments(); break;
            case 8: viewCommissions(); break;
            case 9: manageClients(); break;
            case 10: documentManagement(); break; // NEW
            case 11: communicationSystem(); break; // NEW
            case 12: logout(); break;
            default: System.out.println("Invalid choice!");
    }
}
    
    private static void showClientMenu() {
        System.out.println("1. View All Properties");
        System.out.println("2. Search Properties");
        System.out.println("3. Schedule Viewing");
        System.out.println("4. My Appointments");
        System.out.println("5. Communication System"); // NEW
        System.out.println("6. Logout");
        System.out.print("Choose option: ");

        int choice = sc.nextInt(); sc.nextLine();
        switch (choice) {
            case 1: viewAllProperties(); break;
            case 2: searchProperty(); break;
            case 3: scheduleAppointment(); break;
            case 4: viewMyAppointments(); break;
            case 5: communicationSystem(); break; // NEW
            case 6: logout(); break;
            default: System.out.println("Invalid choice!");
    }
}

    private static void registerUser() {
    try {
        String username = "", email = "", phone = "";
        boolean usernameAvailable = false;
        boolean emailAvailable = false;
        boolean phoneAvailable = false;

        // Get username with availability check
        while (!usernameAvailable) {
            System.out.print("Enter username: ");
            username = sc.nextLine();
            
            if (username.trim().isEmpty()) {
                System.out.println("Username cannot be empty!");
                continue;
            }
            
            String checkUsernameSql = "SELECT * FROM users WHERE username=?";
            if (cfg.recordExists(checkUsernameSql, username)) {
                System.out.println("Username '" + username + "' is already taken. Please choose a different username.");
            } else {
                usernameAvailable = true;
            }
        }

        // Get email with availability check
        while (!emailAvailable) {
            System.out.print("Enter email: ");
            email = sc.nextLine();
            
            if (!SecurityUtils.isValidEmail(email)) {
                System.out.println("Invalid email format! Please enter a valid email address.");
                continue;
            }
            
            String checkEmailSql = "SELECT * FROM users WHERE email=?";
            if (cfg.recordExists(checkEmailSql, email)) {
                System.out.println("Email '" + email + "' is already registered. Please use a different email address.");
            } else {
                emailAvailable = true;
            }
        }

        // Get remaining information
        System.out.print("Enter first name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter last name: ");
        String lastName = sc.nextLine();
        
        // Get phone number with validation
        while (!phoneAvailable) {
            System.out.print("Enter phone number: ");
            phone = sc.nextLine();
            
            // Check if phone contains only numbers
            if (!phone.matches("\\d+")) {
                System.out.println("Phone number must contain only numbers! Please enter a valid phone number.");
                continue;
            }
            
            // Check if phone has exactly 11 digits
            if (phone.length() != 11) {
                System.out.println("Phone number must be exactly 11 digits! You entered " + phone.length() + " digits.");
                continue;
            }
            
            // Check if phone number is already registered
            String checkPhoneSql = "SELECT * FROM users WHERE phone=?";
            if (cfg.recordExists(checkPhoneSql, phone)) {
                System.out.println("Phone number '" + phone + "' is already registered. Please use a different phone number.");
            } else {
                phoneAvailable = true;
            }
        }
        
        // Get password with validation
        String password;
        while (true) {
            System.out.print("Enter password: ");
            password = sc.nextLine();
            
            if (!SecurityUtils.isStrongPassword(password)) {
                System.out.println("Password must be at least 8 characters with uppercase, lowercase and numbers!");
            } else {
                break;
            }
        }

        String role;
        while (true) {
            System.out.print("Enter role (Admin/Agent/Client): ");
            role = sc.nextLine();
            
            if (!role.equalsIgnoreCase("Admin") && !role.equalsIgnoreCase("Agent") && !role.equalsIgnoreCase("Client")) {
                System.out.println("Invalid role! Please choose from: Admin, Agent, or Client.");
            } else {
                // Capitalize first letter for consistency
                role = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
                break;
            }
        }

        String hashedPassword = SecurityUtils.hashPassword(password);
        String sql = "INSERT INTO users (username, email, password_hash, role, first_name, last_name, phone) VALUES (?,?,?,?,?,?,?)";
        
        cfg.addRecord(sql, username, email, hashedPassword, role, firstName, lastName, phone);
        System.out.println("Registration successful! You can now login with your credentials.");
        
    } catch (Exception e) {
        System.out.println("Error during registration: " + e.getMessage());
        e.printStackTrace(); // Add this for debugging
    }
}

    private static void loginUser() {
    try {
        System.out.print("Enter username, email, or phone: ");
        String identifier = sc.nextLine();
        
        // Check what type of identifier was entered
        boolean isPhone = identifier.matches("\\d+");
        boolean isEmail = identifier.contains("@");
        boolean isUsername = !isPhone && !isEmail;
        
        // First, check if the identifier exists in the system
        String checkUserSql = "SELECT * FROM users WHERE username=? OR email=? OR phone=? AND is_active=1";
        List<Map<String, Object>> userCheck = cfg.fetchRecords(checkUserSql, identifier, identifier, identifier);
        
        if (userCheck.isEmpty()) {
            if (isPhone) {
                System.out.println("No account found with phone number: " + identifier);
            } else if (isEmail) {
                System.out.println("No account found with email: " + identifier);
            } else {
                System.out.println("No account found with username: " + identifier);
            }
            return;
        }
        
        int attempts = 3;
        boolean loginSuccess = false;
        
        while (attempts > 0 && !loginSuccess) {
            System.out.print("Enter password (" + attempts + " attempts remaining): ");
            String password = sc.nextLine();

            String hashedPassword = SecurityUtils.hashPassword(password);
            
            String sql;
            if (isPhone) {
                sql = "SELECT * FROM users WHERE phone=? AND password_hash=? AND is_active=1";
            } else {
                sql = "SELECT * FROM users WHERE (username=? OR email=?) AND password_hash=? AND is_active=1";
            }
            
            List<Map<String, Object>> users;
            if (isPhone) {
                users = cfg.fetchRecords(sql, identifier, hashedPassword);
            } else {
                users = cfg.fetchRecords(sql, identifier, identifier, hashedPassword);
            }
            
            if (!users.isEmpty()) {
                Map<String, Object> user = users.get(0);
                currentUser = user.get("username").toString();
                currentRole = user.get("role").toString();
                currentUserId = Integer.parseInt(user.get("user_id").toString());
                loggedIn = true;
                loginSuccess = true;
                
                String loginMethod = isPhone ? "phone" : (isEmail ? "email" : "username");
                System.out.println("Login successful! Welcome " + currentUser + " (logged in with " + loginMethod + ")");
            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println("Incorrect password. Please try again.");
                } else {
                    System.out.println("Too many failed attempts. Returning to main menu.");
                }
            }
        }
        
    } catch (Exception e) {
        System.out.println("Login error: " + e.getMessage());
    }
}
    
    private static void addProperty() {
        try {
            System.out.print("Enter title: ");
            String title = sc.nextLine();
            System.out.print("Enter type (house, lot, condo, apartment, villa): ");
            String type = sc.nextLine();
            System.out.print("Enter price: ");
            double price = sc.nextDouble(); sc.nextLine();
            System.out.print("Enter location: ");
            String location = sc.nextLine();
            System.out.print("Enter size (sqft): ");
            int size = sc.nextInt(); sc.nextLine();
            System.out.print("Enter bedrooms: ");
            int bedrooms = sc.nextInt(); sc.nextLine();
            System.out.print("Enter bathrooms: ");
            int bathrooms = sc.nextInt(); sc.nextLine();
            System.out.print("Enter description: ");
            String description = sc.nextLine();

            String sql = "INSERT INTO properties (title, type, price, location, description, size_sqft, bedrooms, bathrooms, created_by) VALUES (?,?,?,?,?,?,?,?,?)";
            
            cfg.addRecord(sql, title, type, price, location, description, size, bedrooms, bathrooms, currentUserId);
            System.out.println("Property added successfully!");
            
        } catch (Exception e) {
            System.out.println("Error adding property: " + e.getMessage());
        }
    }

    private static void viewAllProperties() {
        String sql = "SELECT * FROM properties WHERE status != 'sold'";
        String[] headers = {"ID","Title","Type","Price","Location","Bedrooms","Status"};
        String[] columns = {"property_id","title","type","price","location","bedrooms","status"};
        cfg.viewRecords(sql, headers, columns);
    }

    private static void viewMyProperties() {
        String sql = "SELECT * FROM properties WHERE created_by = ?";
        String[] headers = {"ID","Title","Type","Price","Location","Status"};
        String[] columns = {"property_id","title","type","price","location","status"};
        cfg.viewRecords(sql, headers, columns, currentUserId);
    }

    private static void searchProperty() {
        String[] headers = {"ID","Title","Type","Price","Location","Bedrooms","Status"};
        String[] columns = {"property_id","title","type","price","location","bedrooms","status"};
        
        System.out.println("Search by: 1) ID 2) Location 3) Price Range 4) Type");
        int searchType = sc.nextInt(); sc.nextLine();
        
        switch (searchType) {
            case 1:
                System.out.print("Enter Property ID: ");
                int id = sc.nextInt(); sc.nextLine();
                String sql = "SELECT * FROM properties WHERE property_id=?";
                cfg.viewRecords(sql, headers, columns, id);
                break;
            case 2:
                System.out.print("Enter Location: ");
                String location = sc.nextLine();
                sql = "SELECT * FROM properties WHERE location LIKE ? AND status != 'sold'";
                cfg.viewRecords(sql, headers, columns, "%" + location + "%");
                break;
            case 3:
                System.out.print("Enter Min Price: ");
                double minPrice = sc.nextDouble();
                System.out.print("Enter Max Price: ");
                double maxPrice = sc.nextDouble(); sc.nextLine();
                sql = "SELECT * FROM properties WHERE price BETWEEN ? AND ? AND status != 'sold'";
                cfg.viewRecords(sql, headers, columns, minPrice, maxPrice);
                break;
            case 4:
                System.out.print("Enter Type: ");
                String type = sc.nextLine();
                sql = "SELECT * FROM properties WHERE type=? AND status != 'sold'";
                cfg.viewRecords(sql, headers, columns, type);
                break;
            default:
                System.out.println("Invalid search option!");
        }
    }

    private static void recordSale() {
        try {
            System.out.print("Enter Property ID: ");
            int propertyId = sc.nextInt(); sc.nextLine();
            
            String checkSql = "SELECT * FROM properties WHERE property_id=? AND status='available'";
            if (!cfg.recordExists(checkSql, propertyId)) {
                System.out.println("Property not found or not available for sale!");
                return;
            }

            System.out.print("Enter Client ID: ");
            int clientId = sc.nextInt(); sc.nextLine();

            System.out.print("Enter Sale Price: ");
            double salePrice = sc.nextDouble(); sc.nextLine();

            System.out.print("Enter Commission Rate (e.g., 0.05 for 5%): ");
            double commissionRate = sc.nextDouble(); sc.nextLine();

            double commissionAmount = salePrice * commissionRate;

            String saleSql = "INSERT INTO sales (property_id, agent_id, client_id, sale_price, commission_rate, commission_amount, sale_date) VALUES (?,?,?,?,?,?, date('now'))";
            String updatePropertySql = "UPDATE properties SET status='sold' WHERE property_id=?";
            
            cfg.addRecord(saleSql, propertyId, currentUserId, clientId, salePrice, commissionRate, commissionAmount);
            cfg.updateRecord(updatePropertySql, propertyId);
            
            System.out.println("Sale recorded successfully! Commission: $" + commissionAmount);
            
        } catch (Exception e) {
            System.out.println("Error recording sale: " + e.getMessage());
        }
    }

    private static void manageProperties() {
        System.out.println("1. View All Properties");
        System.out.println("2. Delete Property");
        System.out.println("3. Update Property Status");
        System.out.print("Choose option: ");
        
        int choice = sc.nextInt(); sc.nextLine();
        switch (choice) {
            case 1: viewAllProperties(); break;
            case 2: deleteProperty(); break;
            case 3: updatePropertyStatus(); break;
        }
    }

    private static void updatePropertyStatus() {
        System.out.print("Enter Property ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Enter New Status (available/sold/pending/reserved): ");
        String status = sc.nextLine();
        
        String sql = "UPDATE properties SET status=? WHERE property_id=?";
        cfg.updateRecord(sql, status, id);
        System.out.println("Property status updated!");
    }

    private static void scheduleAppointment() {
        try {
            System.out.print("Enter Property ID: ");
            int propertyId = sc.nextInt(); sc.nextLine();
            
            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = sc.nextLine();
            System.out.print("Enter Time (HH:MM): ");
            String time = sc.nextLine();
            
            String appointmentDateTime = date + " " + time;
            
            int clientId = currentUserId;
            if (!currentRole.equalsIgnoreCase("Client")) {
                System.out.print("Enter Client ID: ");
                clientId = sc.nextInt(); sc.nextLine();
            }

            String sql = "INSERT INTO appointments (property_id, agent_id, client_id, appointment_date) VALUES (?,?,?,?)";
            
            cfg.addRecord(sql, propertyId, currentUserId, clientId, appointmentDateTime);
            System.out.println("Appointment scheduled successfully!");
            
        } catch (Exception e) {
            System.out.println("Error scheduling appointment: " + e.getMessage());
        }
    }

    private static void viewMyAppointments() {
        String sql;
        String[] headers;
        String[] columns;
        
        if (currentRole.equalsIgnoreCase("Agent")) {
            sql = "SELECT a.appointment_id, p.title, u.username as client_name, a.appointment_date, a.status " +
                  "FROM appointments a " +
                  "JOIN properties p ON a.property_id = p.property_id " +
                  "JOIN users u ON a.client_id = u.user_id " +
                  "WHERE a.agent_id = ? ORDER BY a.appointment_date";
            headers = new String[]{"ID", "Property", "Client", "Date", "Status"};
            columns = new String[]{"appointment_id", "title", "client_name", "appointment_date", "status"};
        } else {
            sql = "SELECT a.appointment_id, p.title, u.username as agent_name, a.appointment_date, a.status " +
                  "FROM appointments a " +
                  "JOIN properties p ON a.property_id = p.property_id " +
                  "JOIN users u ON a.agent_id = u.user_id " +
                  "WHERE a.client_id = ? ORDER BY a.appointment_date";
            headers = new String[]{"ID", "Property", "Agent", "Date", "Status"};
            columns = new String[]{"appointment_id", "title", "agent_name", "appointment_date", "status"};
        }
        
        cfg.viewRecords(sql, headers, columns, currentUserId);
    }

    private static void viewCommissions() {
        String sql;
        String[] headers;
        String[] columns;
        
        if(currentRole.equalsIgnoreCase("Admin")) {
            sql = "SELECT u.username, SUM(s.commission_amount) AS total_commission, COUNT(s.sale_id) AS sales_count " +
                  "FROM sales s JOIN users u ON s.agent_id=u.user_id " +
                  "GROUP BY u.user_id, u.username";
            headers = new String[]{"Agent", "Total Commission", "Sales Count"};
            columns = new String[]{"username", "total_commission", "sales_count"};
            cfg.viewRecords(sql, headers, columns);
        } else {
            sql = "SELECT SUM(commission_amount) AS my_commission, COUNT(sale_id) AS my_sales " +
                  "FROM sales WHERE agent_id=?";
            headers = new String[]{"My Commission", "My Sales"};
            columns = new String[]{"my_commission", "my_sales"};
            cfg.viewRecords(sql, headers, columns, currentUserId);
        }
    }

    private static void manageUsers() {
        String sql = "SELECT user_id, username, email, role, first_name, last_name, is_active FROM users";
        String[] headers = {"ID", "Username", "Email", "Role", "First Name", "Last Name", "Active"};
        String[] columns = {"user_id", "username", "email", "role", "first_name", "last_name", "is_active"};
        cfg.viewRecords(sql, headers, columns);
    }

    private static void manageClients() {
        String sql = "SELECT user_id, username, email, first_name, last_name, phone FROM users WHERE role='Client'";
        String[] headers = {"ID", "Username", "Email", "First Name", "Last Name", "Phone"};
        String[] columns = {"user_id", "username", "email", "first_name", "last_name", "phone"};
        cfg.viewRecords(sql, headers, columns);
    }

    private static void viewSalesReports() {
        String sql = "SELECT s.sale_id, p.title, u1.username as agent, u2.username as client, " +
                     "s.sale_price, s.commission_amount, s.sale_date " +
                     "FROM sales s " +
                     "JOIN properties p ON s.property_id = p.property_id " +
                     "JOIN users u1 ON s.agent_id = u1.user_id " +
                     "JOIN users u2 ON s.client_id = u2.user_id " +
                     "ORDER BY s.sale_date DESC";
        String[] headers = {"Sale ID", "Property", "Agent", "Client", "Sale Price", "Commission", "Date"};
        String[] columns = {"sale_id", "title", "agent", "client", "sale_price", "commission_amount", "sale_date"};
        cfg.viewRecords(sql, headers, columns);
    }

    private static void generateReports() {
        System.out.println("=== SYSTEM REPORTS ===");
        
        System.out.println("\nProperties by Status:");
        String sql = "SELECT status, COUNT(*) as count FROM properties GROUP BY status";
        String[] headers = {"Status", "Count"};
        String[] columns = {"status", "count"};
        cfg.viewRecords(sql, headers, columns);
        
        System.out.println("\nMonthly Sales:");
        sql = "SELECT strftime('%m', sale_date) as month, SUM(sale_price) as total_sales, SUM(commission_amount) as total_commissions " +
              "FROM sales GROUP BY strftime('%m', sale_date)";
        headers = new String[]{"Month", "Total Sales", "Total Commissions"};
        columns = new String[]{"month", "total_sales", "total_commissions"};
        cfg.viewRecords(sql, headers, columns);
    }

    private static void updateProperty() {
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

    private static void logout() {
        loggedIn = false;
        currentUser = "";
        currentRole = "";
        currentUserId = 0;
        System.out.println("Logged out successfully!");
    }
    
    private static void documentManagement() {
    System.out.println("\n=== DOCUMENT MANAGEMENT ===");
    System.out.println("1. Upload Document");
    System.out.println("2. View Documents");
    System.out.println("3. Search Documents");
    System.out.println("4. Delete Document");
    System.out.println("5. Back to Main Menu");
    System.out.print("Choose option: ");
    
    int choice = sc.nextInt(); sc.nextLine();
    switch (choice) {
        case 1: uploadDocument(); break;
        case 2: viewDocuments(); break;
        case 3: searchDocuments(); break;
        case 4: deleteDocument(); break;
        case 5: return;
        default: System.out.println("Invalid choice!");
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
        Integer fileSize = sizeInput.isEmpty() ? null : Integer.parseInt(sizeInput);
        
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
                  "WHERE d.property_id = ? " +
                  "ORDER BY d.uploaded_at DESC";
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
                  "WHERE d.document_type LIKE ? " +
                  "ORDER BY d.uploaded_at DESC";
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
                  "WHERE u.username LIKE ? " +
                  "ORDER BY d.uploaded_at DESC";
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

private static void communicationSystem() {
    System.out.println("\n=== COMMUNICATION SYSTEM ===");
    System.out.println("1. Send Message");
    System.out.println("2. Inbox");
    System.out.println("3. Sent Messages");
    System.out.println("4. View Notifications");
    System.out.println("5. Back to Main Menu");
    System.out.print("Choose option: ");
    
    int choice = sc.nextInt(); sc.nextLine();
    switch (choice) {
        case 1: sendMessage(); break;
        case 2: viewInbox(); break;
        case 3: viewSentMessages(); break;
        case 4: viewNotifications(); break;
        case 5: return;
        default: System.out.println("Invalid choice!");
    }
}

private static void sendMessage() {
    try {
        String usersSql = "SELECT user_id, username, role FROM users WHERE user_id != ?";
        String[] headers = {"User ID", "Username", "Role"};
        String[] columns = {"user_id", "username", "role"};
        System.out.println("\nAvailable Users:");
        cfg.viewRecords(usersSql, headers, columns, currentUserId);
        
        System.out.print("Enter recipient User ID: ");
        int recipientId = sc.nextInt(); sc.nextLine();
        
        System.out.print("Enter message subject: ");
        String subject = sc.nextLine();
        
        System.out.print("Enter message: ");
        String message = sc.nextLine();
        
        String sql = "INSERT INTO messages (sender_id, recipient_id, subject, message) VALUES (?,?,?,?)";
        cfg.addRecord(sql, currentUserId, recipientId, subject, message);
        
        String notifSql = "INSERT INTO notifications (user_id, notification_type, title, message) VALUES (?, 'message', 'New Message', ?)";
        cfg.addRecord(notifSql, recipientId, "You have a new message from " + currentUser);
        
        System.out.println("Message sent successfully!");
        
    } catch (Exception e) {
        System.out.println("Error sending message: " + e.getMessage());
    }
}

private static void viewInbox() {
    String sql = "SELECT m.message_id, u.username as sender, m.subject, m.message, m.sent_at, m.is_read " +
                 "FROM messages m " +
                 "JOIN users u ON m.sender_id = u.user_id " +
                 "WHERE m.recipient_id = ? " +
                 "ORDER BY m.sent_at DESC";
    
    String[] headers = {"Msg ID", "Sender", "Subject", "Message", "Sent At", "Read"};
    String[] columns = {"message_id", "sender", "subject", "message", "sent_at", "is_read"};
    cfg.viewRecords(sql, headers, columns, currentUserId);
    
    String updateSql = "UPDATE messages SET is_read=1 WHERE recipient_id=? AND is_read=0";
    cfg.updateRecord(updateSql, currentUserId);
}

private static void viewSentMessages() {
    String sql = "SELECT m.message_id, u.username as recipient, m.subject, m.message, m.sent_at " +
                 "FROM messages m " +
                 "JOIN users u ON m.recipient_id = u.user_id " +
                 "WHERE m.sender_id = ? " +
                 "ORDER BY m.sent_at DESC";
    
    String[] headers = {"Msg ID", "Recipient", "Subject", "Message", "Sent At"};
    String[] columns = {"message_id", "recipient", "subject", "message", "sent_at"};
    cfg.viewRecords(sql, headers, columns, currentUserId);
}

private static void viewNotifications() {
    String sql = "SELECT notification_id, notification_type, title, message, created_at, is_read " +
                 "FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
    
    String[] headers = {"Notif ID", "Type", "Title", "Message", "Created At", "Read"};
    String[] columns = {"notification_id", "notification_type", "title", "message", "created_at", "is_read"};
    cfg.viewRecords(sql, headers, columns, currentUserId);
    
    String updateSql = "UPDATE notifications SET is_read=1 WHERE user_id=? AND is_read=0";
    cfg.updateRecord(updateSql, currentUserId);
}

private static void reportingAnalytics() {
    System.out.println("\n=== REPORTING & ANALYTICS ===");
    System.out.println("1. Sales Performance Report");
    System.out.println("2. Agent Performance Report");
    System.out.println("3. Property Performance Report");
    System.out.println("4. Financial Summary");
    System.out.println("5. System Statistics");
    System.out.println("6. Back to Main Menu");
    System.out.print("Choose option: ");
    
    int choice = sc.nextInt(); sc.nextLine();
    switch (choice) {
        case 1: salesPerformanceReport(); break;
        case 2: agentPerformanceReport(); break;
        case 3: propertyPerformanceReport(); break;
        case 4: financialSummary(); break;
        case 5: systemStatistics(); break;
        case 6: return;
        default: System.out.println("Invalid choice!");
    }
}

private static void salesPerformanceReport() {
    System.out.println("\n=== SALES PERFORMANCE REPORT ===");
    
    System.out.println("\nMonthly Sales Trend:");
    String sql = "SELECT strftime('%Y-%m', sale_date) as month, " +
                 "COUNT(sale_id) as total_sales, " +
                 "SUM(sale_price) as total_revenue, " +
                 "SUM(commission_amount) as total_commissions " +
                 "FROM sales " +
                 "GROUP BY strftime('%Y-%m', sale_date) " +
                 "ORDER BY month DESC";
    String[] headers = {"Month", "Total Sales", "Total Revenue", "Total Commissions"};
    String[] columns = {"month", "total_sales", "total_revenue", "total_commissions"};
    cfg.viewRecords(sql, headers, columns);
    
    System.out.println("\nTop Performing Properties:");
    sql = "SELECT p.title, p.type, p.location, " +
          "COUNT(s.sale_id) as times_sold, " +
          "AVG(s.sale_price) as avg_sale_price " +
          "FROM sales s " +
          "JOIN properties p ON s.property_id = p.property_id " +
          "GROUP BY p.property_id " +
          "ORDER BY times_sold DESC " +
          "LIMIT 10";
    headers = new String[]{"Property", "Type", "Location", "Times Sold", "Avg Sale Price"};
    columns = new String[]{"title", "type", "location", "times_sold", "avg_sale_price"};
    cfg.viewRecords(sql, headers, columns);
}

private static void agentPerformanceReport() {
    System.out.println("\n=== AGENT PERFORMANCE REPORT ===");
    
    String sql = "SELECT u.username, u.first_name, u.last_name, " +
                 "COUNT(s.sale_id) as total_sales, " +
                 "SUM(s.sale_price) as total_sales_volume, " +
                 "SUM(s.commission_amount) as total_commissions, " +
                 "AVG(s.commission_amount) as avg_commission " +
                 "FROM sales s " +
                 "JOIN users u ON s.agent_id = u.user_id " +
                 "WHERE u.role = 'Agent' " +
                 "GROUP BY u.user_id " +
                 "ORDER BY total_commissions DESC";
    
    String[] headers = {"Agent", "First Name", "Last Name", "Total Sales", "Sales Volume", "Total Commissions", "Avg Commission"};
    String[] columns = {"username", "first_name", "last_name", "total_sales", "total_sales_volume", "total_commissions", "avg_commission"};
    cfg.viewRecords(sql, headers, columns);
}

private static void propertyPerformanceReport() {
    System.out.println("\n=== PROPERTY PERFORMANCE REPORT ===");
    
    System.out.println("Properties by Status:");
    String sql = "SELECT status, COUNT(*) as count, " +
                 "AVG(price) as avg_price, " +
                 "SUM(price) as total_value " +
                 "FROM properties " +
                 "GROUP BY status";
    String[] headers = {"Status", "Count", "Average Price", "Total Value"};
    String[] columns = {"status", "count", "avg_price", "total_value"};
    cfg.viewRecords(sql, headers, columns);
   
    System.out.println("\nProperties by Type:");
    sql = "SELECT type, COUNT(*) as count, " +
          "AVG(price) as avg_price " +
          "FROM properties " +
          "GROUP BY type " +
          "ORDER BY count DESC";
    headers = new String[]{"Type", "Count", "Average Price"};
    columns = new String[]{"type", "count", "avg_price"};
    cfg.viewRecords(sql, headers, columns);
}

private static void financialSummary() {
    System.out.println("\n=== FINANCIAL SUMMARY ===");
    
    String sql = "SELECT " +
                 "COUNT(sale_id) as total_sales, " +
                 "SUM(sale_price) as total_revenue, " +
                 "SUM(commission_amount) as total_commissions, " +
                 "AVG(sale_price) as avg_sale_price, " +
                 "AVG(commission_amount) as avg_commission " +
                 "FROM sales";
    
    String[] headers = {"Total Sales", "Total Revenue", "Total Commissions", "Avg Sale Price", "Avg Commission"};
    String[] columns = {"total_sales", "total_revenue", "total_commissions", "avg_sale_price", "avg_commission"};
    cfg.viewRecords(sql, headers, columns);
    
    System.out.println("\nMonthly Financial Trend (Last 6 Months):");
    sql = "SELECT strftime('%Y-%m', sale_date) as month, " +
          "SUM(sale_price) as monthly_revenue, " +
          "SUM(commission_amount) as monthly_commissions " +
          "FROM sales " +
          "WHERE sale_date >= date('now', '-6 months') " +
          "GROUP BY strftime('%Y-%m', sale_date) " +
          "ORDER BY month DESC";
    headers = new String[]{"Month", "Monthly Revenue", "Monthly Commissions"};
    columns = new String[]{"month", "monthly_revenue", "monthly_commissions"};
    cfg.viewRecords(sql, headers, columns);
}

private static void systemStatistics() {
    System.out.println("\n=== SYSTEM STATISTICS ===");
    
    String userStats = "SELECT role, COUNT(*) as count FROM users WHERE is_active=1 GROUP BY role";
    System.out.println("User Statistics:");
    String[] headers = {"Role", "Count"};
    String[] columns = {"role", "count"};
    cfg.viewRecords(userStats, headers, columns);
    
    String propStats = "SELECT COUNT(*) as total_properties, " +
                      "SUM(CASE WHEN status='available' THEN 1 ELSE 0 END) as available_properties, " +
                      "SUM(CASE WHEN status='sold' THEN 1 ELSE 0 END) as sold_properties " +
                      "FROM properties";
    System.out.println("\nProperty Statistics:");
    headers = new String[]{"Total Properties", "Available", "Sold"};
    columns = new String[]{"total_properties", "available_properties", "sold_properties"};
    cfg.viewRecords(propStats, headers, columns);
    
    System.out.println("\nRecent Activity (Last 7 Days):");
    String recentSales = "SELECT COUNT(*) as recent_sales FROM sales WHERE sale_date >= date('now', '-7 days')";
    String recentProps = "SELECT COUNT(*) as new_properties FROM properties WHERE created_at >= datetime('now', '-7 days')";
    String recentUsers = "SELECT COUNT(*) as new_users FROM users WHERE created_at >= datetime('now', '-7 days')";

    System.out.println("Recent Sales: " + getSingleValue(recentSales, "recent_sales"));
    System.out.println("New Properties: " + getSingleValue(recentProps, "new_properties"));
    System.out.println("New Users: " + getSingleValue(recentUsers, "new_users"));
}

private static String getSingleValue(String sql, String column) {
    try {
        List<Map<String, Object>> results = cfg.fetchRecords(sql);
        if (!results.isEmpty()) {
            return results.get(0).get(column).toString();
        }
    } catch (Exception e) {
        System.out.println("Error getting value: " + e.getMessage());
    }
    return "0";
}

}