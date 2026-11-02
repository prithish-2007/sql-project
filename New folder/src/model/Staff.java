package src.model;

public class Staff {

    private int staffId;
    private String staffName;
    private String username;
    private String role;

    public Staff(int staffId, String staffName,
                 String username, String role) {

        this.staffId = staffId;
        this.staffName = staffName;
        this.username = username;
        this.role = role;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void displayRole() {
        System.out.println("Staff Role: " + role);
    }
}