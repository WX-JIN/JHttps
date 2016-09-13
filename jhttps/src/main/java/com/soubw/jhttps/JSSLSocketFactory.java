package com.soubw.jhttps;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author WX_JIN
 * wangxiaojin@soubw.com
 * http://soubw.com
 */
public class JSSLSocketFactory {

    private static final String TAG = "JHttps";

    public static SSLSocketFactory getSSLSocketFactory() {

        List<InputStream> certificates = JCertificate.getCertificatesInputStreamData();
        List<byte[]> certs_data = JCertificate.getCertificatesByteData();
        List<String> certs_string = JCertificate.getCertificatesStringData();
        if (certs_data != null && !certs_data.isEmpty()) {
            for (byte[] bytes : certs_data) {
                certificates.add(new ByteArrayInputStream(bytes));
            }
        }
        if (certs_string != null && !certs_string.isEmpty()) {
            for (String s : certs_string) {
                certificates.add(new ByteArrayInputStream(s.getBytes()));
            }
        }
        if (certs_string != null && !certs_string.isEmpty()) {
            for (String s : certs_string) {
                certificates.add(new ByteArrayInputStream(s.getBytes()));
            }
        }
        SSLSocketFactory sslSocketFactory = getSocketFactory(certificates);
        return sslSocketFactory;
    }

    public static SSLSocketFactory getSocketFactory(List<InputStream> certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            try {
                for (int i = 0, size = certificates.size(); i < size; ) {
                    InputStream certificate = certificates.get(i);
                    String certificateAlias = Integer.toString(i++);
                    keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                    if (certificate != null)
                        certificate.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "JSSLSocketFactory-getSocketFactory-IOException : " + e.getMessage());
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "JSSLSocketFactory-getSocketFactory-Exception : " + e.getMessage());
        }
        return null;
    }

}
