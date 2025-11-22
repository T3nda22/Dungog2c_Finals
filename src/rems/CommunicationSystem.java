package rems;

import config.config;
import java.util.Scanner;

public class CommunicationSystem {

    private static final Scanner sc = new Scanner(System.in);
    private static final config cfg = new config();
    private static int currentUserId;
    private static String currentUser;

    public static void showMenu(int userId, String username) {
        currentUserId = userId;
        currentUser = username;

        while (true) {
            System.out.println("\n=== COMMUNICATION SYSTEM ===");
            System.out.println("1. Send Message");
            System.out.println("2. Inbox");
            System.out.println("3. Sent Messages");
            System.out.println("4. View Notifications");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: sendMessage();
                    break;
                case 2: viewInbox();
                    break;
                case 3: viewSentMessages();
                    break;
                case 4: viewNotifications();
                    break;
                case 5: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    // ============================================
    //                 SEND MESSAGE
    // ============================================
    private static void sendMessage() {
        try {
            System.out.println("\nAvailable Users:");
            String usersSql = "SELECT user_id, username, role FROM users WHERE user_id != ?";
            String[] headers = {"User ID", "Username", "Role"};
            String[] columns = {"user_id", "username", "role"};
            cfg.viewRecords(usersSql, headers, columns, currentUserId);

            System.out.print("Enter Recipient User ID: ");
            int recipientId = sc.nextInt();
            sc.nextLine();

            // Verify if recipient exists
            String checkSql = "SELECT user_id FROM users WHERE user_id = ?";
            if (!cfg.recordExists(checkSql, recipientId)) {
                System.out.println("❌ Recipient not found!");
                return;
            }

            System.out.print("Enter message subject: ");
            String subject = sc.nextLine();

            System.out.print("Enter message: ");
            String message = sc.nextLine();

            // Insert message into DB
            String sql = "INSERT INTO messages (sender_id, recipient_id, subject, message) VALUES (?,?,?,?)";
            cfg.addRecord(sql, currentUserId, recipientId, subject, message);

            // Create notification
            String notifSql =
                "INSERT INTO notifications (user_id, notification_type, title, message) " +
                "VALUES (?, 'message', 'New Message', ?)";
            String notifMsg = "You have a new message from " + currentUser;
            cfg.addRecord(notifSql, recipientId, notifMsg);

            System.out.println("📨 Message sent successfully!");

        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    // ============================================
    //                   INBOX
    // ============================================
    private static void viewInbox() {
        String sql =
            "SELECT m.message_id, u.username AS sender, m.subject, m.message, " +
            "m.sent_at, m.is_read " +
            "FROM messages m " +
            "JOIN users u ON m.sender_id = u.user_id " +
            "WHERE m.recipient_id = ? " +
            "ORDER BY m.sent_at DESC";

        String[] headers = {"Msg ID", "Sender", "Subject", "Message", "Sent At", "Read"};
        String[] columns = {"message_id", "sender", "subject", "message", "sent_at", "is_read"};
        cfg.viewRecords(sql, headers, columns, currentUserId);

        // Mark all unread messages as read
        String updateSql = "UPDATE messages SET is_read = 1 WHERE recipient_id = ? AND is_read = 0";
        cfg.updateRecord(updateSql, currentUserId);
    }

    // ============================================
    //               SENT MESSAGES
    // ============================================
    private static void viewSentMessages() {
        String sql =
            "SELECT m.message_id, u.username AS recipient, m.subject, m.message, m.sent_at " +
            "FROM messages m " +
            "JOIN users u ON m.recipient_id = u.user_id " +
            "WHERE m.sender_id = ? " +
            "ORDER BY m.sent_at DESC";

        String[] headers = {"Msg ID", "Recipient", "Subject", "Message", "Sent At"};
        String[] columns = {"message_id", "recipient", "subject", "message", "sent_at"};
        cfg.viewRecords(sql, headers, columns, currentUserId);
    }

    // ============================================
    //               NOTIFICATIONS
    // ============================================
    private static void viewNotifications() {
        String sql =
            "SELECT notification_id, notification_type, title, message, created_at, is_read " +
            "FROM notifications WHERE user_id = ? ORDER BY created_at DESC";

        String[] headers = {"Notif ID", "Type", "Title", "Message", "Created At", "Read"};
        String[] columns = {"notification_id", "notification_type", "title", "message", "created_at", "is_read"};
        cfg.viewRecords(sql, headers, columns, currentUserId);

        // Mark notifications as read
        String updateSql = "UPDATE notifications SET is_read = 1 WHERE user_id = ? AND is_read = 0";
        cfg.updateRecord(updateSql, currentUserId);
    }

}
