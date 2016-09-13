package com.soubw.jhttps;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author WX_JIN
 * wangxiaojin@soubw.com
 * http://soubw.com
 */
public class JHttpsURLConnection {
    private static final String TAG = "JHttps";

    /**
     * @param httpUrl
     * @param isCommon 是否采用全部运行的方式进行请求
     */
    public static void doHttpsURLConnection(String httpUrl, boolean isCommon) {
        BufferedReader input = null;
        StringBuilder sb;
        URL url;
        HttpURLConnection con = null;
        try {
            url = new URL(httpUrl);
            try {
                if (isCommon) {
                    HttpsURLConnection.setDefaultSSLSocketFactory(JCommonSSL.getCommonSSLSocketFactory());
                } else {
                    HttpsURLConnection.setDefaultSSLSocketFactory(JSSLSocketFactory.getSSLSocketFactory());
                }

                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                if (url.getProtocol().toLowerCase().equals("https")) {
                    https.setHostnameVerifier(JCommonSSL.DO_NOT_VERIFY);
                    con = https;
                } else {
                    con = (HttpURLConnection) url.openConnection();
                }
                input = new BufferedReader(new InputStreamReader(con.getInputStream()));
                sb = new StringBuilder();
                String s;
                while ((s = input.readLine()) != null) {
                    sb.append(s).append("\n");
                }
                Log.d(TAG, "con.getResponseCode()11:" + con.getResponseCode());
                Log.d(TAG, "result:" + sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "IOException:" + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
