package src;
import src.model.Medicine;
import src.model.Stock;
import java.time.LocalDate;

public class TestMedicine {

    public static void main(String[] args) {

        Medicine medicine = new Medicine(
            1,
            "Paracetamol",
            "Tablet",
            "First-aid medicine"
        );

        System.out.println(medicine);

        Stock stock = new Stock(
            1,
            1,
            "B101",
            100,
            LocalDate.of(2027, 8, 15),
            20
        );

        System.out.println("Quantity: " + stock.getQuantity());

        System.out.println("Expiry Status: " + stock.getExpiryStatus());
    }
}