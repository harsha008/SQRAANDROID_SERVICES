package com.adient.mobility.sqra.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MySingleTon {

    public static Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> sqrcData;
    //private static Map<String, Map<String, Map<String, String>>> secondLevelData;
    private static Map<String, Map<String, ArrayList<String>>> modelData;
    //private static Map<String, ArrayList<String>> secondLevelData;
    private static ArrayList<String> componentData;
    private static Map<String, Map<String, Map<String, Map<String, String>>>> secondLevelData;
    private static Map<String, Map<String, Map<String, String>>> yearData;
    public static Map<String, Map<String, String>> yearDetailData;
    private static Map<String, String> res_detail_yr;
    public static boolean rememberMe;
    public static String smKey;
    public static String json;
    public static  boolean firstLogin = false;

    public static Map<String, Map<String, Map<String, Map<String, String>>>> parseJson2ndLevel(String rowValue) {
        secondLevelData = sqrcData.get(rowValue);
        return secondLevelData;
    }

    public static Map<String, Map<String, Map<String, String>>> parseJsonYear(String data) {
        yearData = secondLevelData.get(data);
        return yearData;
    }

    public static Map<String, Map<String, String>> parseJsonYearDetail(String data) {
        yearDetailData = yearData.get(data);
        return yearDetailData;
    }

    public static Map<String, String> parseJsonYearFlowDetail(String rowValueSticky, String rowValueYr, String rowValue) {

        secondLevelData = sqrcData.get(rowValueSticky);
        yearData = secondLevelData.get(rowValueYr);
        yearDetailData = yearData.get(rowValueYr);

       for (Map.Entry<String, Map<String, String>> entry : yearDetailData.entrySet()) {
           String key = entry.getKey();
           if (rowValue.equals(key)) {
               res_detail_yr= entry.getValue();
           }
        }

        return res_detail_yr;
    }



    public static boolean isNetworkOnline(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }

    public static void smAuthenticate(String user, String password) {
        OkHttpClient client = new OkHttpClient();

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add(MyAppConstants.USER, user);
        formEncodingBuilder.add(MyAppConstants.PASSWORD, password);
        RequestBody formBody = formEncodingBuilder.build();

        Request request = new Request.Builder()
                .url(MyAppConstants.PRE_LOGIN_URL)
                .post(formBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            smKey = response.priorResponse().priorResponse().header(MyAppConstants.SET_COOKIE);
        } catch (IOException e) {
            Log.d("UtilHelper:", "Fail in smAuthenticate: " + e.getMessage());
        }
    }

    public static void retrieveRegions() {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Cookie", smKey)
                .url(MyAppConstants.PRE_LOGIN_URL_1)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            json = response.body().string();
        } catch (IOException e) {
            Log.d("UtilHelper:", "Fail in retrieveModels: " + e.getMessage());
        }
    }
}