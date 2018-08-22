package com.example.plugin;

import java.util.List;
import org.json.JSONObject;

public class ScreenScrapingResult {

    private String balance;
    private JSONObject movements;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public JSONObject getMovements() {
        return movements;
    }

    public void setMovements(JSONObject movements) {
        this.movements = movements;
    }
}