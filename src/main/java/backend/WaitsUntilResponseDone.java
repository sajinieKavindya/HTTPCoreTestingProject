package backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

public class WaitsUntilResponseDone extends BackendServer {

    public void run(int port, String content) throws Exception {

        try {
            // Create a ServerSocket to listen on that port.
            ServerSocketFactory ssf = ServerSocketFactory.getDefault();
            ss = ssf.createServerSocket(port);
            System.out.println("Server Started!");

            for (; ; ) {
                // Wait for a client to connect. The method will block;
                // when it returns the socket will be connected to the client
                Socket client = ss.accept();
                System.out.println("Received a request to backend!");

                // Get input and output streams to talk to the client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                char[] buf = new char[10];
                StringBuilder outt = new StringBuilder();
                while (true) {
                    try {
                        int read = in.read(buf);
                        outt.append(buf, 0, read);
                        //if (read < 100)
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                PrintWriter out = new PrintWriter(client.getOutputStream());

                // Start sending our reply, using the HTTP 1.1 protocol
                out.print("HTTP/1.1 200 OK\r\n"); // Version & status code
                out.print("Access-Control-Expose-Headers:\r\n");
                out.print("Access-Control-Allow-Origin: *\r\n");
                out.print("X-Correlation-ID: 9f22c69b-6673-4326-8aff-0c0c097cd3c0\r\n");
                out.print(
                        "Access-Control-Allow-Headers: authorization,Access-Control-Allow-Origin,Content-Type,SOAPAction,apikey,testKey,Authorization\r\n");
                out.print("Content-Type: application/json\r\n");
                out.print("Date: Tue, 14 Dec 2021 08:15:17 GMT\r\n");
                out.print("Transfer-Encoding: chunked\r\n");
                out.print("Content-Length:  " + content.getBytes().length + "\r\n");
                out.print("Connection: Close\r\n");
                out.print("\r\n"); // End of headers
                out.print(content + "\r\n");
                out.close(); // Flush and close the output stream
                in.close(); // Close the input stream
                client.close(); // Close the socket itself
                System.out.println("Sent a response to MI server!");
            } // Now loop again, waiting for the next connection
        }
        // If anything goes wrong, print an error message
        catch (Exception e) {
            System.err.println("Server shutdown!");
        }
    }
}
