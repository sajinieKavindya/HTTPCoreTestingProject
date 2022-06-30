package backend;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class SSL400BE {


    public static void main(String[] args) throws Exception {

        SSL400BE echoSSL = new SSL400BE();

        File file = echoSSL.getFile("payload-large.json");

        String content = FileUtils.readFileToString(file, "UTF-8");

        String line4 = "{\"Hello\":\"World\"}";
        content = line4;

        // System.out.println(content);

        try {
            System.setProperty("javax.net.ssl.keyStore",
                    "/Users/nirothipan/Desktop/trash/http-core-testing/" + "wso2mi-4.0.0/repository/resources" +
                            "/security/wso2carbon.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "wso2carbon");
            ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();
            ServerSocket serverSocket = serverSocketFactory.createServerSocket(7002);
            System.out.println("SSL Echo Server Started!");

            do {
                Socket client = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                StringBuilder sb = new StringBuilder();
                char[] buf = new char[10];
                StringBuilder outt = new StringBuilder();
                int read = in.read(buf);
                System.out.println("entering loop");
                while (read > 0) {
                    try {
                        read = in.read(buf);
                        outt.append(buf, 0, read);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("out of loop");
                PrintWriter out = new PrintWriter(client.getOutputStream());

                // Start sending our reply, using the HTTP 1.1 protocol
                //out.print(0 + "\r\n");
                out.print("HTTP/1.1 400 OK\r\n"); // Version & status code
                out.print("Access-Control-Expose-Headers:\r\n");
                out.print("Access-Control-Allow-Origin: *\r\n");
                out.print("X-Correlation-ID: 9f22c69b-6673-4326-8aff-0c0c097cd3c0\r\n");
                out.print(
                        "Access-Control-Allow-Headers: authorization,Access-Control-Allow-Origin,Content-Type," +
                                "SOAPAction,apikey,testKey,Authorization\r\n");
                out.print("Content-Type: application/json\r\n");
                out.print("Date: Tue, 14 Dec 2021 08:15:17 GMT\r\n");
                out.print("Transfer-Encoding: chunked\r\n");
                out.print("Content-Length:  " + content.getBytes().length + "\r\n");
                ; // The type of data
                out.print("Connection: Close\r\n");
                out.print("\r\n");
                out.print(content + "\r\n");
                out.close();
                client.close();
            } while (true);
        } catch (Exception e) {
            System.err.println("Error" + e);
        }
    }


    private File getFile(String name) throws URISyntaxException {

        URL resource = getClass().getClassLoader().getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }
    }
}
