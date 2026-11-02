
package src;

import java.util.Scanner;
import src.dao.MedicineDAO;

public class TestMedicineExists {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Medicine ID: ");
        int id = sc.nextInt();

        if (MedicineDAO.medicineExists(id)) {
            System.out.println("Valid Medicine ID!");
        } else {
            System.out.println("Invalid Medicine ID!");
        }

        sc.close();
    }
}

