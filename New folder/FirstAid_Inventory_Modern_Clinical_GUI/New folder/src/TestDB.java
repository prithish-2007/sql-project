
package src;

import java.sql.Connection;
import src.database.DBConnection;

public class TestDB {

    public static void main(String[] args) {

        System.out.println("Testing MySQL Connection...");

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("JDBC is working successfully!");

            try {
                con.close();
                System.out.println("Connection closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Please check your database settings.");
        }
    }
}
