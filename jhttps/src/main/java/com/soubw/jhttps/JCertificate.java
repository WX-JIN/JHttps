package com.soubw.jhttps;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WX_JIN
 * @email wangxiaojin@soubw.com
 * @link http://soubw.com
 */
public class JCertificate {

    private static final String TAG = "JHttps";

    /**
     * 证书数据，byte列表
     */
    private static List<byte[]> CERTIFICATES_BYTE_DATA = new ArrayList<>();
    /**
     * 证书数据，String列表
     */
    private static List<String> CERTIFICATES_STRING_DATA = new ArrayList<>();
    /**
     * 证书数据，InputStream列表
     */
    private static List<InputStream> CERTIFICATES_INPUT_STREAM_DATA = new ArrayList<>();


    /**
     * 该方法提供范例，读取放在Assets中certs文件下所有证书的数据流放入在byte列表中
     *
     * @param context
     */
    public static void readAssetsCertificate(Context context) {
        try {
            String[] certFiles = context.getAssets().list("certs");
            if (certFiles != null) {
                for (String cert : certFiles) {
                    InputStream is = context.getAssets().open("certs/" + cert);
                    addCertificatesInputStreamData(is);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 添加https证书数据流放入在byte列表中
     *
     * @param inputStream
     */
    public synchronized static void addInputStreamCertificate(InputStream inputStream) {
        Log.e(TAG, "JCertificate-addInputStreamCertificate : " + inputStream);
        if (inputStream != null) {
            try {
                int available;
                int len = 0;
                ArrayList<byte[]> data = new ArrayList<>();
                while ((available = inputStream.available()) > 0) {
                    byte[] buffer = new byte[available];
                    inputStream.read(buffer);
                    data.add(buffer);
                    len += available;
                }
                byte[] buff = new byte[len];
                int dstPos = 0;
                for (byte[] bytes : data) {
                    int length = bytes.length;
                    System.arraycopy(bytes, 0, buff, dstPos, length);
                    dstPos += length;
                }
                CERTIFICATES_BYTE_DATA.add(buff);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "JCertificate-addInputStreamCertificate-IOException : " + e.getMessage());
            }
        }
    }

    public synchronized static void addCertificatesByteData(byte[] certificatesByteData) {
        if (certificatesByteData == null || certificatesByteData.equals(""))
            return;
        CERTIFICATES_BYTE_DATA.add(certificatesByteData);
    }

    public synchronized static void addCertificatesByteData(List<byte[]> certificatesByteData) {
        if (certificatesByteData == null || certificatesByteData.isEmpty())
            return;
        CERTIFICATES_BYTE_DATA.addAll(certificatesByteData);
    }

    public synchronized static void addCertificatesStringData(String certificatesStringData) {
        if (certificatesStringData == null || certificatesStringData.equals("") || certificatesStringData.isEmpty())
            return;
        CERTIFICATES_STRING_DATA.add(certificatesStringData);
    }

    public synchronized static void addCertificatesStringData(List<String> certificatesStringData) {
        if (certificatesStringData == null || certificatesStringData.isEmpty())
            return;
        CERTIFICATES_STRING_DATA.addAll(certificatesStringData);
    }

    public synchronized static void addCertificatesInputStreamData(InputStream certificatesInputStreamData) {
        if (certificatesInputStreamData == null || certificatesInputStreamData.equals("") )
            return;
        CERTIFICATES_INPUT_STREAM_DATA.add(certificatesInputStreamData);
    }

    public synchronized static void addCertificatesInputStreamData(List<InputStream> certificatesInputStreamData) {
        if (certificatesInputStreamData == null || certificatesInputStreamData.isEmpty())
            return;
        CERTIFICATES_INPUT_STREAM_DATA.addAll(certificatesInputStreamData);
    }

    public static List<byte[]> getCertificatesByteData() {
        return CERTIFICATES_BYTE_DATA;
    }

    public static List<String> getCertificatesStringData() {
        return CERTIFICATES_STRING_DATA;
    }

    public static List<InputStream> getCertificatesInputStreamData() {
        return CERTIFICATES_INPUT_STREAM_DATA;
    }
}
