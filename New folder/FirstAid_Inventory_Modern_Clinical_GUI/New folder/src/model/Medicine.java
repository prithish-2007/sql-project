package src.model;

public class Medicine {

    private int medicineId;
    private String medicineName;
    private String category;
    private String description;

    public Medicine(int medicineId, String medicineName,
                    String category, String description) {

        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.category = category;
        this.description = description;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Medicine ID: " + medicineId
                + ", Name: " + medicineName
                + ", Category: " + category;
    }
}