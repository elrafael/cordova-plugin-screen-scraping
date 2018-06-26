package com.carbon.pagescraping;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("greet")) {

            new ScrapeData().execute("4339660104222190", "160585");

            return true;

        } else {

            return false;

        }
    }

    private class ScrapeData extends AsyncTask<String, Void, ScreenScrapingResult> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ScreenScrapingResult doInBackground(String... params) {
            String saldoDisponivel = " ";
            ScreenScrapingResult saldoDeConta = new ScreenScrapingResult();

            try {
                final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";
                String loginFormUrl = "https://www.myedenred.pt/euroticket/pages/login.jsf";
                String loginActionUrl = "https://www.myedenred.pt/euroticket/pages/login.jsf?windowId=cd3";
                //String username = "4339660104222190";
                //String password = "160585";

                Connection.Response loginForm = Jsoup.connect(loginFormUrl)
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .execute();

                Map<String, String> loginCookies = loginForm.cookies();

                Document loginDoc = loginForm.parse();
                String authToken = loginDoc.select("input[name=javax.faces.ViewState]")
                        .first()
                        .attr("value");

                Connection.Response login = Jsoup.connect(loginActionUrl)
                        .userAgent(USER_AGENT)
                        .data("loginform", "loginform")
                        .data("loginform:username", params[0])
                        .data("loginform:password", params[1])
                        .data("loginform:loginButton", "Entrar")
                        .data("javax.faces.ViewState", authToken)
                        .cookies(loginCookies)
                        .method(Connection.Method.POST)
                        .execute();
                loginCookies.putAll(login.cookies());

                Document doc = Jsoup.connect("https://www.myedenred.pt/euroticket/pages/private/customer/customer.jsf?windowId=cd3")
                        .cookies(loginCookies)
                        .userAgent(USER_AGENT)
                        .get();

                String values = doc.select("div[class=content]").first().text();
                String[] splited = values.split("\\s+");


                if (splited != null && splited.length > 0) {
                    saldoDisponivel = splited[5];
                    saldoDeConta.setBalance(saldoDisponivel);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return saldoDeConta;
        }

        @Override
        protected void onPostExecute(ScreenScrapingResult result) {
            PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, result);
            pluginresult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginresult);
        }
    }
}
