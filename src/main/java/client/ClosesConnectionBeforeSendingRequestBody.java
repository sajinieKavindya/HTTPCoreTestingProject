package client;

import client.helpers.RequestMethods;
import client.helpers.TestPayloads;

import java.io.*;
import java.net.Socket;

public class ClosesConnectionBeforeSendingRequestBody {

    private String host = "localhost";
    private int port = 8290;

    public static void main(String[] args) {

        ClosesConnectionBeforeSendingRequestBody client = new ClosesConnectionBeforeSendingRequestBody();
//        for (int i = 0; i < 1000; i++) {
        client.run();
//        }
    }

    ClosesConnectionBeforeSendingRequestBody() {

    }

    ClosesConnectionBeforeSendingRequestBody(String host, int port) {

        this.host = host;
        this.port = port;
    }

    // Start to run the server
    public void run() {

        try {
            // Create socket
            Socket socket = new Socket(this.host, this.port);
            socket.setSendBufferSize(12000);

            System.out.println("client started");
            new ClosesConnectionBeforeSendingRequestBody.ClientThread(socket).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Thread handling the socket to server
    static class ClientThread extends Thread {

        private Socket socket = null;

        ClientThread(Socket socket) {

            this.socket = socket;
        }

        public void run() {

            try {
                // Start handling application content
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
                // Write data

                String payload = TestPayloads.SMALL_PAYLOAD;
                RequestMethods method = RequestMethods.GET;

                printWriter.print(method + " /test HTTP/1.1\r\n");
                printWriter.print("Accept: application/json\r\n");
                printWriter.print("Connection: keep-alive\r\n");

                printWriter.print("Content-Type: application/json\r\n");
                if (!method.equals(RequestMethods.GET)) {
                    printWriter.print("Content-Length: " + payload.length() + "\r\n");
                }

                printWriter.print("\r\n");
                socket.close();
                System.exit(-1);
                if (!method.equals(RequestMethods.GET)) {
                    printWriter.print(payload);
                }
                printWriter.flush();
                String line = null;
                int i = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    i++;
                    System.out.println("Input : " + line);
                }
                printWriter.close();
                bufferedReader.close();
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
