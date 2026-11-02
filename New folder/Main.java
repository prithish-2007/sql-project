

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import src.dao.MedicineDAO;
import src.dao.StaffDAO;
import src.dao.StockDAO;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        System.out.println("====================================");
        System.out.println(" COLLEGE FIRST-AID INVENTORY SYSTEM");
        System.out.println("====================================");
        do {
            System.out.println("\n1. Add Medicine\n2. Add Medicine Stock\n3. Update Stock Quantity\n4. View Medicine Stock\n5. Check Expiry Dates\n6. Check Low Stock\n7. Search Medicine\n8. View Expired Stock Report\n9. View Stock History\n10. Staff Login\n0. Exit");
            choice = readInt(sc, "Enter your choice: ");
            switch (choice) {
                case 1 -> {
                    System.out.print("Medicine name: "); String name = sc.nextLine();
                    System.out.print("Category: "); String category = sc.nextLine();
                    System.out.print("Description: "); String description = sc.nextLine();
                    System.out.println(MedicineDAO.addMedicine(name, category, description) ? "Medicine added successfully!" : "Could not add medicine.");
                }
                case 2 -> {
                    int medicineId = readInt(sc, "Medicine ID: ");
                    if (!MedicineDAO.medicineExists(medicineId)) { System.out.println("Medicine ID does not exist."); break; }
                    System.out.print("Batch number: "); String batch = sc.nextLine();
                    int quantity = readInt(sc, "Quantity (greater than 0): ");
                    if (quantity <= 0) { System.out.println("Quantity must be greater than zero."); break; }
                    System.out.print("Expiry date (YYYY-MM-DD): ");
                    try {
                        LocalDate expiry = LocalDate.parse(sc.nextLine());
                        if (expiry.isBefore(LocalDate.now())) { System.out.println("Expiry date cannot be in the past."); break; }
                        int minimum = readInt(sc, "Minimum quantity (0 or more): ");
                        if (minimum < 0) { System.out.println("Minimum quantity cannot be negative."); break; }
                        if (!StockDAO.addStock(medicineId, batch, quantity, expiry, minimum)) System.out.println("Could not add stock.");
                    } catch (DateTimeParseException e) { System.out.println("Invalid date. Use YYYY-MM-DD."); }
                }
                case 3 -> {
                    int stockId = readInt(sc, "Stock ID: ");
                    int quantity = readInt(sc, "New quantity (0 or more): ");
                    if (quantity < 0) { System.out.println("Quantity cannot be negative."); break; }
                    int staffId = readInt(sc, "Staff ID: ");
                    System.out.print("Remarks: "); String remarks = sc.nextLine();
                    if (!StockDAO.updateStockQuantity(stockId, quantity, staffId, remarks)) System.out.println("Stock update failed.");
                }
                case 4 -> StockDAO.viewStock();
                case 5 -> StockDAO.checkExpiryDates();
                case 6 -> StockDAO.checkLowStock();
                case 7 -> { System.out.print("Enter medicine name to search: "); MedicineDAO.searchMedicine(sc.nextLine()); }
                case 8 -> StockDAO.viewExpiredStock();
                case 9 -> StockDAO.viewStockHistory();
                case 10 -> {
                    System.out.print("Username: "); String username = sc.nextLine();
                    System.out.print("Password: "); String password = sc.nextLine();
                    System.out.println(StaffDAO.login(username, password) ? "Login successful!" : "Invalid login details.");
                }
                case 0 -> System.out.println("Thank you. Exiting application.");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
        sc.close();
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try { return Integer.parseInt(input.trim()); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid whole number."); }
        }
    }
}
