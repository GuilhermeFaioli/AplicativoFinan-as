package com.ufop.guilherme.appfinanas;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class WebService {

    private ArrayList<Float> convesoes = new ArrayList<>();
    private Context context;
    private boolean encontrou;

    public WebService(Context context) {
        this.context = context;
        buscar();
    }

    public boolean isEncontrou() {
        return encontrou;
    }

    public void setEncontrou(boolean encontrou) {
        this.encontrou = encontrou;
    }

    public ArrayList<Float> getConvesoes() {
        return convesoes;
    }

    public void setConvesoes(ArrayList<Float> convesoes) {
        this.convesoes = convesoes;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public final void buscar() {

        // define a url
        String url = "https://api.exchangeratesapi.io/latest?base=BRL";

        // define os dados
        JSONObject obj = null;
        try {
            obj = new JSONObject(this.get(url));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!obj.has("erro")) {
            try {
                convesoes.add(Float.parseFloat(obj.getJSONObject("rates").getString("USD")));
                convesoes.add(Float.parseFloat(obj.getJSONObject("rates").getString("EUR")));
                convesoes.add(Float.parseFloat(obj.getJSONObject("rates").getString("GBP")));
                convesoes.add(Float.parseFloat(obj.getJSONObject("rates").getString("JPY")));
                encontrou = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            encontrou = false;
        }
    }

    private String get(String urlToRead) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

        } catch (MalformedURLException | ProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return result.toString();
    }
}
