package client;

import client.helpers.RequestMethods;
import client.helpers.TestPayloads;

public class ClientMain {

    public static String keyStoreLocation = "<define keystore location here>";
    public static String keyStorePassword = "<define keystore password here>";

    public static void main(String[] args) {

        ClientMain main = new ClientMain();
        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.SMALL_PAYLOAD, RequestMethods.GET);
        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.SMALL_PAYLOAD, RequestMethods.POST);
        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.SMALL_PAYLOAD, RequestMethods.PUT);

        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.LARGE_PAYLOAD_25K, RequestMethods.GET);
        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.LARGE_PAYLOAD_25K, RequestMethods.POST);
        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.LARGE_PAYLOAD_25K, RequestMethods.PUT);
    }

    public void runHTTPClientsWithPayload(String host, int port, String payload, RequestMethods method) {

        ClientSendsChunks client1 = new ClientSendsChunks(host, port);
        client1.run(payload, method);

        ClientSendsInvalidChunks client2 = new ClientSendsInvalidChunks(host, port);
        client2.run(payload, method);

        ClosesConnectionBeforeReceivingResponse client3 = new ClosesConnectionBeforeReceivingResponse(host, port);
        client3.run(payload, method);

        ClosesConnectionBeforeSendingRequestBody client4 = new ClosesConnectionBeforeSendingRequestBody(host, port);
        client4.run(payload, method);

        ClosesConnectionWhileReceivingResponseBody client5 = new ClosesConnectionWhileReceivingResponseBody(host, port);
        client5.run(payload, method);

        ClosesConnectionWhileReceivingResponseHeaders client6 = new ClosesConnectionWhileReceivingResponseHeaders(host, port);
        client6.run(payload, method);

        ClosesConnectionWhileSendingHeaders client7 = new ClosesConnectionWhileSendingHeaders(host, port);
        client7.run(payload, method);

        ClosesConnectionWhileSendingRequestBody client8 = new ClosesConnectionWhileSendingRequestBody(host, port);
        client8.run(payload, method);

        ContentLengthDifferFromActualContentLength client9 = new ContentLengthDifferFromActualContentLength(host, port);
        client9.run(payload, payload.getBytes().length - 10, method);
        client9.run(payload, payload.getBytes().length + 10, method);

        ContentTypeAndBodyMismatch client10 = new ContentTypeAndBodyMismatch(host, port);
        client10.run(payload, method);

        MalformedRequest client11 = new MalformedRequest(host, port);
        client11.run(payload, method);

        SlowRead client12 = new SlowRead(host, port);
        client12.run(payload, method);

        WaitUntilAResponseReceived client13 = new WaitUntilAResponseReceived(host, port);
        client13.run(payload, method);
    }
}
