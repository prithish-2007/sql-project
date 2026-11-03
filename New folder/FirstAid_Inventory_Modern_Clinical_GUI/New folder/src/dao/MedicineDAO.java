package src.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import src.database.DBConnection;

public class MedicineDAO {

    public static boolean addMedicine(String name, String category, String description) {
        String sql = "INSERT INTO MEDICINE (medicine_name, category, description) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, category);
                ps.setString(3, description);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Error adding medicine: " + e.getMessage());
            return false;
        }
    }

    public static boolean medicineExists(int medicineId) {
        String sql = "SELECT 1 FROM MEDICINE WHERE medicine_id = ?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, medicineId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking medicine ID: " + e.getMessage());
            return false;
        }
    }

    public static void searchMedicine(String medicineName) {
        String sql = "SELECT medicine_id, medicine_name, category, description FROM MEDICINE WHERE medicine_name LIKE ? ORDER BY medicine_name";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + medicineName + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    boolean found = false;
                    System.out.println("\n========== MEDICINE SEARCH ==========");
                    while (rs.next()) {
                        found = true;
                        System.out.println("Medicine ID: " + rs.getInt("medicine_id"));
                        System.out.println("Name: " + rs.getString("medicine_name"));
                        System.out.println("Category: " + rs.getString("category"));
                        System.out.println("Description: " + rs.getString("description"));
                        System.out.println("------------------------------------");
                    }
                    if (!found) System.out.println("No matching medicine found.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error searching medicine: " + e.getMessage());
        }
    }
}
