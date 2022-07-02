package backend.case14.https;

import client.ClientMain;
import client.helpers.RequestMethods;
import client.helpers.TestPayloads;

import javax.net.ssl.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

public class SlowWriteClientHTTPS {

    private String host;
    private int port;
    public static final String CRLF = "\r\n";

    public SlowWriteClientHTTPS() {

    }

    public SlowWriteClientHTTPS(String host, int port) {

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
    public void run(String payload, String authorizationHeader) {

        SSLContext sslContext = this.createSSLContext();

        try {
            // Create socket factory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Create socket
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(this.host, this.port);
            System.out.println("SSL client started");

            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());

            // Start handshake
            sslSocket.startHandshake();

            // Get session after the connection is established
            SSLSession sslSession = sslSocket.getSession();

            System.out.println("SSLSession :");
            System.out.println("\tProtocol : " + sslSession.getProtocol());
            System.out.println("\tCipher suite : " + sslSession.getCipherSuite());

            // Start handling application content
            InputStream inputStream = sslSocket.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            PrintStream printWriter = new PrintStream(sslSocket.getOutputStream());
            // Write data

            printWriter.print("POST /test/1.0.0 HTTP/1.1\r\n");
            printWriter.print("Accept: application/json\r\n");
            printWriter.print(authorizationHeader +"\r\n");
            printWriter.print("Connection: keep-alive\r\n");
            printWriter.print("Transfer-Encoding: chunked\n");
            printWriter.print("Content-Type: text/plain\r\n");
            printWriter.print("\r\n");

            printWriter.flush();
            InputStream stream = new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8));

            int chunkSize = 50;
            int count;
            byte[] buffer = new byte[chunkSize];

            while ((count = stream.read(buffer)) > 0) {
                printWriter.printf("%x" + "\r\n", count);
                printWriter.write(buffer, 0, count);
                printWriter.print(CRLF);
                Thread.sleep(50);
            }

            printWriter.print("0" + CRLF);
            printWriter.print(CRLF);
            printWriter.flush();
            printWriter.close();

            String line = null;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                i++;
                System.out.println("Input : " + line);
                if (line.equals("0")) {
                    break;
                }
            }
            bufferedReader.close();
            sslSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
