
package src;

import java.util.Scanner;
import java.time.LocalDate;
import src.dao.StockDAO;

public class TestAddStock {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== ADD MEDICINE STOCK =====");

        System.out.print("Enter Medicine ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Batch Number: ");
        String batch = sc.nextLine();

        System.out.print("Enter Quantity: ");
        int quantity = sc.nextInt();

        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
        String expiry = sc.next();

        System.out.print("Enter Minimum Quantity: ");
        int minimum = sc.nextInt();

        boolean success = StockDAO.addStock(
            id, batch, quantity, LocalDate.parse(expiry), minimum
        );

        if (success) {
            System.out.println("Stock added successfully!");
        } else {
            System.out.println("Failed to add stock.");
        }

        sc.close();
    }
}
