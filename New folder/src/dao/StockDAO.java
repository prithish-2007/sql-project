
package src.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import src.database.DBConnection;

public class StockDAO {

    // 1. ADD MEDICINE STOCK
    public static boolean addStock(
            int medicineId,
            String batchNumber,
            int quantity,
            LocalDate expiryDate,
            int minimumQuantity) {

        String sql = "INSERT INTO STOCK "
                + "(medicine_id, batch_number, quantity, expiry_date, minimum_quantity) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection con = DBConnection.getConnection();

        if (con == null) {
            return false;
        }

        try (con;
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, medicineId);
            ps.setString(2, batchNumber);
            ps.setInt(3, quantity);
            ps.setDate(4, Date.valueOf(expiryDate));
            ps.setInt(5, minimumQuantity);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            System.out.println("Error adding stock!");
            e.printStackTrace();
            return false;
        }
    }


    // 2. UPDATE STOCK QUANTITY
    public static boolean updateStockQuantity(
            int stockId,
            int newQuantity,
            int staffId,
            String remarks) {

        String selectSQL =
                "SELECT quantity FROM STOCK WHERE stock_id = ?";

        String updateSQL =
                "UPDATE STOCK SET quantity = ? WHERE stock_id = ?";

        String historySQL =
                "INSERT INTO STOCK_HISTORY "
                + "(stock_id, staff_id, action_type, quantity_changed, remarks) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection con = DBConnection.getConnection();

        if (con == null) {
            return false;
        }

        try {
            con.setAutoCommit(false);

            int oldQuantity;

            try (PreparedStatement selectPS =
                    con.prepareStatement(selectSQL)) {

                selectPS.setInt(1, stockId);

                try (ResultSet rs = selectPS.executeQuery()) {

                    if (!rs.next()) {
                        con.rollback();
                        System.out.println("Stock ID not found!");
                        return false;
                    }

                    oldQuantity = rs.getInt("quantity");
                }
            }

            if (newQuantity < 0) {
                con.rollback();
                System.out.println("Quantity cannot be negative!");
                return false;
            }

            try (PreparedStatement updatePS =
                    con.prepareStatement(updateSQL)) {

                updatePS.setInt(1, newQuantity);
                updatePS.setInt(2, stockId);

                updatePS.executeUpdate();
            }

            int difference = newQuantity - oldQuantity;

            String action;

            if (difference > 0) {
                action = "INCREASE";
            } else if (difference < 0) {
                action = "DECREASE";
            } else {
                action = "NO_CHANGE";
            }

            try (PreparedStatement historyPS =
                    con.prepareStatement(historySQL)) {

                historyPS.setInt(1, stockId);
                historyPS.setInt(2, staffId);
                historyPS.setString(3, action);
                historyPS.setInt(4, difference);
                historyPS.setString(5, remarks);

                historyPS.executeUpdate();
            }

            con.commit();

            System.out.println("Stock updated successfully!");
            return true;

        } catch (Exception e) {

            try {
                con.rollback();
            } catch (Exception rollbackError) {
                rollbackError.printStackTrace();
            }

            System.out.println("Error updating stock!");
            e.printStackTrace();

            return false;

        } finally {

            try {
                con.setAutoCommit(true);
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 3. VIEW MEDICINE STOCK
    public static void viewStock() {

        String sql =
                "SELECT m.medicine_id, m.medicine_name, m.category, "
                + "s.stock_id, s.batch_number, s.quantity, "
                + "s.expiry_date, s.minimum_quantity "
                + "FROM MEDICINE m "
                + "JOIN STOCK s ON m.medicine_id = s.medicine_id "
                + "ORDER BY m.medicine_name";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("===== MEDICINE STOCK REPORT =====");

                while (rs.next()) {

                    System.out.println("------------------------------");

                    System.out.println("Medicine ID: "
                            + rs.getInt("medicine_id"));

                    System.out.println("Medicine Name: "
                            + rs.getString("medicine_name"));

                    System.out.println("Category: "
                            + rs.getString("category"));

                    System.out.println("Stock ID: "
                            + rs.getInt("stock_id"));

                    System.out.println("Batch Number: "
                            + rs.getString("batch_number"));

                    System.out.println("Quantity: "
                            + rs.getInt("quantity"));

                    System.out.println("Expiry Date: "
                            + rs.getDate("expiry_date"));

                    System.out.println("Minimum Quantity: "
                            + rs.getInt("minimum_quantity"));
                }
            }

        } catch (Exception e) {
            System.out.println("Error viewing stock!");
            e.printStackTrace();
        }
    }


    // 4. CHECK EXPIRY DATES
    public static void checkExpiryDates() {

        String sql =
                "SELECT m.medicine_name, s.batch_number, "
                + "s.quantity, s.expiry_date "
                + "FROM MEDICINE m "
                + "JOIN STOCK s ON m.medicine_id = s.medicine_id "
                + "ORDER BY s.expiry_date";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                LocalDate today = LocalDate.now();

                System.out.println("===== EXPIRY DATE REPORT =====");

                while (rs.next()) {

                    String medicineName =
                            rs.getString("medicine_name");

                    String batch =
                            rs.getString("batch_number");

                    int quantity =
                            rs.getInt("quantity");

                    LocalDate expiry =
                            rs.getDate("expiry_date").toLocalDate();

                    long daysLeft =
                            ChronoUnit.DAYS.between(today, expiry);

                    String status;

                    if (daysLeft < 0) {
                        status = "EXPIRED";
                    } else if (daysLeft <= 30) {
                        status = "EXPIRING SOON";
                    } else {
                        status = "SAFE";
                    }

                    System.out.println("------------------------------");
                    System.out.println("Medicine: " + medicineName);
                    System.out.println("Batch: " + batch);
                    System.out.println("Quantity: " + quantity);
                    System.out.println("Expiry Date: " + expiry);
                    System.out.println("Days Left: " + daysLeft);
                    System.out.println("Status: " + status);
                }
            }

        } catch (Exception e) {
            System.out.println("Error checking expiry dates!");
            e.printStackTrace();
        }
    }


    // 5. CHECK LOW STOCK
    public static void checkLowStock() {

        String sql =
                "SELECT m.medicine_name, s.stock_id, "
                + "s.batch_number, s.quantity, s.minimum_quantity "
                + "FROM MEDICINE m "
                + "JOIN STOCK s ON m.medicine_id = s.medicine_id "
                + "WHERE s.quantity <= s.minimum_quantity";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("===== LOW STOCK REPORT =====");

                boolean found = false;

                while (rs.next()) {

                    found = true;

                    System.out.println("------------------------------");

                    System.out.println("Medicine: "
                            + rs.getString("medicine_name"));

                    System.out.println("Stock ID: "
                            + rs.getInt("stock_id"));

                    System.out.println("Batch Number: "
                            + rs.getString("batch_number"));

                    System.out.println("Current Quantity: "
                            + rs.getInt("quantity"));

                    System.out.println("Minimum Quantity: "
                            + rs.getInt("minimum_quantity"));

                    System.out.println("Status: LOW STOCK");
                }

                if (!found) {
                    System.out.println("No low-stock medicines found.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error checking low stock!");
            e.printStackTrace();
        }
    }
    public static void viewExpiredStock() {

    String sql = "SELECT m.medicine_name, s.batch_number, " +
                 "s.quantity, s.expiry_date " +
                 "FROM MEDICINE m " +
                 "JOIN STOCK s ON m.medicine_id = s.medicine_id " +
                 "WHERE s.expiry_date < CURDATE()";

    try {
        java.sql.Connection con = src.database.DBConnection.getConnection();

        if (con == null) {
            return;
        }

        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        java.sql.ResultSet rs = ps.executeQuery();

        boolean found = false;

        System.out.println("\n========== EXPIRED STOCK REPORT ==========");

        while (rs.next()) {
            found = true;

            System.out.println("--------------------------------");
            System.out.println("Medicine Name: " + rs.getString("medicine_name"));
            System.out.println("Batch Number: " + rs.getString("batch_number"));
            System.out.println("Quantity: " + rs.getInt("quantity"));
            System.out.println("Expiry Date: " + rs.getDate("expiry_date"));
        }

        if (!found) {
            System.out.println("No expired stock found!");
        }

        rs.close();
        ps.close();
        con.close();

    } catch (Exception e) {
        System.out.println("Error displaying expired stock:");
        e.printStackTrace();
    }
}
public static void viewStockHistory() {

    String sql = "SELECT h.stock_id, m.medicine_name, " +
                 "h.staff_id, h.action_type, h.quantity_changed, " +
                 "h.transaction_date, h.remarks " +
                 "FROM STOCK_HISTORY h " +
                 "JOIN STOCK s ON h.stock_id = s.stock_id " +
                 "JOIN MEDICINE m ON s.medicine_id = m.medicine_id " +
                 "ORDER BY h.transaction_date DESC";

    try {
        java.sql.Connection con = src.database.DBConnection.getConnection();

        if (con == null) {
            return;
        }

        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        java.sql.ResultSet rs = ps.executeQuery();

        boolean found = false;

        System.out.println("\n========== STOCK HISTORY ==========");

        while (rs.next()) {
            found = true;

            System.out.println("--------------------------------");
            System.out.println("Stock ID: " + rs.getInt("stock_id"));
            System.out.println("Medicine Name: " + rs.getString("medicine_name"));
            System.out.println("Staff ID: " + rs.getInt("staff_id"));
            System.out.println("Action: " + rs.getString("action_type"));
            System.out.println("Quantity Changed: " + rs.getInt("quantity_changed"));
            System.out.println("Transaction Date: " + rs.getTimestamp("transaction_date"));
            System.out.println("Remarks: " + rs.getString("remarks"));
        }

        if (!found) {
            System.out.println("No stock history found!");
        }

        rs.close();
        ps.close();
        con.close();

    } catch (Exception e) {
        System.out.println("Error displaying stock history:");
        e.printStackTrace();
    }
}

}
