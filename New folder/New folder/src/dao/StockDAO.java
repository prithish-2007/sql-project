package src.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import src.database.DBConnection;

public class StockDAO {

    public static boolean addStock(int medicineId, String batchNumber, int quantity,
                                   LocalDate expiryDate, int minimumQuantity) {
        if (quantity <= 0 || minimumQuantity < 0 || expiryDate == null) {
            System.out.println("Invalid stock details.");
            return false;
        }
        String sql = "INSERT INTO STOCK (medicine_id, batch_number, quantity, expiry_date, minimum_quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, medicineId);
                ps.setString(2, batchNumber);
                ps.setInt(3, quantity);
                ps.setDate(4, Date.valueOf(expiryDate));
                ps.setInt(5, minimumQuantity);
                boolean success = ps.executeUpdate() > 0;
                if (success) System.out.println("Stock added successfully!");
                return success;
            }
        } catch (Exception e) {
            System.out.println("Error adding stock: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateStockQuantity(int stockId, int newQuantity, int staffId, String remarks) {
        if (newQuantity < 0) {
            System.out.println("Quantity cannot be negative!");
            return false;
        }
        String selectSql = "SELECT quantity FROM STOCK WHERE stock_id = ?";
        String updateSql = "UPDATE STOCK SET quantity = ? WHERE stock_id = ?";
        String historySql = "INSERT INTO STOCK_HISTORY (stock_id, staff_id, action_type, quantity_changed, remarks) VALUES (?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return false;
            con.setAutoCommit(false);
            int oldQuantity;
            try (PreparedStatement ps = con.prepareStatement(selectSql)) {
                ps.setInt(1, stockId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Stock ID not found!");
                        con.rollback();
                        return false;
                    }
                    oldQuantity = rs.getInt("quantity");
                }
            }
            int difference = newQuantity - oldQuantity;
            String action = difference > 0 ? "INCREASE" : difference < 0 ? "DECREASE" : "NO_CHANGE";
            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setInt(1, newQuantity);
                ps.setInt(2, stockId);
                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }
            try (PreparedStatement ps = con.prepareStatement(historySql)) {
                ps.setInt(1, stockId);
                ps.setInt(2, staffId);
                ps.setString(3, action);
                ps.setInt(4, difference);
                ps.setString(5, remarks);
                ps.executeUpdate();
            }
            con.commit();
            System.out.println("Stock quantity updated successfully!");
            System.out.println("Old Quantity: " + oldQuantity + ", New Quantity: " + newQuantity + ", Action: " + action);
            return true;
        } catch (Exception e) {
            System.out.println("Error updating stock: " + e.getMessage());
            if (con != null) try { con.rollback(); } catch (Exception ignored) { }
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    public static void viewStock() {
        String sql = "SELECT m.medicine_id, m.medicine_name, m.category, s.stock_id, s.batch_number, s.quantity, s.expiry_date, s.minimum_quantity FROM MEDICINE m JOIN STOCK s ON m.medicine_id = s.medicine_id ORDER BY m.medicine_name";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                System.out.println("\n========== MEDICINE STOCK REPORT ==========");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("ID: %d | %s | Category: %s | Stock ID: %d | Batch: %s | Qty: %d | Expiry: %s | Minimum: %d%n",
                        rs.getInt("medicine_id"), rs.getString("medicine_name"), rs.getString("category"),
                        rs.getInt("stock_id"), rs.getString("batch_number"), rs.getInt("quantity"),
                        rs.getDate("expiry_date"), rs.getInt("minimum_quantity"));
                }
                if (!found) System.out.println("No stock records found.");
            }
        } catch (Exception e) { System.out.println("Error viewing stock: " + e.getMessage()); }
    }

    public static void checkExpiryDates() {
        String sql = "SELECT m.medicine_name, s.batch_number, s.quantity, s.expiry_date FROM MEDICINE m JOIN STOCK s ON m.medicine_id = s.medicine_id ORDER BY s.expiry_date";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                LocalDate today = LocalDate.now();
                System.out.println("\n========== EXPIRY DATE REPORT ==========");
                while (rs.next()) {
                    LocalDate expiry = rs.getDate("expiry_date").toLocalDate();
                    long days = ChronoUnit.DAYS.between(today, expiry);
                    String status = days < 0 ? "EXPIRED" : days <= 30 ? "EXPIRING SOON" : "SAFE";
                    System.out.printf("%s | Batch: %s | Qty: %d | Expiry: %s | Days left: %d | %s%n",
                        rs.getString("medicine_name"), rs.getString("batch_number"), rs.getInt("quantity"), expiry, days, status);
                }
            }
        } catch (Exception e) { System.out.println("Error checking expiry: " + e.getMessage()); }
    }

    public static void checkLowStock() {
        String sql = "SELECT m.medicine_name, s.batch_number, s.quantity, s.minimum_quantity FROM MEDICINE m JOIN STOCK s ON m.medicine_id = s.medicine_id WHERE s.quantity <= s.minimum_quantity";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                System.out.println("\n========== LOW STOCK REPORT ==========");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%s | Batch: %s | Available: %d | Minimum: %d%n", rs.getString("medicine_name"), rs.getString("batch_number"), rs.getInt("quantity"), rs.getInt("minimum_quantity"));
                }
                if (!found) System.out.println("No low-stock medicines found.");
            }
        } catch (Exception e) { System.out.println("Error checking low stock: " + e.getMessage()); }
    }

    public static void viewExpiredStock() {
        String sql = "SELECT m.medicine_name, s.batch_number, s.quantity, s.expiry_date FROM MEDICINE m JOIN STOCK s ON m.medicine_id = s.medicine_id WHERE s.expiry_date < CURDATE() ORDER BY s.expiry_date";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                System.out.println("\n========== EXPIRED STOCK REPORT ==========");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%s | Batch: %s | Qty: %d | Expiry: %s%n", rs.getString("medicine_name"), rs.getString("batch_number"), rs.getInt("quantity"), rs.getDate("expiry_date"));
                }
                if (!found) System.out.println("No expired stock found!");
            }
        } catch (Exception e) { System.out.println("Error viewing expired stock: " + e.getMessage()); }
    }

    public static void viewStockHistory() {
        String sql = "SELECT h.stock_id, m.medicine_name, h.staff_id, h.action_type, h.quantity_changed, h.transaction_date, h.remarks FROM STOCK_HISTORY h JOIN STOCK s ON h.stock_id = s.stock_id JOIN MEDICINE m ON s.medicine_id = m.medicine_id ORDER BY h.transaction_date DESC";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                System.out.println("\n========== STOCK HISTORY ==========");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("Stock ID: %d | Medicine: %s | Staff ID: %d | Action: %s | Change: %d | Date: %s | Remarks: %s%n",
                        rs.getInt("stock_id"), rs.getString("medicine_name"), rs.getInt("staff_id"), rs.getString("action_type"), rs.getInt("quantity_changed"), rs.getTimestamp("transaction_date"), rs.getString("remarks"));
                }
                if (!found) System.out.println("No stock history found.");
            }
        } catch (Exception e) { System.out.println("Error viewing stock history: " + e.getMessage()); }
    }
}
