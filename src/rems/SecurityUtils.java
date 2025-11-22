package rems;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class SecurityUtils {
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone.matches("\\d{11}");
    }
    
    public static boolean isStrongPassword(String password) {
        return password.length() >= 8 && 
               password.matches(".*[A-Z].*") && 
               password.matches(".*[a-z].*") && 
               password.matches(".*[0-9].*");
    }
    
    public static boolean verifyPassword(String password, String hashed) {
    return hashPassword(password).equals(hashed);
}

}