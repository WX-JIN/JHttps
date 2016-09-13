package com.soubw.jhttps;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author WX_JIN
 * wangxiaojin@soubw.com
 * http://soubw.com
 */
public class JOkHttp {

    private static final String TAG = "JHttps";

    private static final int TIMEOUT_CONNECTION = 10000;

    private static final int TIMEOUT_WRITE = 10000;

    private static final int TIMEOUT_READ = 10000;

    private static OkHttpClient okHttpClient;

    /**
     * 允许所有证书 通用版本
     *
     * @param context
     * @param url
     */
    public static void doOkHttpCommon(Context context, String url) {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.MILLISECONDS)
                .hostnameVerifier(JCommonSSL.DO_NOT_VERIFY)//add
                .sslSocketFactory(JCommonSSL.getCommonSSLSocketFactory())//add
                .build();
        doHttpRequest(url);
    }


    /**
     * 校验自家产的证书(两种一个证书文件，一种是证书数据)
     *
     * @param context
     * @param url
     */
    public static void doOkHttpSelf(Context context, String url) {
        JCertificate.readAssetsCertificate(context);//读取证书，可以有很多中形式去加载证书，详情请看JCertificate
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.MILLISECONDS)
                .sslSocketFactory(JSSLSocketFactory.getSSLSocketFactory())//add
                .build();

        doHttpRequest(url);

    }

    private static void doHttpRequest(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody reqBody = new FormBody.Builder()
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(reqBody)
                        .build();
                Response response;
                try {
                    response = okHttpClient.newCall(request).execute();
                    Log.d(TAG, "response.code()=" + response.code());
                    System.out.print("response.code()=" + response.code());
                    Log.d(TAG, "toString=" + response.toString());
                    System.out.print("toString=" + response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "错误=" + e.getMessage());
                    System.out.print( "错误=" + e.getMessage());
                }
            }
        }).start();
    }

}
