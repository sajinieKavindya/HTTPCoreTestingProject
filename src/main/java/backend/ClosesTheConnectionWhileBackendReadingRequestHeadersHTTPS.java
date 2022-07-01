package backend;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

public class ClosesTheConnectionWhileBackendReadingRequestHeadersHTTPS {

    public static void main(String[] args) {
        try {
            // Create a ServerSocket to listen on that port.
            System.setProperty("javax.net.ssl.keyStore", "/Users/apple/.wum3/products/wso2mi/4.0.0/wso2mi-4.0.0_http_core_testing/repository/resources/security/wso2carbon.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "wso2carbon");
            ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
            ServerSocket ss = ssf.createServerSocket(7005);

            // Now enter an infinite loop, waiting for & handling connections.
            for (;;) {
                // Wait for a client to connect. The method will block;
                // when it returns the socket will be connected to the client
                Socket clientSocket = ss.accept();

                // Get input and output streams to talk to the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                char[] buf = new char[10];
                StringBuilder outt = new StringBuilder();
                int i = 0;
                while (true) {
                    try{
                        int read = in.read(buf);
                        outt.append(buf, 0, read);
                        //if (read < 100)
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    i++;
                    if (i == 3) {
                        // closes the client socket while reading the request headers
                        clientSocket.close();
                        break;
                    }
                }
                in.close();
            } // Now loop again, waiting for the next connection
        }
        // If anything goes wrong, print an error message
        catch (Exception e) {
            System.err.println(e);
            System.err.println("Usage: java HttpMirror <port>");
        }
    }
}
