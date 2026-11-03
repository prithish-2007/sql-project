
package src.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import src.database.DBConnection;

public class MedicineDAO {

    public static boolean addMedicine(
            String name,
            String category,
            String description) {

        String sql = "INSERT INTO MEDICINE "
                   + "(medicine_name, category, description) "
                   + "VALUES (?, ?, ?)";

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                return false;
            }

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, category);
            ps.setString(3, description);

            int result = ps.executeUpdate();

            ps.close();
            con.close();

            return result > 0;

        } catch (Exception e) {
            System.out.println("Error adding medicine:");
            e.printStackTrace();
            return false;
        }
    }
    
public static boolean medicineExists(int medicineId) {

    String sql = "SELECT medicine_id FROM MEDICINE WHERE medicine_id = ?";

    try {
        Connection con = DBConnection.getConnection();

        if (con == null) {
            return false;
        }

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, medicineId);

        java.sql.ResultSet rs = ps.executeQuery();

        boolean exists = rs.next();

        rs.close();
        ps.close();
        con.close();

        return exists;

    } catch (Exception e) {
        System.out.println("Error checking Medicine ID:");
        e.printStackTrace();
        return false;
    }
}

public static void searchMedicine(String medicineName) {

    String sql = "SELECT * FROM MEDICINE WHERE medicine_name LIKE ?";

    try {
        java.sql.Connection con = src.database.DBConnection.getConnection();

        if (con == null) {
            return;
        }

        java.sql.PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, "%" + medicineName + "%");

        java.sql.ResultSet rs = ps.executeQuery();

        boolean found = false;

        while (rs.next()) {
            found = true;

            System.out.println("\n===== MEDICINE DETAILS =====");
            System.out.println("Medicine ID: " + rs.getInt("medicine_id"));
            System.out.println("Medicine Name: " + rs.getString("medicine_name"));
            System.out.println("Category: " + rs.getString("category"));
            System.out.println("Description: " + rs.getString("description"));
        }

        if (!found) {
            System.out.println("Medicine not found!");
        }

        rs.close();
        ps.close();
        con.close();

    } catch (Exception e) {
        System.out.println("Error searching medicine:");
        e.printStackTrace();
    }
}
}

