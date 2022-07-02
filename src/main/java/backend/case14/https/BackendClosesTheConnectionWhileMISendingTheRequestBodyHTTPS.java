package backend.case14.https;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class BackendClosesTheConnectionWhileMISendingTheRequestBodyHTTPS {

    public static void main(String[] args) {
        try {
            // Create a ServerSocket to listen on that port.
            System.setProperty("javax.net.ssl.keyStore", "/Users/apple/.wum3/products/wso2mi/4.0.0/wso2mi-4.0.0_http_core_testing/repository/resources/security/wso2carbon.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "wso2carbon");
            ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
            ServerSocket ss = ssf.createServerSocket(7005);
            System.out.println("SSL Server Started!");

            // Now enter an infinite loop, waiting for & handling connections.
            for (;;) {
                // Wait for a client to connect. The method will block;
                // when it returns the socket will be connected to the client
                Socket clientSocket = ss.accept();
                System.out.println("connection accepted");
                clientSocket.setReceiveBufferSize(3);

                // Get input and output streams to talk to the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //code to read and print headers
                int i = 0;
                String headerLine = null;
                while((headerLine = in.readLine()).length() != 0) {
                    System.out.println(headerLine);
                    clientSocket.close();
                    System.out.println("closed the socket");
                    break;
                }
                in.close();

            } // Now loop again, waiting for the next connection
        }
        // If anything goes wrong, print an error message
        catch (Exception e) {
            System.err.println("Server shutdown!");
        }
    }
}
