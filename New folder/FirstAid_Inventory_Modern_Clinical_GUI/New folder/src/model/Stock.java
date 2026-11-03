package src.model;

import java.time.LocalDate;

public class Stock {

    private int stockId;
    private int medicineId;
    private String batchNumber;
    private int quantity;
    private LocalDate expiryDate;
    private int minimumQuantity;

    // Constructor
    public Stock(int stockId, int medicineId,
                 String batchNumber, int quantity,
                 LocalDate expiryDate, int minimumQuantity) {

        this.stockId = stockId;
        this.medicineId = medicineId;
        this.batchNumber = batchNumber;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.minimumQuantity = minimumQuantity;
    }

    public int getStockId() {
        return stockId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public boolean isLowStock() {
        return quantity <= minimumQuantity;
    }

    public String getExpiryStatus() {

        LocalDate today = LocalDate.now();

        if (expiryDate.isBefore(today)) {
            return "Expired";
        }

        if (!expiryDate.isAfter(today.plusDays(30))) {
            return "Expiring Soon";
        }

        return "Safe";
    }
}