package backend;

import javax.net.ServerSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SlowWriting {

    public static void main(String[] args) {
        String payload = "response from backend";
        int itr = 10;

        try {
            // Create a ServerSocket to listen on that port.
            ServerSocketFactory ssf = ServerSocketFactory.getDefault();
            ServerSocket ss = ssf.createServerSocket(7005);
            ss.setReceiveBufferSize(3);

            // Now enter an infinite loop, waiting for & handling connections.
            for (;;) {
                // Wait for a client to connect. The method will block;
                // when it returns the socket will be connected to the client
                Socket clientSocket = ss.accept();
                clientSocket.setReceiveBufferSize(3);
                clientSocket.setSendBufferSize(3);

                // Get input and output streams to talk to the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                char[] buf = new char[3];
                StringBuilder outt = new StringBuilder();
                while (true) {
                    try{
                        int read = in.read(buf);
                        outt.append(buf, 0, read);
                        break;
                        //if (read < 100)
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());

                // Start sending our reply, using the HTTP 1.1 protocol
                out.print("HTTP/1.1 200 OK\r\n"); // Version & status code
                out.flush();
                Thread.sleep(2000);
                out.print("Access-Control-Expose-Headers:\r\n");
                out.flush();
                Thread.sleep(2000);
                out.print("Access-Control-Allow-Origin: *\r\n");
                out.flush();
                Thread.sleep(2000);
                out.print("X-Correlation-ID: 9f22c69b-6673-4326-8aff-0c0c097cd3c0\r\n");
                out.flush();
                Thread.sleep(2000);
                out.print("Access-Control-Allow-Headers: authorization,Access-Control-Allow-Origin,Content-Type,SOAPAction,apikey,testKey,Authorization\r\n");
                out.flush();
                Thread.sleep(2000);
                out.print("Content-Type: text/plain\r\n");
                out.flush();
                Thread.sleep(2000);
                out.print("Date: Tue, 14 Dec 2021 08:15:17 GMT\r\n");
                out.flush();
                Thread.sleep(2000);
                //out.print("Transfer-Encoding: chunked\r\n");
                out.print("Content-Length: " + (payload.getBytes().length)*itr + "\r\n");; // The type of data
                out.flush();
                Thread.sleep(2000);
                //out.print("Content-Length: 10\r\n");; // The type of data
                out.print("Connection: Close\r\n");

                out.print("\r\n"); // End of headers

                for (int i = 0; i < itr; i++) {
                    out.println("response from backend,");
                    out.flush();
                    Thread.sleep(2000);
                }

                out.print("\r\n");

                out.flush();
                in.close();
                out.close();
                clientSocket.close();
            } // Now loop again, waiting for the next connection
        }
        // If anything goes wrong, print an error message
        catch (Exception e) {
            System.err.println(e);
            System.err.println("Usage: java HttpMirror <port>");
        }
    }

}
