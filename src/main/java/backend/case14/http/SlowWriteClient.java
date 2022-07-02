package backend.case14.http;

import client.helpers.RequestMethods;
import client.helpers.TestPayloads;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SlowWriteClient {

    private String host = "localhost";
    private int port = 8280;
    public static final String CRLF = "\r\n";

    public SlowWriteClient() {

    }

    public SlowWriteClient(String host, int port) {

        this.host = host;
        this.port = port;
    }

    // Start to run the server
    public void run(String payload) {

        try {
            // Create socket
            Socket socket = new Socket(this.host, this.port);
            System.out.println("client started");

            // Start handling application content
            InputStream inputStream = socket.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            PrintStream printWriter = new PrintStream(socket.getOutputStream());
            // Write data

            printWriter.print("POST /test/1.0.0 HTTP/1.1\r\n");
            printWriter.print("Accept: application/json\r\n");
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
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
