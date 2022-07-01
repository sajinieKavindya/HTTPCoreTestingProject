package client;

import client.helpers.RequestMethods;
import client.helpers.TestPayloads;

import java.io.*;
import java.net.Socket;

public class ClosesConnectionWhileSendingRequestBody {

    private String host = "localhost";
    private int port = 8290;

    ClosesConnectionWhileSendingRequestBody() {

    }

    ClosesConnectionWhileSendingRequestBody(String host, int port) {

        this.host = host;
        this.port = port;
    }

    // Start to run the server
    public void run(String payload, RequestMethods method) {

        try {
            // Create socket
            Socket socket = new Socket(this.host, this.port);
            socket.setSendBufferSize(12000);

            System.out.println("client started");
            new ClosesConnectionWhileSendingRequestBody.ClientThread(socket, payload, method).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Thread handling the socket to server
    static class ClientThread extends Thread {

        private Socket socket = null;
        String payload;
        RequestMethods method;

        ClientThread(Socket socket, String payload, RequestMethods method) {

            this.socket = socket;
            this.payload = payload;
            this.method = method;
        }

        public void run() {

            try {
                // Start handling application content
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
                // Write data

                printWriter.print(method + " /test HTTP/1.1\r\n");
                printWriter.print("Accept: application/json\r\n");
                printWriter.print("Connection: keep-alive\r\n");
                printWriter.print("Content-Type: application/json\r\n");
                printWriter.print("Content-Length: " + payload.getBytes().length + "\r\n");
                printWriter.print("\r\n");

                BufferedReader bufReader = new BufferedReader(new StringReader(payload));
                String line=null;
                int count = 0;
                while( (line=bufReader.readLine()) != null )
                {
                    if (count++ == 10) {
                        socket.close();
                        System.exit(-1);
                    }
                    printWriter.print(line);
                }

                printWriter.flush();
                line = null;
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
