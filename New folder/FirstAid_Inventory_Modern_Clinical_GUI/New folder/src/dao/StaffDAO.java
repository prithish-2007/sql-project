package src.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import src.database.DBConnection;

public class StaffDAO {

    public static boolean login(String username, String password) {

        String sql = "SELECT * FROM STAFF WHERE username = ? AND password_hash = ?";

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                return false;
            }

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            boolean valid = rs.next();

            if (valid) {
                System.out.println("Welcome, " + rs.getString("staff_name") + "!");
            }

            rs.close();
            ps.close();
            con.close();

            return valid;

        } catch (Exception e) {
            System.out.println("Login error:");
            e.printStackTrace();
            return false;
        }
    }
}