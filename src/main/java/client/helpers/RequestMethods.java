package client.helpers;

public enum RequestMethods {
    GET("GET"),
    POST("POST"),
    PUT("PUT");

    private final RequestMethods methodName;
    RequestMethods(String name) {
        this.methodName = name;
    }

    @Override
    public String toString() {
        return this.methodName;
    }
}
