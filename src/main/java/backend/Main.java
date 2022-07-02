package backend;

import backend.case14.http.BackendClosesTheConnectionWhileMISendingTheRequestBody;
import backend.case14.http.SlowWriteClient;
import backend.case14.https.BackendClosesTheConnectionWhileMISendingTheRequestBodyHTTPS;
import backend.case14.https.SlowWriteClientHTTPS;
import client.helpers.RequestMethods;
import client.helpers.SimpleHTTPClient;
import client.helpers.SimpleHTTPSClient;
import client.helpers.TestPayloads;

public class Main {

    public static int waitForServerShutdown = 2000;
    public static int waitForServerStartup = 2000;

    public static String host = "localhost";
    public static int serverPort = 8280;
    public static int serverPortHTTPS = 8243;

    public static int backendServerPort = 7000;

    public static String authorizationHeader = "Authorization: Bearer eyJ4NXQiOiJOVGRtWmpNNFpEazNOalkwWXpjNU1tWm1PRGd3TVRFM01XWXdOREU1TVdSbFpEZzROemM0WkEiLCJraWQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZ19SUzI1NiIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJhZG1pbiIsImF1dCI6IkFQUExJQ0FUSU9OIiwiYXVkIjoidGhkcV9YWkxLR0ZzTlFvaW9jbW1ET0xyOFNrYSIsIm5iZiI6MTY1Njc1Nzk3MywiYXpwIjoidGhkcV9YWkxLR0ZzTlFvaW9jbW1ET0xyOFNrYSIsInNjb3BlIjoiZGVmYXVsdCIsImlzcyI6Imh0dHBzOlwvXC9sb2NhbGhvc3Q6OTQ0M1wvb2F1dGgyXC90b2tlbiIsImV4cCI6OTIyMzM3MjAzNjg1NDc3NSwiaWF0IjoxNjU2NzU3OTczLCJqdGkiOiI4YmQ2ZDEyOC1jNGM1LTQ4YTgtOGYyNi0zOWUwNGZkZmM3N2IifQ.vOxvleqNjCVY6h8lrDOqMl_SJn5rZfrOY-eWlIRNlS4aR9XSIz2JggJvqk5vaNYlWOACGl4Ma8yFVYeCCQqcmf8xXWXF0OA2mrnEZIGDTy-y8yjVDFhqKCobU6hNSPNLcAkwpEDvDNyngBvkf_PqTuhYC_R4pRaL7NJBdZoBNjRP1Mn3pQTE-XNqY-k-rFxkqfB2-yJC-BGIZH-isjXBGEEs_UFIj1idjwwk-S6aoIc4fixeNO9SXrGheaN-bB4gABqWQyrLLjfbu1Qjjkj-IJEuZoBfP6FqnJL3H4XCBxdAT2ZzAnsEryGmCFmCQBn6h01Mll-lDYijTFmAnAbC7g";

    public static void main(String[] args) {

        System.setProperty("javax.net.ssl.keyStore", "/Users/shefandarren/Documents/dbgermany/final/wso2am-4.0.0/repository/resources/security/wso2carbon.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "wso2carbon");

        Main main = new Main();
        // http
        main.runAllHTTPCases(backendServerPort, TestPayloads.SMALL_PAYLOAD, TestPayloads.SMALL_PAYLOAD, RequestMethods.GET, 1);
        main.runAllHTTPCases(backendServerPort, TestPayloads.SMALL_PAYLOAD, TestPayloads.SMALL_PAYLOAD, RequestMethods.POST, 1);
        main.runAllHTTPCases(backendServerPort, TestPayloads.SMALL_PAYLOAD, TestPayloads.SMALL_PAYLOAD, RequestMethods.PUT, 1);

        main.runAllHTTPCases(backendServerPort, TestPayloads.LARGE_PAYLOAD_25K, TestPayloads.SMALL_PAYLOAD, RequestMethods.GET, 1);
        main.runAllHTTPCases(backendServerPort, TestPayloads.LARGE_PAYLOAD_25K, TestPayloads.SMALL_PAYLOAD, RequestMethods.POST, 1);
        main.runAllHTTPCases(backendServerPort, TestPayloads.LARGE_PAYLOAD_25K, TestPayloads.SMALL_PAYLOAD, RequestMethods.PUT, 1);

        //https
        main.runAllHTTPSCases(backendServerPort, TestPayloads.SMALL_PAYLOAD, TestPayloads.SMALL_PAYLOAD, RequestMethods.GET, 1);
        main.runAllHTTPSCases(backendServerPort, TestPayloads.SMALL_PAYLOAD, TestPayloads.SMALL_PAYLOAD, RequestMethods.POST, 1);
        main.runAllHTTPSCases(backendServerPort, TestPayloads.SMALL_PAYLOAD, TestPayloads.SMALL_PAYLOAD, RequestMethods.PUT, 1);

        main.runAllHTTPSCases(backendServerPort, TestPayloads.LARGE_PAYLOAD_25K, TestPayloads.SMALL_PAYLOAD, RequestMethods.GET, 1);
        main.runAllHTTPSCases(backendServerPort, TestPayloads.LARGE_PAYLOAD_25K, TestPayloads.SMALL_PAYLOAD, RequestMethods.POST, 1);
        main.runAllHTTPSCases(backendServerPort, TestPayloads.LARGE_PAYLOAD_25K, TestPayloads.SMALL_PAYLOAD, RequestMethods.PUT, 1);

    }

    public void runAllHTTPCases(int serverPort, String serverPayload, String clientPayload, RequestMethods method,
                                int noOfClientRequests) {

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start WaitsUntilResponseDone backend");
        runHTTPCase(new WaitsUntilResponseDone(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End WaitsUntilResponseDone backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start BE200 backend");
        runHTTPCase(new BE200(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End BE200 backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start BE200 backend");
        runHTTPCase(new BE400(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End BE200 backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start BE500 backend");
        runHTTPCase(new BE500(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End BE500 backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ContentLengthHeaderGreaterThanActualContentLength backend");
        runHTTPCase(new ContentLengthHeaderGreaterThanActualContentLength(), serverPort, serverPayload, clientPayload,
                method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ContentLengthHeaderGreaterThanActualContentLength backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ContentLengthHeaderLessThanActualContentLength backend");
        runHTTPCase(new ContentLengthHeaderLessThanActualContentLength(), serverPort, serverPayload, clientPayload,
                method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ContentLengthHeaderLessThanActualContentLength backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ContentTypeAndBodyMismatch backend");
        runHTTPCase(new ContentTypeAndBodyMismatch(), serverPort, serverPayload, clientPayload, method,
                noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ContentTypeAndBodyMismatch backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start MalformedResponse backend");
        runHTTPCase(new MalformedResponse(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End MalformedResponse backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start BackendSendsChunks backend");
        runHTTPCase(new BackendSendsChunks(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End BackendSendsChunks backend");

        // the following need to be separately executed one by one

//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ClosesTheConnectionAsSoonAsTheRequestReceived backend");
//        runHTTPCase(new ClosesTheConnectionAsSoonAsTheRequestReceived(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ClosesTheConnectionAsSoonAsTheRequestReceived backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ClosesTheConnectionBeforeBackendSendsTheBody backend");
//        runHTTPCase(new ClosesTheConnectionBeforeBackendSendsTheBody(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ClosesTheConnectionBeforeBackendSendsTheBody backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ClosesTheConnectionWhileBackendReadingRequestHeaders backend");
//        runHTTPCase(new ClosesTheConnectionWhileBackendReadingRequestHeaders(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ClosesTheConnectionWhileBackendReadingRequestHeaders backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ClosesTheConnectionWhileBackendSendingTheHeaders backend");
//        runHTTPCase(new ClosesTheConnectionWhileBackendSendingTheHeaders(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ClosesTheConnectionWhileBackendSendingTheHeaders backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start SlowReading backend");
//        runHTTPCase(new SlowReading(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End SlowReading backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start SlowWriting backend");
//        runHTTPCase(new SlowWriting(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End SlowWriting backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start BackendClosesTheConnectionWhileMISendingTheRequestBody backend");
//        runCase14(serverPort, serverPayload, noOfClientRequests);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End BackendClosesTheConnectionWhileMISendingTheRequestBody backend");

    }

    public void runAllHTTPSCases(int serverPort, String serverPayload, String clientPayload, RequestMethods method,
                                int noOfClientRequests) {

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start WaitsUntilResponseDoneHTTPS backend");
        runHTTPSCase(new WaitsUntilResponseDoneHTTPS(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End WaitsUntilResponseDoneHTTPS backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start SSL200BE backend");
        runHTTPSCase(new SSL200BE(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End SSL200BE backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start SSL400BE backend");
        runHTTPSCase(new SSL400BE(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End SSL400BE backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start SSL500BE backend");
        runHTTPSCase(new SSL500BE(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End SSL500BE backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ContentLengthHeaderGreaterThanActualContentLengthHTTPS backend");
        runHTTPSCase(new ContentLengthHeaderGreaterThanActualContentLengthHTTPS(), serverPort, serverPayload, clientPayload,
                method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ContentLengthHeaderGreaterThanActualContentLengthHTTPS backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ContentLengthHeaderLessThanActualContentLengthHTTPS backend");
        runHTTPSCase(new ContentLengthHeaderLessThanActualContentLengthHTTPS(), serverPort, serverPayload, clientPayload,
                method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ContentLengthHeaderLessThanActualContentLengthHTTPS backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ContentTypeAndBodyMismatchHTTPS backend");
        runHTTPSCase(new ContentTypeAndBodyMismatchHTTPS(), serverPort, serverPayload, clientPayload, method,
                noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ContentTypeAndBodyMismatchHTTPS backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start MalformedResponseHTTPS backend");
        runHTTPSCase(new MalformedResponseHTTPS(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End MalformedResponseHTTPS backend");

        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start BackendSendsChunksHTTPS backend");
        runHTTPSCase(new BackendSendsChunksHTTPS(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End BackendSendsChunksHTTPS backend");

        // the following need to be separately executed one by one as the endpoint getting suspended for these cases

//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ClosesTheConnectionAsSoonAsTheRequestReceivedHTTPS backend");
//        runHTTPSCase(new ClosesTheConnectionAsSoonAsTheRequestReceivedHTTPS(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ClosesTheConnectionAsSoonAsTheRequestReceivedHTTPS backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ClosesTheConnectionBeforeBackendSendsTheBodyHTTPS backend");
//        runHTTPSCase(new ClosesTheConnectionBeforeBackendSendsTheBodyHTTPS(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ClosesTheConnectionBeforeBackendSendsTheBodyHTTPS backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ClosesTheConnectionWhileBackendReadingRequestHeadersHTTPS backend");
//        runHTTPSCase(new ClosesTheConnectionWhileBackendReadingRequestHeadersHTTPS(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ClosesTheConnectionWhileBackendReadingRequestHeadersHTTPS backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start ClosesTheConnectionWhileBackendSendingTheHeadersHTTPS backend");
//        runHTTPSCase(new ClosesTheConnectionWhileBackendSendingTheHeadersHTTPS(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End ClosesTheConnectionWhileBackendSendingTheHeadersHTTPS backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start SlowReading backend");
//        runHTTPSCase(new SlowReading(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End SlowReading backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start SlowWriting backend");
//        runHTTPSCase(new SlowWriting(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End SlowWriting backend");
//
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>> Start BackendClosesTheConnectionWhileMISendingTheRequestBodyHTTPS backend");
//        runCase14HTTPS(serverPort, serverPayload, noOfClientRequests);
//        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<< End BackendClosesTheConnectionWhileMISendingTheRequestBodyHTTPS backend");

    }

    public void runHTTPCase(BackendServer bE1, int serverPort, String serverPayload, String clientPayload,
                            RequestMethods method, int noOfClientRequests, boolean enableChunkingClient) {

        startBackendServer(bE1, serverPort, serverPayload);

        try {
            System.out.println("Waiting until backend starts!");
            Thread.sleep(waitForServerStartup);
        } catch (InterruptedException e) {
            //
        }

        for (int i = 0; i < noOfClientRequests; i++) {
            sendHTTPClientRequest(clientPayload, method, enableChunkingClient);
        }

        bE1.shutdownServer();

        try {
            Thread.sleep(waitForServerShutdown);
        } catch (InterruptedException e) {
            //
        }
    }

    public void runHTTPSCase(BackendServer bE1, int serverPort, String serverPayload, String clientPayload,
                            RequestMethods method, int noOfClientRequests, boolean enableChunkingClient) {

        startBackendServer(bE1, serverPort, serverPayload);

        try {
            System.out.println("Waiting until backend starts!");
            Thread.sleep(waitForServerStartup);
        } catch (InterruptedException e) {
            //
        }

        for (int i = 0; i < noOfClientRequests; i++) {
            sendHTTPSClientRequest(clientPayload, method, enableChunkingClient);
        }

        bE1.shutdownServer();

        try {
            Thread.sleep(waitForServerShutdown);
        } catch (InterruptedException e) {
            //
        }
    }

    public void startBackendServer(BackendServer bE1, int port, String payload) {
        Runnable basic = () -> {
            try {
                bE1.run(port, payload);
            } catch (Exception ignore) {
                //
            }
        };
        Thread thread1 = new Thread(basic);
        thread1.start();
    }

    public void sendHTTPClientRequest(String payload, RequestMethods method, boolean enableChunking) {
        SimpleHTTPClient client = new SimpleHTTPClient(host, serverPort);
        client.run(payload, method, authorizationHeader, enableChunking);
    }

    public void sendHTTPSClientRequest(String payload, RequestMethods method, boolean enableChunking) {
        SimpleHTTPSClient client = new SimpleHTTPSClient(host, serverPortHTTPS);
        client.run(payload, method, authorizationHeader, enableChunking);
    }

    public void runCase14(int port, String serverPayload, int noOfClientRequests) {
        BackendServer bE1 = new BackendClosesTheConnectionWhileMISendingTheRequestBody();
        startBackendServer(bE1, port, serverPayload);

        try {
            System.out.println("Waiting until backend starts!");
            Thread.sleep(waitForServerStartup);
        } catch (InterruptedException e) {
            //
        }

        SlowWriteClient client = new SlowWriteClient(host, serverPort);
        for (int i = 0; i < noOfClientRequests; i++) {
            client.run(TestPayloads.LARGE_PAYLOAD_25K);
        }

        bE1.shutdownServer();

        try {
            Thread.sleep(waitForServerShutdown);
        } catch (InterruptedException e) {
            //
        }
    }

    public void runCase14HTTPS(int port, String serverPayload, int noOfClientRequests) {
        BackendServer bE1 = new BackendClosesTheConnectionWhileMISendingTheRequestBodyHTTPS();
        startBackendServer(bE1, port, serverPayload);

        try {
            System.out.println("Waiting until backend starts!");
            Thread.sleep(waitForServerStartup);
        } catch (InterruptedException e) {
            //
        }

        SlowWriteClientHTTPS client = new SlowWriteClientHTTPS(host, serverPortHTTPS);
        for (int i = 0; i < noOfClientRequests; i++) {
            client.run(TestPayloads.LARGE_PAYLOAD_25K, authorizationHeader);
        }

        bE1.shutdownServer();

        try {
            Thread.sleep(waitForServerShutdown);
        } catch (InterruptedException e) {
            //
        }
    }

}
