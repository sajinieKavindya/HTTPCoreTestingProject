package client.helpers;

import java.io.*;
import java.net.Socket;

public class SimpleHTTPClient {

    private String host;
    private int port;

    public SimpleHTTPClient() {

    }

    public SimpleHTTPClient(String host, int port) {

        this.host = host;
        this.port = port;
    }

    // Start to run the server
    public void run(String payload, RequestMethods method, String authorizationHeader, boolean enableChunking) {

        try {
            // Create socket
            Socket socket = new Socket(this.host, this.port);

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));

            socket.setSendBufferSize(25000);
            // Write data
            System.out.println("Client sending a request!");

            printWriter.print(method + " /test/1.0.0 HTTP/1.1\r\n");
            printWriter.print("Accept: application/json\r\n");
            printWriter.print("Connection: keep-alive\r\n");
            printWriter.print(authorizationHeader + "\r\n");
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
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
