package client;

import client.helpers.RequestMethods;
import client.helpers.TestPayloads;

import java.io.*;
import java.net.Socket;

public class ClosesConnectionWhileSendingRequestBody {

    private String host = "localhost";
    private int port = 8290;

    public static void main(String[] args) {

        ClosesConnectionWhileSendingRequestBody client = new ClosesConnectionWhileSendingRequestBody();
//        for (int i = 0; i < 1000; i++) {
        client.run();
//        }
    }

    ClosesConnectionWhileSendingRequestBody() {

    }

    ClosesConnectionWhileSendingRequestBody(String host, int port) {

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
            new ClosesConnectionWhileSendingRequestBody.ClientThread(socket).start();
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

                String payload = TestPayloads.FULL_PAYLOAD;
                RequestMethods method = RequestMethods.PUT;

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
                    System.out.println("Inut : " + line);
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
