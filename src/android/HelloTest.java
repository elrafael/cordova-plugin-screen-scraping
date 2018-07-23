package com.example.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.os.AsyncTask;
import com.example.plugin.ScreenScrapingResult;

import java.io.IOException;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HelloTest {
    public static void main(String args[]) {
        try {
            //Hello hello = new Hello();
            //hello.execute();
            System.out.println("Test!");
        } catch(Exception e) {}
    }
}