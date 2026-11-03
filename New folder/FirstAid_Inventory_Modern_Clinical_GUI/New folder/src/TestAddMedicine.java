
package src;

import java.util.Scanner;
import src.dao.MedicineDAO;

public class TestAddMedicine {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== ADD MEDICINE =====");

        System.out.print("Enter medicine name: ");
        String name = sc.nextLine();

        System.out.print("Enter category: ");
        String category = sc.nextLine();

        System.out.print("Enter description: ");
        String description = sc.nextLine();

        boolean success = MedicineDAO.addMedicine(
            name, category, description
        );

        if (success) {
            System.out.println("Medicine added successfully!");
        } else {
            System.out.println("Failed to add medicine.");
        }

        sc.close();
    }
}
