package rems;

import config.config;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReportingAnalytics {

    private static Scanner sc = new Scanner(System.in);
    private static config cfg = new config();

    public static void showMenu() {
        while (true) {
            System.out.println("\n=== REPORTING & ANALYTICS ===");
            System.out.println("1. Sales Performance Report");
            System.out.println("2. Agent Performance Report");
            System.out.println("3. Property Performance Report");
            System.out.println("4. Financial Summary");
            System.out.println("5. System Statistics");
            System.out.println("6. View Sales Report");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choose option: ");

            int choice = sc.nextInt(); 
            sc.nextLine();

            switch (choice) {
                case 1:
                    salesPerformanceReport();
                    break;
                case 2:
                    agentPerformanceReport();
                    break;
                case 3:
                    propertyPerformanceReport();
                    break;
                case 4:
                    financialSummary();
                    break;
                case 5:
                    systemStatistics();
                    break;
                case 6:
                    viewSalesReports();
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    public static void viewSalesReports() {
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

    // ============================
    // PLACE YOUR REPORT METHODS HERE
    // ============================

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

    void reportingAnalytics() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void generateReports() {
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
}
