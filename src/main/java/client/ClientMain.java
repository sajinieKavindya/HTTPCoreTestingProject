package client;

import client.helpers.RequestMethods;
import client.helpers.TestPayloads;

public class ClientMain {

    public static String keyStoreLocation = "<define keystore location here>";
    public static String keyStorePassword = "<define keystore password here>";

    public static void main(String[] args) {

        ClientMain main = new ClientMain();
        // HTTP client
        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.SMALL_PAYLOAD, RequestMethods.GET);
        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.SMALL_PAYLOAD, RequestMethods.POST);
        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.SMALL_PAYLOAD, RequestMethods.PUT);

        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.LARGE_PAYLOAD_25K, RequestMethods.GET);
        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.LARGE_PAYLOAD_25K, RequestMethods.POST);
        main.runHTTPClientsWithPayload("localhost", 8290, TestPayloads.LARGE_PAYLOAD_25K, RequestMethods.PUT);

        // HTTPS client
        main.runHTTPSClientsWithPayload("localhost", 8290, TestPayloads.SMALL_PAYLOAD, RequestMethods.GET);
        main.runHTTPSClientsWithPayload("localhost", 8290, TestPayloads.SMALL_PAYLOAD, RequestMethods.POST);
        main.runHTTPSClientsWithPayload("localhost", 8290, TestPayloads.SMALL_PAYLOAD, RequestMethods.PUT);

        main.runHTTPSClientsWithPayload("localhost", 8290, TestPayloads.LARGE_PAYLOAD_25K, RequestMethods.GET);
        main.runHTTPSClientsWithPayload("localhost", 8290, TestPayloads.LARGE_PAYLOAD_25K, RequestMethods.POST);
        main.runHTTPSClientsWithPayload("localhost", 8290, TestPayloads.LARGE_PAYLOAD_25K, RequestMethods.PUT);
    }

    public void runHTTPClientsWithPayload(String host, int port, String payload, RequestMethods method) {

        System.out.println("Run ClientSendsChunks");
        ClientSendsChunks client1 = new ClientSendsChunks(host, port);
        client1.run(payload, method);
        System.out.println("Stop ClientSendsChunks");

        System.out.println("Run ClientSendsInvalidChunks");
        ClientSendsInvalidChunks client2 = new ClientSendsInvalidChunks(host, port);
        client2.run(payload, method);
        System.out.println("Stop ClientSendsInvalidChunks");

        System.out.println("Run ClosesConnectionBeforeReceivingResponse");
        ClosesConnectionBeforeReceivingResponse client3 = new ClosesConnectionBeforeReceivingResponse(host, port);
        client3.run(payload, method);
        System.out.println("Stop ClosesConnectionBeforeReceivingResponse");

        System.out.println("Run ClosesConnectionBeforeSendingRequestBody");
        ClosesConnectionBeforeSendingRequestBody client4 = new ClosesConnectionBeforeSendingRequestBody(host, port);
        client4.run(payload, method);
        System.out.println("Stop ClosesConnectionBeforeSendingRequestBody");

        System.out.println("Run ClosesConnectionWhileReceivingResponseBody");
        ClosesConnectionWhileReceivingResponseBody client5 = new ClosesConnectionWhileReceivingResponseBody(host, port);
        client5.run(payload, method);
        System.out.println("Stop ClosesConnectionWhileReceivingResponseBody");

        System.out.println("Run ClosesConnectionWhileReceivingResponseHeaders");
        ClosesConnectionWhileReceivingResponseHeaders client6 = new ClosesConnectionWhileReceivingResponseHeaders(host, port);
        client6.run(payload, method);
        System.out.println("Stop ClosesConnectionWhileReceivingResponseHeaders");

        System.out.println("Run ClosesConnectionWhileSendingHeaders");
        ClosesConnectionWhileSendingHeaders client7 = new ClosesConnectionWhileSendingHeaders(host, port);
        client7.run(payload, method);
        System.out.println("Stop ClosesConnectionWhileSendingHeaders");

        System.out.println("Run ClosesConnectionWhileSendingRequestBody");
        ClosesConnectionWhileSendingRequestBody client8 = new ClosesConnectionWhileSendingRequestBody(host, port);
        client8.run(payload, method);
        System.out.println("Stop ClosesConnectionWhileSendingRequestBody");

        System.out.println("Run ContentLengthLessThanActualContentLength");
        ContentLengthDifferFromActualContentLength client9 = new ContentLengthDifferFromActualContentLength(host, port);
        client9.run(payload, payload.getBytes().length - 10, method);
        System.out.println("Stop ContentLengthLessThanActualContentLength");

        System.out.println("Run ContentLengthGreaterThanActualContentLength");
        client9.run(payload, payload.getBytes().length + 10, method);
        System.out.println("Stop ContentLengthGreaterThanActualContentLength");

        System.out.println("Run ContentTypeAndBodyMismatch");
        ContentTypeAndBodyMismatch client10 = new ContentTypeAndBodyMismatch(host, port);
        client10.run(payload, method);
        System.out.println("Stop ContentTypeAndBodyMismatch");

        System.out.println("Run MalformedRequest");
        MalformedRequest client11 = new MalformedRequest(host, port);
        client11.run(payload, method);
        System.out.println("Stop MalformedRequest");

        System.out.println("Run SlowRead");
        SlowRead client12 = new SlowRead(host, port);
        client12.run(payload, method);
        System.out.println("Stop SlowRead");

        System.out.println("Run WaitUntilAResponseReceived");
        WaitUntilAResponseReceived client13 = new WaitUntilAResponseReceived(host, port);
        client13.run(payload, method);
        System.out.println("Stop WaitUntilAResponseReceived");
    }

    public void runHTTPSClientsWithPayload(String host, int port, String payload, RequestMethods method) {

        System.out.println("Run ClientSendsChunksTTPS");
        ClientSendsChunksHTTPS client1 = new ClientSendsChunksHTTPS(host, port);
        client1.run(payload, method);
        System.out.println("Stop ClientSendsChunksTTPS");

        System.out.println("Run ClientSendsInvalidChunksTTPS");
        ClientSendsInvalidChunksHTTPS client2 = new ClientSendsInvalidChunksHTTPS(host, port);
        client2.run(payload, method);
        System.out.println("Stop ClientSendsInvalidChunksTTPS");

        System.out.println("Run ClosesConnectionBeforeReceivingResponseTTPS");
        ClosesConnectionBeforeReceivingResponseHTTPS client3 = new ClosesConnectionBeforeReceivingResponseHTTPS(host, port);
        client3.run(payload, method);
        System.out.println("Stop ClosesConnectionBeforeReceivingResponseTTPS");

        System.out.println("Run ClosesConnectionBeforeSendingRequestBodyTTPS");
        ClosesConnectionBeforeSendingRequestBodyHTTPS client4 = new ClosesConnectionBeforeSendingRequestBodyHTTPS(host, port);
        client4.run(payload, method);
        System.out.println("Stop ClosesConnectionBeforeSendingRequestBodyTTPS");

        System.out.println("Run ClosesConnectionWhileReceivingResponseBodyTTPS");
        ClosesConnectionWhileReceivingResponseBodyHTTPS client5 = new ClosesConnectionWhileReceivingResponseBodyHTTPS(host, port);
        client5.run(payload, method);
        System.out.println("Stop ClosesConnectionWhileReceivingResponseBodyTTPS");

        System.out.println("Run ClosesConnectionWhileReceivingResponseHeadersTTPS");
        ClosesConnectionWhileReceivingResponseHeadersHTTPS client6 = new ClosesConnectionWhileReceivingResponseHeadersHTTPS(host, port);
        client6.run(payload, method);
        System.out.println("Stop ClosesConnectionWhileReceivingResponseHeadersTTPS");

        System.out.println("Run ClosesConnectionWhileSendingHeadersTTPS");
        ClosesConnectionWhileSendingHeadersHTTPS client7 = new ClosesConnectionWhileSendingHeadersHTTPS(host, port);
        client7.run(payload, method);
        System.out.println("Stop ClosesConnectionWhileSendingHeadersTTPS");

        System.out.println("Run ClosesConnectionWhileSendingRequestBodyTTPS");
        ClosesConnectionWhileSendingRequestBodyHTTPS client8 = new ClosesConnectionWhileSendingRequestBodyHTTPS(host, port);
        client8.run(payload, method);
        System.out.println("Stop ClosesConnectionWhileSendingRequestBodyTTPS");

        System.out.println("Run ContentLengthLessThanActualContentLengthTTPS");
        ContentLengthDifferFromActualContentLengthHTTPS client9 = new ContentLengthDifferFromActualContentLengthHTTPS(host, port);
        client9.run(payload, payload.getBytes().length - 10, method);
        System.out.println("Stop ContentLengthLessThanActualContentLengthTTPS");

        System.out.println("Run ContentLengthGreaterThanActualContentLengthTTPS");
        client9.run(payload, payload.getBytes().length + 10, method);
        System.out.println("Stop ContentLengthGreaterThanActualContentLengthTTPS");

        System.out.println("Run ContentTypeAndBodyMismatchTTPS");
        ContentTypeAndBodyMismatchHTTPS client10 = new ContentTypeAndBodyMismatchHTTPS(host, port);
        client10.run(payload, method);
        System.out.println("Stop ContentTypeAndBodyMismatchTTPS");

        System.out.println("Run MalformedRequestTTPS");
        MalformedRequestHTTPS client11 = new MalformedRequestHTTPS(host, port);
        client11.run(payload, method);
        System.out.println("Stop MalformedRequestTTPS");

        System.out.println("Run SlowReadHTTPS");
        SlowReadHTTPS client12 = new SlowReadHTTPS(host, port);
        client12.run(payload, method);
        System.out.println("Stop SlowReadHTTPS");

        System.out.println("Run WaitUntilAResponseReceivedHTTPS");
        WaitUntilAResponseReceivedHTTPS client13 = new WaitUntilAResponseReceivedHTTPS(host, port);
        client13.run(payload, method);
        System.out.println("Stop WaitUntilAResponseReceivedHTTPS");
    }
}
