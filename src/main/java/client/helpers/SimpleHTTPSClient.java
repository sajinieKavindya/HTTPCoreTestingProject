package client.helpers;

import client.ClientMain;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class SimpleHTTPSClient {

    private String host;
    private int port;

    public SimpleHTTPSClient() {

    }

    public SimpleHTTPSClient(String host, int port) {

        this.host = host;
        this.port = port;
    }

    // Create the and initialize the SSLContext
    private SSLContext createSSLContext() {

        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(ClientMain.keyStoreLocation), ClientMain.keyStorePassword.toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "wso2carbon".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();

            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            TrustManager[] tm = trustManagerFactory.getTrustManagers();

            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(km, tm, null);

            return sslContext;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Start to run the server
    public void run(String payload, RequestMethods method, String authorizationHeader, boolean enableChunking) {

        SSLContext sslContext = this.createSSLContext();

        try {
            // Create socket factory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Create socket
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(this.host, this.port);

            InputStream inputStream = sslSocket.getInputStream();
            OutputStream outputStream = sslSocket.getOutputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));

            sslSocket.setSendBufferSize(25000);
            // Write data
            System.out.println("Client sending a request!");

            printWriter.print(method + " /test/1.0.0 HTTP/1.1\r\n");
            printWriter.print("Accept: application/json\r\n");
            printWriter.print("Connection: keep-alive\r\n");
            printWriter.print(authorizationHeader + "\r\n");
            printWriter.print("Content-Type: application/json\r\n");
            if (!method.equals(RequestMethods.GET)) {
                if (enableChunking) {
                    printWriter.print("Transfer-Encoding: chunked\n");
                } else {
                    printWriter.print("Content-Length: " + payload.length() + "\r\n");
                }
            }

            printWriter.print("\r\n");
            if (!method.equals(RequestMethods.GET)) {
                printWriter.print(payload);
            }
            printWriter.flush();

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("Input : " + line);
                if (line.trim().equals("0")) {
                    break;
                }
            }
            printWriter.close();
            bufferedReader.close();
            System.out.println("Closing the client connection!");
            sslSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
