package src;

import src.dao.StockDAO;

public class TestLowStock {

    public static void main(String[] args) {

        System.out.println("===== CHECK LOW STOCK =====");

        StockDAO.checkLowStock();
    }
}