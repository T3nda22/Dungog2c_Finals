package rems;

import config.config;
import java.sql.ResultSet;
import java.util.*;

public class Main {
    private static config cfg = new config();
    private static Scanner sc = new Scanner(System.in);
    private static boolean loggedIn = false;
    private static String currentUser = "";
    private static String currentRole = "";
    private static int currentUserId = 0;
    static UserManager userManager = new UserManager();
    static PropertyManager propertyManager = new PropertyManager();
    static CommunicationSystem communicationSystem = new CommunicationSystem();
    static DocumentManager documentManager = new DocumentManager();
    static ReportingAnalytics  reportingAnalytics = new ReportingAnalytics();
    static ClientManager clientManager = new ClientManager(cfg, sc);

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
    System.out.println("5. Document Management");
    System.out.println("6. Communication System"); 
    System.out.println("7. Reporting & Analytics");
    System.out.println("8. System Reports");
    System.out.println("9. My Profile"); 
    System.out.println("10. Change Password"); 
    System.out.println("11. Logout");
    System.out.print("Choose option: ");
    
    int choice = sc.nextInt(); sc.nextLine();
    switch (choice) {
        case 1: propertyManager.showMenu(); 
                break;
        case 2: userManager.manageUsers(); break;
        case 3: reportingAnalytics.viewSalesReports(); break;
        case 4: viewCommissions(); break;
        case 5: documentManager.showMenu(); break;  // Fixed
        case 6: communicationSystem.showMenu(currentUserId, currentUser); break;
        case 7: reportingAnalytics.showMenu();  break;
        case 8: reportingAnalytics.generateReports(); break;
        case 9: viewMyProfile(); break;
        case 10: changePassword(); break;
        case 11: logout(); break;
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
        System.out.println("10. Document Management");
        System.out.println("11. Communication System"); 
        System.out.println("12. Update Appointment Status");
        System.out.println("13. Cancel Appointment");
        System.out.println("14. My Profile");
        System.out.println("15. Change Password");
        System.out.println("16. Logout");
        System.out.print("Choose option: ");
        
        int choice = sc.nextInt(); sc.nextLine();
    switch (choice) {
                case 1: 
                    addProperty(); 
                        break;
                case 2: 
                    viewMyProperties();
                        break;
                case 3: 
                    searchProperty();
                        break;
                case 4: 
                    PropertyManager.updateProperty(); 
                        break;
                case 5: 
                    recordSale(); 
                        break;
                case 6: 
                    scheduleAppointment();
                        break;
                case 7: viewMyAppointments(); 
                        break;
                case 8: 
                    viewCommissions(); 
                        break;
                case 9: clientManager.showMenu(); break;
                case 10: documentManager.showMenu(); break;
                case 11: communicationSystem.showMenu(currentUserId, currentUser); break;
                case 12: updateAppointmentStatus(); break;  
                case 13: cancelAppointment(); break;        
                case 14: viewMyProfile(); break;            
                case 15: changePassword(); break;           
                case 16: logout(); break;                   
                default: System.out.println("Invalid choice!");
            }
}
    
    private static void showClientMenu() {
    System.out.println("1. View My Properties");
    System.out.println("2. Search Properties");
    System.out.println("3. Schedule Viewing");
    System.out.println("4. My Appointments");
    System.out.println("5. Communication System");
    System.out.println("6. Cancel Appointment");    
    System.out.println("7. My Profile");            
    System.out.println("8. change Password");   
    System.out.println("9. Logout");
    System.out.print("Choose option: ");

    int choice = sc.nextInt(); sc.nextLine();
    switch (choice) {
        case 1: viewMyProperties(); break;
        case 2: searchProperty(); break;
        case 3: scheduleAppointment(); break;
        case 4: viewMyAppointments(); break;
        case 5: communicationSystem.showMenu(currentUserId, currentUser); break;
        case 6: cancelAppointment(); break;        
        case 7: viewMyProfile(); break;             
        case 8: changePassword(); break;           
        case 9: logout(); break;                    
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
        String sql = "INSERT INTO users (username, email, password_hash, role, first_name, last_name, phone, status) VALUES (?,?,?,?,?,?,?,?)";
        
        cfg.addRecord(sql, username, email, hashedPassword, role, firstName, lastName, phone, "Pending");
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

        // Detect identifier type
        boolean isPhone = identifier.matches("\\d+");
        boolean isEmail = identifier.contains("@");
        boolean isUsername = !isPhone && !isEmail;

        // FIXED SQL: correct parentheses + check active status
        String checkUserSql =
            "SELECT * FROM users WHERE (username=? OR email=? OR phone=?) AND is_active=1";

        List<Map<String, Object>> userCheck = cfg.fetchRecords(checkUserSql, identifier, identifier, identifier);

        if (userCheck.isEmpty()) {
            System.out.println("No account found with: " + identifier);
            return;
        }

        // User exists, check status & role
        Map<String, Object> foundUser = userCheck.get(0);
        String status = foundUser.get("status").toString();
        String role = foundUser.get("role").toString();

        // ADMIN CAN ALWAYS LOGIN
        if (role.equalsIgnoreCase("Admin")) {
            System.out.println("Admin detected — bypassing account approval.");
        }
        // NON-ADMINS MUST BE APPROVED
        else if (!status.equalsIgnoreCase("Approved")) {
            System.out.println("Your account status is: " + status + ". You cannot log in until an admin approves your account.");
            return;
        }

        int attempts = 3;
        boolean loginSuccess = false;

        while (attempts > 0 && !loginSuccess) {
            System.out.print("Enter password (" + attempts + " attempts remaining): ");
            String password = sc.nextLine();
            String hashedPassword = SecurityUtils.hashPassword(password);

            String sql;
            List<Map<String, Object>> users;

            // LOGIN BY PHONE
            if (isPhone) {
                sql = "SELECT * FROM users WHERE phone=? AND password_hash=? AND is_active=1";
                users = cfg.fetchRecords(sql, identifier, hashedPassword);
            }
            // LOGIN BY USERNAME OR EMAIL
            else {
                sql = "SELECT * FROM users WHERE (username=? OR email=?) AND password_hash=? AND is_active=1";
                users = cfg.fetchRecords(sql, identifier, identifier, hashedPassword);
            }

            if (!users.isEmpty()) {
                Map<String, Object> user = users.get(0);

                // Set session details
                currentUser = user.get("username").toString();
                currentRole = user.get("role").toString();
                currentUserId = Integer.parseInt(user.get("user_id").toString());
                loggedIn = true;
                loginSuccess = true;

                String loginMethod = isPhone ? "phone" : (isEmail ? "email" : "username");
                System.out.println("Login successful! Welcome " + currentUser + " (logged in with " + loginMethod + ")");
                return;
            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println("Incorrect password. Try again.");
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
        String type = sc.nextLine().toLowerCase();

        // Validate type (optional but good practice)
        String[] allowedTypes = {"house", "lot", "condo", "apartment", "villa"};
        boolean validType = java.util.Arrays.asList(allowedTypes).contains(type);
        if (!validType) {
            System.out.println("Invalid type! Property not added.");
            return;
        }

        // ---- PRICE (SAFE INPUT) ----
        double price = 0;
        while (true) {
            try {
                System.out.print("Enter price: ");
                price = Double.parseDouble(sc.nextLine());
                break; // exit loop if correct
            } catch (Exception e) {
                System.out.println("Invalid price! Please enter numbers only.");
            }
        }

        System.out.print("Enter location: ");
        String location = sc.nextLine();

        // ---- SIZE (SAFE INPUT) ----
        int size = 0;
        while (true) {
            try {
                System.out.print("Enter size in sqft: ");
                size = Integer.parseInt(sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid size! Numbers only.");
            }
        }

        // ---- BEDROOMS (SAFE INPUT) ----
        int bedrooms = 0;
        while (true) {
            try {
                System.out.print("Enter number of bedrooms: ");
                bedrooms = Integer.parseInt(sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid input! Numbers only.");
            }
        }

        // ---- BATHROOMS (SAFE INPUT) ----
        int bathrooms = 0;
        while (true) {
            try {
                System.out.print("Enter number of bathrooms: ");
                bathrooms = Integer.parseInt(sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid input! Numbers only.");
            }
        }

        System.out.print("Enter description: ");
        String description = sc.nextLine();

        // ---- SQL INSERT ----
        String sql = "INSERT INTO properties (title, type, price, location, description, size_sqft, bedrooms, bathrooms, created_by) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        cfg.addRecord(sql,
                title,
                type,
                price,
                location,
                description,
                size,
                bedrooms,
                bathrooms,
                currentUserId
        );

        System.out.println("Property added successfully!");

    } catch (Exception e) {
        System.out.println("Error adding property:");
        e.printStackTrace();  // shows the real reason
    }
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

    private static void scheduleAppointment() {
    try {
        if (currentRole == null || currentUserId == 0) {
            System.out.println("You must be logged in to schedule an appointment!");
            return;
        }

        System.out.print("Enter Property ID: ");
        int propertyId = sc.nextInt();
        sc.nextLine();

        // Check if property exists
        String checkPropertySql = "SELECT title, status FROM properties WHERE property_id=?";
        ResultSet rs = cfg.queryRecord(checkPropertySql, propertyId);

        if (!rs.next()) {
            System.out.println("Property not found!");
            return;
        }

        String propertyTitle = rs.getString("title");
        String propertyStatus = rs.getString("status");

        if (!propertyStatus.equalsIgnoreCase("available")) {
            System.out.println("Property is not available for viewing!");
            return;
        }

        System.out.println("Scheduling appointment for property: " + propertyTitle);

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine().trim();

        System.out.print("Enter Time (HH:MM): ");
        String time = sc.nextLine().trim();

        String appointmentDateTime = date + " " + time;

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}") || !time.matches("\\d{2}:\\d{2}")) {
            System.out.println("Invalid date or time format!");
            return;
        }

        int clientId = currentUserId;
        if (!currentRole.equalsIgnoreCase("Client")) {
            System.out.print("Enter Client ID: ");
            clientId = sc.nextInt();
            sc.nextLine();
        }

        // Check for conflict
        String conflictCheckSql = "SELECT * FROM appointments WHERE client_id=? AND appointment_date=?";
        if (cfg.recordExists(conflictCheckSql, clientId, appointmentDateTime)) {
            System.out.println("You already have an appointment scheduled at that time!");
            return;
        }

        String sql = "INSERT INTO appointments (property_id, agent_id, client_id, appointment_date) VALUES (?, ?, ?, ?)";
        cfg.addRecord(sql, propertyId, currentUserId, clientId, appointmentDateTime);

        System.out.println("✅ Appointment scheduled successfully for " + appointmentDateTime + "!");

    } catch (Exception e) {
        System.out.println("⚠️ Error scheduling appointment: " + e.getMessage());
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

    private static void logout() {
        loggedIn = false;
        currentUser = "";
        currentRole = "";
        currentUserId = 0;
        System.out.println("Logged out successfully!");
    }

private static void viewMyProfile() {
    try {
        String sql = "SELECT user_id, username, email, role, first_name, last_name, phone, created_at " +
                     "FROM users WHERE user_id=?";
        
        String[] headers = {"ID", "Username", "Email", "Role", "First Name", "Last Name", "Phone", "Created At"};
        String[] columns = {"user_id", "username", "email", "role", "first_name", "last_name", "phone", "created_at"};
        
        cfg.viewRecords(sql, headers, columns, currentUserId);

    } catch (Exception e) {
        System.out.println("Error loading profile: " + e.getMessage());
    }
}


private static void updateMyProfile() {
    try {
        System.out.print("Enter new first name (press enter to keep current): ");
        String firstName = sc.nextLine();
        
        System.out.print("Enter new last name (press enter to keep current): ");
        String lastName = sc.nextLine();
        
        System.out.print("Enter new phone (press enter to keep current): ");
        String phone = sc.nextLine();
        
        if (!phone.isEmpty() && !phone.matches("\\d{11}")) {
            System.out.println("Phone must be 11 digits!");
            return;
        }
        
        String sql = "UPDATE users SET first_name=?, last_name=?, phone=? WHERE user_id=?";
        cfg.updateRecord(sql, 
            firstName.isEmpty() ? null : firstName,
            lastName.isEmpty() ? null : lastName,
            phone.isEmpty() ? null : phone,
            currentUserId
        );
        
        System.out.println("Profile updated successfully!");
        
    } catch (Exception e) {
        System.out.println("Error updating profile: " + e.getMessage());
    }
}

private static void changePassword() {
    try {
        System.out.print("Enter current password: ");
        String currentPassword = sc.nextLine();
        
        // Verify current password
        String verifySql = "SELECT password_hash FROM users WHERE user_id=?";
        List<Map<String, Object>> users = cfg.fetchRecords(verifySql, currentUserId);
        
        if (users.isEmpty()) {
            System.out.println("User not found!");
            return;
        }
        
        String currentHash = users.get(0).get("password_hash").toString();
        String enteredHash = SecurityUtils.hashPassword(currentPassword);
        
        if (!currentHash.equals(enteredHash)) {
            System.out.println("Current password is incorrect!");
            return;
        }
        
        // Get new password
        String newPassword;
        while (true) {
            System.out.print("Enter new password: ");
            newPassword = sc.nextLine();
            
            if (!SecurityUtils.isStrongPassword(newPassword)) {
                System.out.println("Password must be at least 8 characters with uppercase, lowercase and numbers!");
            } else {
                break;
            }
        }
        
        System.out.print("Confirm new password: ");
        String confirmPassword = sc.nextLine();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords don't match!");
            return;
        }
        
        String newHash = SecurityUtils.hashPassword(newPassword);
        String updateSql = "UPDATE users SET password_hash=? WHERE user_id=?";
        cfg.updateRecord(updateSql, newHash, currentUserId);
        
        System.out.println("Password changed successfully!");
        
    } catch (Exception e) {
        System.out.println("Error changing password: " + e.getMessage());
    }
}

private static void updateAppointmentStatus() {
    try {
        System.out.print("Enter Appointment ID: ");
        int appointmentId = sc.nextInt(); sc.nextLine();
        
        System.out.print("Enter new status (scheduled/completed/cancelled): ");
        String status = sc.nextLine();
        
        if (!status.equalsIgnoreCase("scheduled") && !status.equalsIgnoreCase("completed") && !status.equalsIgnoreCase("cancelled")) {
            System.out.println("Invalid status! Use: scheduled, completed, or cancelled");
            return;
        }
        
        String sql = "UPDATE appointments SET status=? WHERE appointment_id=?";
        cfg.updateRecord(sql, status.toLowerCase(), appointmentId);
        
        System.out.println("Appointment status updated!");
        
    } catch (Exception e) {
        System.out.println("Error updating appointment: " + e.getMessage());
    }
}

private static void cancelAppointment() {
    try {
        System.out.print("Enter Appointment ID to cancel: ");
        int appointmentId = sc.nextInt(); sc.nextLine();
        
        // Check if appointment exists and belongs to current user
        String checkSql;
        if (currentRole.equalsIgnoreCase("Agent")) {
            checkSql = "SELECT * FROM appointments WHERE appointment_id=? AND agent_id=?";
        } else {
            checkSql = "SELECT * FROM appointments WHERE appointment_id=? AND client_id=?";
        }
        
        if (!cfg.recordExists(checkSql, appointmentId, currentUserId)) {
            System.out.println("Appointment not found or you don't have permission to cancel it!");
            return;
        }
        
        String sql = "UPDATE appointments SET status='cancelled' WHERE appointment_id=?";
        cfg.updateRecord(sql, appointmentId);
        
        System.out.println("Appointment cancelled successfully!");
        
    } catch (Exception e) {
        System.out.println("Error cancelling appointment: " + e.getMessage());
    }
}

}