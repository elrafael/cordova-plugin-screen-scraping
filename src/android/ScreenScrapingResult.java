package com.example.plugin;

import java.util.List;

public class ScreenScrapingResult {

    private String balance;
    private List<String> movements;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<String> getMovements() {
        return movements;
    }

    public void setMovements(List<String> movements) {
        this.movements = movements;
    }
}
