package backend;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClosesTheConnectionAsSoonAsTheRequestReceived extends BackendServer{

    public void run(int port, String content) throws Exception {

        try {
            // Create a ServerSocket to listen on that port.
            ServerSocketFactory ssf = ServerSocketFactory.getDefault();
            ss = ssf.createServerSocket(port);
            System.out.println("Server Started!");

            for (;;) {
                // Wait for a client to connect. The method will block;
                // when it returns the socket will be connected to the client
                Socket client = ss.accept();
                client.close(); // Close the socket itself

            } // Now loop again, waiting for the next connection
        }
        // If anything goes wrong, print an error message
        catch (Exception e) {
            System.err.println("Server shutdown!");
        }
    }
}

