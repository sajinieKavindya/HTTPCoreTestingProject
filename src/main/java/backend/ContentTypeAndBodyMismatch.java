package backend;

import javax.net.ServerSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ContentTypeAndBodyMismatch {

    public static void main(String[] args) {
        try {
            // Get the port to listen on
            //int port = Integer.parseInt(args[0]);
            // Create a ServerSocket to listen on that port.
            //System.setProperty("javax.net.ssl.keyStore", "/Users/shefandarren/Documents/dbgermany/wso2am-4.0.0/repository/resources/security/wso2carbon.jks");
            //System.setProperty("javax.net.ssl.keyStorePassword", "wso2carbon");
            //ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
            ServerSocketFactory ssf = ServerSocketFactory.getDefault();
            //String line4 = "{\"request\":{\"productCode\":\"NGGENDOMPAY,NGASIALOCALPAY\",\"Request-ID\":\"REQ-95e66d46-206f-410d-901b-0486fccb4728\",\"dbeBridgeAccountNumber\":\"1524941000\",\"status\":\"Active\"},\"data\":[{\"status\":\"Active\",\"updatedDate\":\"2022-05-24 11:33:20\",\"accountType\":\"CCY\",\"mandateType\":\"DBDI\",\"mandateId\":\"gtbmandate.ap\",\"companyShortName\":\"bafircomp\",\"companyCrdsId\":null,\"mainAccountNumber\":\"1524941\",\"dbdiAccountNumber\":null,\"masterAccountNumber\":null,\"dbeBridgeAccountNumber\":\"1524941000\",\"iban\":null,\"tag25\":\"1524941-00-0\",\"branchBicPlusKey\":\"IN001168\",\"swiftCode\":\"DEUTINBBDEL\",\"masterNumber\":\"10052006\",\"accountLinkedProduct\":\"NGASIALOCALPAY\",\"accountLinkedProductStatus\":\"Disabled\",\"accountLinkedProductFeatures\":[{\"feature\":\"ExcludeE2EID\",\"status\":\"Disabled\"}]},{\"status\":\"Active\",\"updatedDate\":\"2022-03-23 10:36:31\",\"accountType\":\"CCY\",\"mandateType\":\"DBDI\",\"mandateId\":\"gtbmandate.ap\",\"companyShortName\":\"bafircomp\",\"companyCrdsId\":null,\"mainAccountNumber\":\"1524941\",\"dbdiAccountNumber\":null,\"masterAccountNumber\":null,\"dbeBridgeAccountNumber\":\"1524941000\",\"iban\":null,\"tag25\":\"1524941-00-0\",\"branchBicPlusKey\":\"IN001168\",\"swiftCode\":\"DEUTINBBDEL\",\"masterNumber\":\"10052006\",\"accountLinkedProduct\":\"NGGENDOMPAY\",\"accountLinkedProductStatus\":\"Disabled\",\"accountLinkedProductFeatures\":[{\"feature\":\"ACCEPTGIRO\",\"status\":\"Disabled\"},{\"feature\":\"AUTOROLLOVER\",\"status\":\"Disabled\"},{\"feature\":\"DOMSO\",\"status\":\"Disabled\"},{\"feature\":\"ESCONFIRM\",\"status\":\"Disabled\"},{\"feature\":\"FSC\",\"status\":\"Disabled\"},{\"feature\":\"SEPADDB2B\",\"status\":\"Disabled\"},{\"feature\":\"SEPADDCORE\",\"status\":\"Disabled\"},{\"feature\":\"SEPAINDP\",\"status\":\"Disabled\"},{\"feature\":\"TESTSEPA\",\"status\":\"Disabled\"},{\"feature\":\"test feat\",\"status\":\"Disabled\"}]}]}";
            String line4 = "{\"Hello\":\"World\"}";

            ServerSocket ss = ssf.createServerSocket(7000);
            //ServerSocket ss = new ServerSocket(7000);
            // Now enter an infinite loop, waiting for & handling connections.
            for (;;) {
                // Wait for a client to connect. The method will block;
                // when it returns the socket will be connected to the client
                Socket client = ss.accept();

                // Get input and output streams to talk to the client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));


                char[] buf = new char[10];
                StringBuilder outt = new StringBuilder();
                while (true) {
                    try{
                        int read = in.read(buf);
                        outt.append(buf, 0, read);
                        //if (read < 100)
                        break;
                    }

                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                PrintWriter out = new PrintWriter(client.getOutputStream());

                // Start sending our reply, using the HTTP 1.1 protocol
                //out.print(0 + "\r\n");
                out.print("HTTP/1.1 200 OK\r\n"); // Version & status code
                out.print("Access-Control-Expose-Headers:\r\n");
                out.print("Access-Control-Allow-Origin: *\r\n");
                out.print("X-Correlation-ID: 9f22c69b-6673-4326-8aff-0c0c097cd3c0\r\n");
                out.print("Access-Control-Allow-Headers: authorization,Access-Control-Allow-Origin,Content-Type,SOAPAction,apikey,testKey,Authorization\r\n");
                out.print("Content-Type: text/xml\r\n");
                out.print("Date: Tue, 14 Dec 2021 08:15:17 GMT\r\n");
                //out.print("Transfer-Encoding: chunked\r\n");
                out.print("Content-Length:  " + line4.getBytes().length + "\r\n");; // The type of data
                //out.print("Content-Length: 36\r\n");; // The type of data
                //out.print("Content-Length: 10\r\n");; // The type of data
                out.print("Connection: Close\r\n");
                out.print("\r\n"); // End of headers
                //out.flush();
                //System.exit(-1);
                // Now, read the HTTP request from the client, and send it
                // right back to the client as part of the body of our
                // response. The client doesn't disconnect, so we never get
                // an EOF. It does sends an empty line at the end of the
                // headers, though. So when we see the empty line, we stop
                // reading. This means we don't mirror the contents of POST
                // requests, for example. Note that the readLine() method
                // works with Unix, Windows, and Mac line terminators.
                //out.flush();

                //out.flush();
                //out.print("6ac"+"\r\n");
                out.print(line4 + "\r\n");
                //out.print(0+"\r\n");
                //out.flush();
                //out.flush();
                // Close socket, breaking the connection to the client, and
                // closing the input and output streams
                out.close(); // Flush and close the output stream
                //in.close(); // Close the input stream
                client.close(); // Close the socket itself
            } // Now loop again, waiting for the next connection
        }
        // If anything goes wrong, print an error message
        catch (Exception e) {
            System.err.println(e);
            System.err.println("Usage: java HttpMirror <port>");
        }    }
}

