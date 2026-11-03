
package src;

import src.dao.StockDAO;

public class TestExpiry {

    public static void main(String[] args) {

        System.out.println("===== CHECK MEDICINE EXPIRY =====");

        StockDAO.checkExpiryDates();

    }
}
