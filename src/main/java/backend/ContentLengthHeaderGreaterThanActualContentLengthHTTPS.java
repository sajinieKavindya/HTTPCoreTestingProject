package backend;

import util.Utils;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ContentLengthHeaderGreaterThanActualContentLengthHTTPS {

    public static void main(String[] args) {
        try {
            File file = Utils.getFile("payload-large.json");
//            String content = FileUtils.readFileToString(file, "UTF-8");
            String content = "{\"Hello\":\"World\"}";

            System.setProperty("javax.net.ssl.keyStore", "/Users/shefandarren/Documents/dbgermany/wso2am-4.0.0/repository/resources/security/wso2carbon.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "wso2carbon");
            ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
            ServerSocket ss = ssf.createServerSocket(7000);
            System.out.println("Server Started!");

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
                        break;
                    }
                }

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());

                // Start sending our reply, using the HTTP 1.1 protocol
                out.print("HTTP/1.1 200 OK\r\n"); // Version & status code
                out.print("Access-Control-Expose-Headers:\r\n");
                out.print("Access-Control-Allow-Origin: *\r\n");
                out.print("X-Correlation-ID: 9f22c69b-6673-4326-8aff-0c0c097cd3c0\r\n");
                out.print("Access-Control-Allow-Headers: authorization,Access-Control-Allow-Origin,Content-Type,SOAPAction,apikey,testKey,Authorization\r\n");
                out.print("Content-Type: application/json\r\n");
                out.print("Date: Tue, 14 Dec 2021 08:15:17 GMT\r\n");
                //out.print("Transfer-Encoding: chunked\r\n");
                out.print("Content-Length: " + content.getBytes().length + 10 + "\r\n");; // The type of data
                //out.print("Content-Length: 10\r\n");; // The type of data
                out.print("Connection: keel-alive\r\n");
                out.print("\r\n"); // End of headers
                out.print(content + "\r\n");

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
