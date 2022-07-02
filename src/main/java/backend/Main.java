package backend;

import client.helpers.RequestMethods;
import client.helpers.SimpleHTTPClient;
import client.helpers.SimpleHTTPSClient;
import client.helpers.TestPayloads;

public class Main {

    public static int waitForServerShutdown = 2000;
    public static int waitForServerStartup = 2000;

    public static String host = "localhost";
    public static int serverPort = 8290;

    public static int backendServerPort = 7000;

    public static String authorizationHeader = "Authorization: Bearer eyJ4NXQiOiJOVGRtWmpNNFpEazNOalkwWXpjNU1tWm1PRGd3TVRFM01XWXdOREU1TVdSbFpEZzROemM0WkEiLCJraWQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZ19SUzI1NiIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJhZG1pbiIsImF1dCI6IkFQUExJQ0FUSU9OIiwiYXVkIjoiY1o4Q2xHUE9rblBkZFo1eEpNZl9CX2k3VFBRYSIsIm5iZiI6MTY1MjIxNjQ5NiwiYXpwIjoiY1o4Q2xHUE9rblBkZFo1eEpNZl9CX2k3VFBRYSIsInNjb3BlIjoiZGVmYXVsdCIsImlzcyI6Imh0dHBzOlwvXC9sb2NhbGhvc3Q6OTQ0M1wvb2F1dGgyXC90b2tlbiIsImV4cCI6MTY1MjIyMDA5NiwiaWF0IjoxNjUyMjE2NDk2LCJqdGkiOiI0ZjliYjkzNy1iNzU4LTQ5MWEtYTM0ZC00Y2MyOTI2OWFhNzIifQ.tru_XsD-kGj2-Eaxsij4f55kM21LsDTKE7voW7SGhcZ2EllVJJBZdL7y_L8Jwv1tMWbfm_i5iHgtrnLrXJY3zUItpOU6IT04oBrFhiI2n4AWC138TeZvJXmH8W2ZAz2vddGpHogtvUwP5Ga_DT43Rtnh0PyTXySOlZQvL6LjR0oiWqjJaIMuuIohsNgRhVdjN8AgSeF2pb_h9jVJStkcK5eIHoom2ZZQeqr0EgtkJgCnft-Z143_83_KUe3pyAU4pzYYhVMTjYPKXIVhx56Z-HSt7UHCe2f1cu_viAyff-LzNcfpyBfj2u5rzTiYlfLtnKVM8ilS7b8hmH307oXb4A";

    public static void main(String[] args) throws Exception {

        System.setProperty("javax.net.ssl.keyStore", "/Users/apple/.wum3/products/wso2mi/4.0.0/wso2mi-4.0.0_http_core_testing/repository/resources/security/wso2carbon.jks");
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

        runHTTPCase(new WaitsUntilResponseDone(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        runHTTPCase(new BE200(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        runHTTPCase(new BE400(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        runHTTPCase(new BE500(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        runHTTPCase(new ContentLengthHeaderGreaterThanActualContentLength(), serverPort, serverPayload, clientPayload,
                method, noOfClientRequests, false);

        runHTTPCase(new ContentLengthHeaderLessThanActualContentLength(), serverPort, serverPayload, clientPayload,
                method, noOfClientRequests, false);

        runHTTPCase(new ContentTypeAndBodyMismatch(), serverPort, serverPayload, clientPayload, method,
                noOfClientRequests, false);

        runHTTPCase(new MalformedResponse(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        runHTTPCase(new BackendSendsChunks(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        // the following need to be separately executed one by one

//        runHTTPCase(new ClosesTheConnectionAsSoonAsTheRequestReceived(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//
//        runHTTPCase(new ClosesTheConnectionBeforeBackendSendsTheBody(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//
//        runHTTPCase(new ClosesTheConnectionWhileBackendReadingRequestHeaders(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//
//        runHTTPCase(new ClosesTheConnectionWhileBackendSendingTheHeaders(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//
//        runHTTPCase(new SlowReading(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
//
//        runHTTPCase(new SlowWriting(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

    }

    public void runAllHTTPSCases(int serverPort, String serverPayload, String clientPayload, RequestMethods method,
                                int noOfClientRequests) {

        runHTTPSCase(new WaitsUntilResponseDoneHTTPS(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        runHTTPSCase(new SSL200BE(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        runHTTPSCase(new SSL200BE(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        runHTTPSCase(new SSL200BE(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        runHTTPSCase(new ContentLengthHeaderGreaterThanActualContentLengthHTTPS(), serverPort, serverPayload, clientPayload,
                method, noOfClientRequests, false);

        runHTTPSCase(new ContentLengthHeaderLessThanActualContentLengthHTTPS(), serverPort, serverPayload, clientPayload,
                method, noOfClientRequests, false);

        runHTTPSCase(new ContentTypeAndBodyMismatchHTTPS(), serverPort, serverPayload, clientPayload, method,
                noOfClientRequests, false);

        runHTTPSCase(new MalformedResponseHTTPS(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        runHTTPSCase(new BackendSendsChunksHTTPS(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

        // the following need to be separately executed one by one as the endpoint getting suspended for these cases

//        runHTTPSCase(new ClosesTheConnectionAsSoonAsTheRequestReceivedHTTPS(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//
//        runHTTPSCase(new ClosesTheConnectionBeforeBackendSendsTheBodyHTTPS(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//
//        runHTTPSCase(new ClosesTheConnectionWhileBackendReadingRequestHeadersHTTPS(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//
//        runHTTPSCase(new ClosesTheConnectionWhileBackendSendingTheHeadersHTTPS(), serverPort, serverPayload, clientPayload,
//                method, noOfClientRequests, false);
//
//        runHTTPSCase(new SlowReading(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);
//
//        runHTTPSCase(new SlowWriting(), serverPort, serverPayload, clientPayload, method, noOfClientRequests, false);

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
        client.run(payload, method);
    }

    public void sendHTTPSClientRequest(String payload, RequestMethods method, boolean enableChunking) {
        SimpleHTTPSClient client = new SimpleHTTPSClient(host, serverPort);
        client.run(payload, method, authorizationHeader, enableChunking);
    }

}
