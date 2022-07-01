package client;

import client.helpers.RequestMethods;
import client.helpers.TestPayloads;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MalformedRequest {

    private String host = "localhost";
    private int port = 8290;

    MalformedRequest() {

    }

    MalformedRequest(String host, int port) {

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
            new MalformedRequest.ClientThread(socket, payload, method).start();
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
                printWriter.print(
                        "odHRwczovL2VpZHAtdWF0LmRlLmRiLmNvbS9hdXRoL3JlYWxtcy9nbG9iYWwiLCJhdWQiOiIxMDEyMzUtMV9CYW5rQVBJLWRiQVBJIiwic3ViIjoiMTUwMmQzNWYtYWZmZi00YzNmLTgxMzQtODY0MGVhNzQyZDljIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiMTAxMjM1LTFfQmFua0FQSS1kYkFQSSIsInNlc3Npb25fc3RhdGUiOiIxYjUzNzNiYS02ZDMwLTQzZWMtYWVjNy0xY2Q0MGI1NjEzNmYiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzdmNfYmFua2FwaSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiXX0sInNpZCI6IjFiNTM3M2JhLTZkMzAtNDNlYy1hZWM3LTFjZDQwYjU2MTM2ZiIsIm5hcmlkIjoiMTAxMjM1LTEiLCJzY3AiOlsibGc6Q0EvQUJGIiwibGc6WlZLL1NBTERPIiwibGc6S0svVU1TIiwibGc6QkZFL1BFUkYiLCJsZzpCRkUvVUVCIiwibGc6QkZFL1VNUyIsImxnOkJGRS9VTVMiLCJsZzpDQk8vQU5aRVAiLCJsZzpDQk8vQU5aRVAiLCJsZzpQQTcvUFJPRFUiLCJsZzpQQTcvS1lDSUYiXSwibGVnaXRpbWF0aW9ucyI6W3siaWQiOiJCRDEwMDgiLCJndm8iOlsiQ0EvQUJGIiwiWlZLL1NBTERPIiwiS0svVU1TIiwiQkZFL1BFUkYiLCJCRkUvVUVCIiwiQkZFL1VNUyIsIkJGRS9VTVMiLCJDQk8vQU5aRVAiLCJDQk8vQU5aRVAiLCJQQTcvUFJPRFUiLCJQQTcvS1lDSUYiXSwibGdfbmFyaWQiOiIxMDEyMzUtMSIsImxlZ2lfYXV0aCI6WyJsZzpDQS9BQkYiLCJsZzpaVksvU0FMRE8iLCJsZzpLSy9VTVMiLCJsZzpCRkUvUEVSRiIsImxnOkJGRS9VRUIiLCJsZzpCRkUvVU1TIiwibGc6QkZFL1VNUyIsImxnOkNCTy9BTlpFUCIsImxnOkNCTy9BTlpFUCIsImxnOlBBNy9QUk9EVSIsImxnOlBBNy9LWUNJRiJdLCJpYXQiOjE2NTYwMDg4NDJ9XSwiZGJsZWdpaWQiOiJCRDEwMDgiLCJsYXN0X2F1dGgiOjE2MzQwNTgwNTl9.eY4UaHKVIjNxEqtW-PK146GMvPs4W91dCot7NutpriUmmQyL5E1R8LfcFAcLG3SoCBI99yNDHcRVoX6jB_Uy-k0Ua8uzbrF691SKA_b5_unUKDUmH01m-SvmTB3K0PsLAL4WbBHiPHFtar3iyT3sLRYuBob5H7meBloEBJIveJqtTHFs-y7EG4h6aNLv1BwlvCvz-halYN-ES7txxkj7UdAtHbahmPjqKKRW2t-_JbxgZKPtMSYS4d_pcBLCqeIVlVZdN44FpQ6EpjaEKFGGcEfiUn05qk6-cLWs9muYw5Jf_ChsXn9btv-ihQaEQlSog2OTJ8ySaUKx3CrVHHIY9A\r\n");
                printWriter.print("Accept: application/json\r\n");
                printWriter.print("Connection: keep-alive\r\n");
                printWriter.print("Content-Type: application/json\r\n");
                if (!method.equals(RequestMethods.GET)) {
                    printWriter.print("Content-Length: " + payload.length() + "\r\n");
                }

                printWriter.print("\r\n");
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
