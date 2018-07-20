package com.example.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.os.AsyncTask;
import com.example.plugin.ScreenScrapingResult;

import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Hello extends CordovaPlugin {

    // we need this callback when Task will finish
    private CallbackContext mMyCallbackContext = null; 

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("aLacard")) {    
            this.mMyCallbackContext = callbackContext;
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT); 
            pluginResult.setKeepCallback(true); 

            try {
                String user = data.getString(0);
                String[] splited = user.split("\\s+");
                new ScrapeAlacardData().execute(splited[0], splited[1]);
            } catch (Exception e) {
                e.printStackTrace();
                PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, "result.getBalance()");
                pluginresult.setKeepCallback(false);
                String message = "Erro a processar as strings";
                mMyCallbackContext.success(message);
            }

            mMyCallbackContext.sendPluginResult(pluginResult);
            return true;

        } else if (action.equals("caixa")){
            this.mMyCallbackContext = callbackContext;
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT); 
            pluginResult.setKeepCallback(true); 

            try{
                String user = data.getString(0);
                String[] splited = user.split("\\s+");
                new ScrapeCaixaData ().execute(splited[0], splited[1]);
            }catch (Exception e) {
                e.printStackTrace();
                PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, "result.getBalance()");
                pluginresult.setKeepCallback(false);
                String message = "Erro a processar as strings";
                mMyCallbackContext.success(message);
            }

            mMyCallbackContext.sendPluginResult(pluginResult);
            return true;
        } else{
            return false;
        }
    }

    private class ScrapeAlacardData extends AsyncTask<String, Void, ScreenScrapingResult> {
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

                // lorem ipsum
                if (splited != null && splited.length > 0) {
                    saldoDisponivel = splited[5];
                    saldoDeConta.setBalance(saldoDisponivel);
                }

            } catch (IOException e) {
                e.printStackTrace();
                PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, "result.getBalance()");
                pluginresult.setKeepCallback(false);
                String message = "Erro a processar o pedido";
                mMyCallbackContext.success(message);
            }

            return saldoDeConta;
        }

        @Override
        protected void onPostExecute(ScreenScrapingResult result) {
            PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, result.getBalance());
            pluginresult.setKeepCallback(false); 
            //mMyCallbackContext.sendPluginResult(pluginresult);

            //String message = "O Seu Saldo é de: " + result.getBalance() + " A la card";
            String message = result.getBalance();
            mMyCallbackContext.success(message);
        }
    }

    private class ScrapeCaixaData extends AsyncTask<String, Void, ScreenScrapingResult> {
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
                String loginFormUrl = "https://www.cgd.pt/Particulares/Pages/Particulares_v2.aspx";
                String loginForm2Url = "https://caixadirectaonline.cgd.pt/cdo/login.seam";
                String loginActionUrl = "https://caixadirectaonline.cgd.pt/cdo/auth/forms/login.fcc";

                Connection.Response loginForm = Jsoup.connect(loginFormUrl)
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .execute();

                Map<String, String> loginCookies = loginForm.cookies();

                Connection.Response loginForm2 = Jsoup.connect(loginForm2Url)
                        .method(Connection.Method.GET)
                        .cookies(loginCookies)
                        .userAgent(USER_AGENT)
                        .execute();

                loginCookies.putAll(loginForm2.cookies());

                Connection.Response login = Jsoup.connect(loginActionUrl)
                        .userAgent(USER_AGENT)
                        .data("loginForm:submit", "Entrar")
                        .data("userInput", params[0])
                        .data("passwordInput", params[1])
                        .data("target","/cdo/private/home.seam")
                        .data("username","CDO" + params[0])
                        .data("password",params[1])
                        .cookies(loginCookies)
                        .method(Connection.Method.POST)
                        .execute();
                loginCookies.putAll(login.cookies());

                Document doc = Jsoup.connect("https://caixadirectaonline.cgd.pt/cdo/private/contasaordem/consultaSaldosMovimentos.seam")
                        .cookies(loginCookies)
                        .userAgent(USER_AGENT)
                        .get();


                Elements elements = doc.select("div[class=saldos contabilistico]");
                if (elements.size() > 0) {
                    String values = elements.first().text();
                    String[] splited = values.split("\\s+");

                    if (splited != null && splited.length > 0) {
                        saldoDisponivel = splited[2];
                        saldoDeConta.setBalance(saldoDisponivel);
                    }
                } else
                    saldoDeConta.setBalance("Erro");

            } catch (IOException e) {
                e.printStackTrace();
                PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, "result.getBalance()");
                pluginresult.setKeepCallback(false);
                String message = "Erro da caixa";
                mMyCallbackContext.success(message);
            }

            return saldoDeConta;
        }

        @Override
        protected void onPostExecute(ScreenScrapingResult result) {
            PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, result.getBalance());
            pluginresult.setKeepCallback(false); 
            String message = "O Seu Saldo é de: " + result.getBalance() + "Caixa";
            mMyCallbackContext.success(message);
        }
    }
}