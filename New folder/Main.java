

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import src.dao.MedicineDAO;
import src.dao.StaffDAO;
import src.dao.StockDAO;


public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("=================================");
        System.out.println(" COLLEGE FIRST-AID INVENTORY");
        System.out.println("=================================");

        do {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Add Medicine");
            System.out.println("2. Add Medicine Stock");
            System.out.println("3. Update Stock Quantity");
            System.out.println("4. View Medicine Stock");
            System.out.println("5. Check Expiry Dates");
            System.out.println("6. Check Low Stock");
            System.out.println("7. Search Medicine");
            System.out.println("8. View Expired Stock Report");
            System.out.println("9. View Stock History");
            System.out.println("10. Staff Login");
            System.out.println("0. Exit");
            System.out.println("==============================");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("\n===== ADD MEDICINE =====");

                    System.out.print("Enter Medicine Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Category: ");
                    String category = scanner.nextLine();

                    System.out.print("Enter Description: ");
                    String description = scanner.nextLine();

                    boolean added = MedicineDAO.addMedicine(
                        name, category, description
                    );

                    if (added) {
                        System.out.println("Medicine added successfully!");
                    } else {
                        System.out.println("Failed to add medicine.");
                    }
                    break;

                case 2:
                    System.out.println("\n===== ADD MEDICINE STOCK =====");

                    System.out.print("Enter Medicine ID: ");
                    int medicineId = scanner.nextInt();
                    scanner.nextLine();

                    if (!MedicineDAO.medicineExists(medicineId)) {
                        System.out.println("Invalid Medicine ID! Medicine does not exist.");
                        break;
                    }

                    System.out.print("Enter Batch Number: ");
                    String batch = scanner.nextLine();

                    System.out.print("Enter Quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    if (quantity <= 0) {
                        System.out.println("Invalid quantity! Please enter a number greater than 0.");
                        break;
                    }

                    System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
                    String expiry = scanner.next();

                    try {
                        LocalDate date = LocalDate.parse(expiry);

                        if (date.isBefore(LocalDate.now())) {
                            System.out.println("Invalid expiry date! Please enter a future date.");
                            break;
                        }

                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format! Use YYYY-MM-DD.");
                        break;
                    }

                    System.out.print("Enter Minimum Quantity: ");
                    int minimum = scanner.nextInt();
                    scanner.nextLine();

                    if (minimum < 0) {
                        System.out.println("Invalid minimum quantity! It cannot be negative.");
                        break;
                    }

                    boolean stockAdded = StockDAO.addStock(
                        medicineId,
                        batch,
                        quantity,
                        LocalDate.parse(expiry),
                        minimum
                    );

                    if (stockAdded) {
                        System.out.println("Stock added successfully!");
                    } else {
                        System.out.println("Failed to add stock.");
                    }
                    break;

                case 3:
                    System.out.println("\n===== UPDATE STOCK QUANTITY =====");

                    System.out.print("Enter Stock ID: ");
                    int stockId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter New Quantity: ");
                    int newQuantity = scanner.nextInt();
                    scanner.nextLine();

                    if (newQuantity < 0) {
                        System.out.println("Invalid quantity! Quantity cannot be negative.");
                        break;
                    }

                    System.out.print("Enter Staff ID: ");
                    int staffId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter Remarks: ");
                    String remarks = scanner.nextLine();

                    boolean updated = StockDAO.updateStockQuantity(
                        stockId,
                        newQuantity,
                        staffId,
                        remarks
                    );

                    if (updated) {
                        System.out.println("Stock update completed!");
                    } else {
                        System.out.println("Stock update failed.");
                    }
                    break;

                case 4:
                    System.out.println("\n===== VIEW MEDICINE STOCK =====");
                    StockDAO.viewStock();
                    break;

                case 5:
                    System.out.println("\n===== CHECK EXPIRY DATES =====");
                    StockDAO.checkExpiryDates();
                    break;

                case 6:
                    System.out.println("\n===== CHECK LOW STOCK =====");
                    StockDAO.checkLowStock();
                    break;

                case 7:
                    System.out.println("\n===== SEARCH MEDICINE =====");

                    System.out.print("Enter Medicine Name: ");
                    String searchName = scanner.nextLine();

                    MedicineDAO.searchMedicine(searchName);
                    break;

                case 8:
                    System.out.println("\n===== EXPIRED STOCK REPORT =====");
                    StockDAO.viewExpiredStock();
                    break;

                case 9:
                    System.out.println("\n===== STOCK HISTORY =====");
                    StockDAO.viewStockHistory();
                    break;
                case 10:
    
    System.out.println("\n===== STAFF LOGIN =====");

    System.out.print("Enter Username: ");
    String username = scanner.nextLine();

    System.out.print("Enter Password: ");
    String password = scanner.nextLine();

    boolean loginSuccess = StaffDAO.login(username, password);

    if (loginSuccess) {
        System.out.println("Login successful!");
    } else {
        System.out.println("Invalid username or password!");
    }
    break;

                case 0:
                    System.out.println("Exiting application...");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 0);

        scanner.close();
    }
}