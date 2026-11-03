
package src;

import java.util.Scanner;
import src.dao.StockDAO;

public class TestUpdateStock {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== UPDATE STOCK QUANTITY =====");

        System.out.print("Enter Stock ID: ");
        int stockId = sc.nextInt();

        System.out.print("Enter New Quantity: ");
        int newQuantity = sc.nextInt();

        System.out.print("Enter Staff ID: ");
        int staffId = sc.nextInt();

        sc.nextLine(); // Clear input buffer

        System.out.print("Enter Remarks: ");
        String remarks = sc.nextLine();

        boolean result = StockDAO.updateStockQuantity(
                stockId, newQuantity, staffId, remarks
        );

        if (result) {
            System.out.println("Stock update completed!");
        } else {
            System.out.println("Stock update failed!");
        }

        sc.close();
    }
}
