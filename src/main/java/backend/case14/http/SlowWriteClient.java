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

    public static void main(String[] args) {

        SlowWriteClient client = new SlowWriteClient();
        client.run();
    }

    SlowWriteClient() {

    }

    SlowWriteClient(String host, int port) {

        this.host = host;
        this.port = port;
    }

    // Start to run the server
    public void run() {

        try {
            // Create socket
            Socket socket = new Socket(this.host, this.port);

            System.out.println("client started");
            new SlowWriteClient.ClientThread(socket).start();
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

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintStream printWriter = new PrintStream(socket.getOutputStream());
                // Write data

                String payload = TestPayloads.LARGE_PAYLOAD_25K;
                RequestMethods method = RequestMethods.POST;

                printWriter.print(method + " /test/1.0.0 HTTP/1.1\r\n");
                printWriter.print("Accept: application/json\r\n");
                printWriter.print("Authorization: Bearer eyJ4NXQiOiJOVGRtWmpNNFpEazNOalkwWXpjNU1tWm1PRGd3TVRFM01XWXdOREU1TVdSbFpEZzROemM0WkEiLCJraWQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZ19SUzI1NiIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJhZG1pbiIsImF1dCI6IkFQUExJQ0FUSU9OIiwiYXVkIjoidGhkcV9YWkxLR0ZzTlFvaW9jbW1ET0xyOFNrYSIsIm5iZiI6MTY1NjU5NTg2OSwiYXpwIjoidGhkcV9YWkxLR0ZzTlFvaW9jbW1ET0xyOFNrYSIsInNjb3BlIjoiZGVmYXVsdCIsImlzcyI6Imh0dHBzOlwvXC9sb2NhbGhvc3Q6OTQ0M1wvb2F1dGgyXC90b2tlbiIsImV4cCI6OTIyMzM3MjAzNjg1NDc3NSwiaWF0IjoxNjU2NTk1ODY5LCJqdGkiOiI0ZDY4NGJmOC1jZDI1LTQyYzItOGQwMC1lZDRkYjc0OGY5MjMifQ.N4H8Fgl0wwHCUe-u7a9Pw6dOA40mdWSZG9J73t9zNWL58yHgh_Rg77n7QVKkW8qp_-fk51uc5Bm5YZ_23o3bLCToAPoVIg_NVXGhZ84JQfapIPdek_CGShBM87hKCPnK7Vmv-gul1TuCWZVmZaSfcfFtGVGBgxGWmvFO4huOx0zb4NUfTYDaTToQ0w_r_y9ee2vcLYRNT31tjKUKcvV9pukI29vkdYI2UexVWjEGcbJTgs5ak8NlZCTqxvUewJGjbVYG9xeSW1nbReJdClD9o6pae6nrOaqHeFX1MZet8dVHLGVwZwlk9NTNVxFKES7U8BU1IxEFxz62vpHfuz4slA\r\n");
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
}
